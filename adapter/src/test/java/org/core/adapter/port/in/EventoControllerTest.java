package org.core.adapter.port.in;

import org.core.adapter.mapper.EventoMapper;
import org.core.domain.model.EventoRecord;
import org.core.domain.port.in.EventoUseCasePortIn;
import org.openapitools.model.Evento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.servlet.ServletException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventoController.class)
class EventoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoUseCasePortIn eventoUseCasePortIn;

    @MockBean
    private EventoMapper eventoMapper;

    @Test
    void listsAndSearchesEvents() throws Exception {
        EventoRecord record = record(1, "Mostra");
        when(eventoUseCasePortIn.findAll()).thenReturn(List.of(record));
        when(eventoUseCasePortIn.getByDataFilter(any(), any())).thenReturn(List.of(record));
        when(eventoUseCasePortIn.getByNomeLike("Mostra")).thenReturn(List.of(record));
        when(eventoMapper.toEvento(record)).thenReturn(evento(1, "Mostra"));

        mockMvc.perform(get("/eventi"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idEvento").value(1))
                .andExpect(jsonPath("$[0].nome").value("Mostra"));

        mockMvc.perform(get("/eventi/by-data")
                        .param("dataInizio", "2026-05-01T00:00:00")
                        .param("dataFine", "2026-05-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEvento").value(1));

        mockMvc.perform(get("/eventi/by-nome/Mostra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Mostra"));
    }

    @Test
    void findsEventOrReturnsNotFound() throws Exception {
        EventoRecord record = record(1, "Mostra");
        when(eventoUseCasePortIn.getById(1)).thenReturn(record);
        when(eventoUseCasePortIn.getById(2)).thenReturn(null);
        when(eventoMapper.toEvento(record)).thenReturn(evento(1, "Mostra"));

        mockMvc.perform(get("/eventi/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idEvento").value(1));

        mockMvc.perform(get("/eventi/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createsEventAndHandlesSaveFailure() throws Exception {
        EventoRecord record = record(1, "Mostra");
        when(eventoUseCasePortIn.save(any(EventoRecord.class))).thenReturn(record);
        when(eventoMapper.toEvento(record)).thenReturn(evento(1, "Mostra"));

        mockMvc.perform(post("/eventi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"Nome":"Mostra","numeroPosti":30,"data":"2026-05-01T10:00:00","durata":2}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idEvento").value(1))
                .andExpect(jsonPath("$.nome").value("Mostra"));

        when(eventoUseCasePortIn.save(any(EventoRecord.class))).thenReturn(null);
        mockMvc.perform(post("/eventi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"Nome":"Mostra","numeroPosti":30,"data":"2026-05-01T10:00:00","durata":2}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsCreatedWhenSaveProducesDifferentEvent() throws Exception {
        EventoRecord saved = record(2, "Mostra salvata");
        when(eventoUseCasePortIn.save(any(EventoRecord.class))).thenReturn(saved);
        when(eventoMapper.toEvento(saved)).thenReturn(evento(2, "Mostra salvata"));

        mockMvc.perform(post("/eventi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"Nome":"Mostra","numeroPosti":30,"data":"2026-05-01T10:00:00","durata":2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idEvento").value(2));
    }

    @Test
    void updatesAndDeletesEvents() throws Exception {
        when(eventoUseCasePortIn.updateById(org.mockito.ArgumentMatchers.eq(1), any(EventoRecord.class)))
                .thenReturn(true, false);
        when(eventoUseCasePortIn.deleteById(1)).thenReturn(true);
        when(eventoUseCasePortIn.deleteById(2)).thenReturn(false);

        mockMvc.perform(patch("/eventi/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idEvento":1,"nome":"Mostra aggiornata","numeroPosti":30,
                                 "data":"2026-05-01T10:00:00","durata":2}
                                """))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/eventi/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idEvento":1,"nome":"Mostra aggiornata","numeroPosti":30,
                                 "data":"2026-05-01T10:00:00","durata":2}
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete("/eventi/1")).andExpect(status().isOk());
        mockMvc.perform(delete("/eventi/2")).andExpect(status().isNotFound());
    }

    @Test
    void propagatesDependencyFailure() {
        when(eventoUseCasePortIn.getById(3)).thenThrow(new IllegalStateException("service unavailable"));

        assertThrows(ServletException.class, () -> mockMvc.perform(get("/eventi/3")).andReturn());
    }

    private static EventoRecord record(int id, String nome) {
        return new EventoRecord(id, nome, 30, LocalDateTime.parse("2026-05-01T10:00:00"), 2);
    }

    private static Evento evento(int id, String nome) {
        Evento evento = new Evento();
        evento.setIdEvento(id);
        evento.setNome(nome);
        evento.setNumeroPosti(30);
        evento.setData(LocalDateTime.parse("2026-05-01T10:00:00"));
        evento.setDurata(2);
        return evento;
    }
}
