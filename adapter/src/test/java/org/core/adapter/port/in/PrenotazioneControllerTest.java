package org.core.adapter.port.in;

import org.core.adapter.mapper.PrenotazioneMapper;
import org.core.domain.model.PrenotazioneRecord;
import org.core.domain.model.PrenotazioniDisponibiliEventoRecord;
import org.core.domain.port.in.PrenotazioneUseCasePortIn;
import org.openapitools.model.Prenotazione;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrenotazioneController.class)
class PrenotazioneControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrenotazioneUseCasePortIn prenotazioneUseCasePortIn;

    @MockBean
    private PrenotazioneMapper prenotazioneMapper;

    @Test
    void createsReservationAndCoversAvailabilityFailures() throws Exception {
        PrenotazioneRecord record = reservationRecord(1, 10, 20, 2);
        PrenotazioneRecord alteredRecord = reservationRecord(2, 11, 20, 2);
        when(prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(20))
                .thenReturn(Optional.of(new PrenotazioniDisponibiliEventoRecord(20, 10, 5)));
        when(prenotazioneUseCasePortIn.save(any(PrenotazioneRecord.class)))
                .thenReturn(record, alteredRecord, null);
        when(prenotazioneMapper.toPrenotazione(record)).thenReturn(reservation(1));
        when(prenotazioneMapper.toPrenotazione(alteredRecord)).thenReturn(reservation(2));
        String body = """
                {"idUtente":10,"idEvento":20,"numeroPartecipanti":2}
                """;

        mockMvc.perform(post("/prenotazioni")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPrenotazione").value(1))
                .andExpect(jsonPath("$.numeroPartecipanti").value(2));

        mockMvc.perform(post("/prenotazioni")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPrenotazione").value(2));

        mockMvc.perform(post("/prenotazioni")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());

        when(prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(21)).thenReturn(Optional.empty());
        mockMvc.perform(post("/prenotazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idUtente":10,"idEvento":21,"numeroPartecipanti":2}
                                """))
                .andExpect(status().isNotFound());

        when(prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(22))
                .thenReturn(Optional.of(new PrenotazioniDisponibiliEventoRecord(22, 10, 1)));
        mockMvc.perform(post("/prenotazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idUtente":10,"idEvento":22,"numeroPartecipanti":2}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void readsReservationsAndAvailableSeats() throws Exception {
        PrenotazioneRecord record = reservationRecord(1, 10, 20, 2);
        when(prenotazioneUseCasePortIn.findById(1)).thenReturn(Optional.of(record));
        when(prenotazioneUseCasePortIn.findById(2)).thenReturn(Optional.empty());
        when(prenotazioneUseCasePortIn.getByDataFilter(any(), any())).thenReturn(List.of(record));
        when(prenotazioneUseCasePortIn.findByEvento(20)).thenReturn(List.of(record));
        when(prenotazioneUseCasePortIn.findByEvento(21)).thenReturn(List.of());
        when(prenotazioneUseCasePortIn.findByUtente(10)).thenReturn(List.of(record));
        when(prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(20))
                .thenReturn(Optional.of(new PrenotazioniDisponibiliEventoRecord(20, 10, 5)));
        when(prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(21)).thenReturn(Optional.empty());
        when(prenotazioneMapper.toPrenotazione(record)).thenReturn(reservation(1));

        mockMvc.perform(get("/prenotazioni/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUtente").value(10));
        mockMvc.perform(get("/prenotazioni/2")).andExpect(status().isNotFound());

        mockMvc.perform(get("/prenotazioni/by-data")
                        .param("dataInizio", "2026-05-01T00:00:00")
                        .param("dataFine", "2026-05-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPrenotazione").value(1));

        mockMvc.perform(get("/eventi/20/prenotazioni"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEvento").value(20));
        mockMvc.perform(get("/eventi/21/prenotazioni"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        mockMvc.perform(get("/prenotazioni/cercaPrenotazioneByIdUtente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUtente").value(10));

        mockMvc.perform(get("/eventi/20/prenotazioni/disponibili"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numeroPostiTotali").value(10))
                .andExpect(jsonPath("$.numeroPostiDispobili").value(5));
        mockMvc.perform(get("/eventi/21/prenotazioni/disponibili"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatesAndDeletesReservation() throws Exception {
        PrenotazioneRecord record = reservationRecord(1, 10, 20, 2);
        when(prenotazioneUseCasePortIn.findById(1)).thenReturn(Optional.of(record));
        when(prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(20))
                .thenReturn(Optional.of(new PrenotazioniDisponibiliEventoRecord(20, 10, 5)));
        when(prenotazioneUseCasePortIn.updateById(1, 3)).thenReturn(true);
        when(prenotazioneUseCasePortIn.updateById(1, 8)).thenReturn(false);
        when(prenotazioneUseCasePortIn.deleteById(1)).thenReturn(true);
        when(prenotazioneUseCasePortIn.deleteById(2)).thenReturn(false);

        mockMvc.perform(patch("/prenotazioni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroPartecipanti":3}
                                """))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/prenotazioni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroPartecipanti":8}
                                """))
                .andExpect(status().isBadRequest());
        mockMvc.perform(patch("/prenotazioni/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numeroPartecipanti":3}
                                """))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/prenotazioni/1")).andExpect(status().isOk());
        mockMvc.perform(delete("/prenotazioni/2")).andExpect(status().isNotFound());
    }

    private static PrenotazioneRecord reservationRecord(int id, int userId, int eventId, int guests) {
        LocalDateTime now = LocalDateTime.parse("2026-05-01T10:00:00");
        return new PrenotazioneRecord(id, userId, eventId, guests, now, now);
    }

    private static Prenotazione reservation(int id) {
        Prenotazione reservation = new Prenotazione();
        reservation.setIdPrenotazione(id);
        reservation.setIdUtente(10);
        reservation.setIdEvento(20);
        reservation.setNumeroPartecipanti(2);
        reservation.setDataCreazione(LocalDateTime.parse("2026-05-01T10:00:00"));
        reservation.setDataUltimaModifica(LocalDateTime.parse("2026-05-01T10:00:00"));
        return reservation;
    }
}
