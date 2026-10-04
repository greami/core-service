package org.core.adapter.port.in;

import org.core.adapter.mapper.EventoOperaMapper;
import org.core.domain.model.EventoOperaRecord;
import org.core.domain.port.in.EventoOperaUseCasePortIn;
import org.openapitools.model.EventoOpera;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(EventoOperaController.class)
class EventoOperaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoOperaUseCasePortIn eventoOperaUseCasePortIn;

    @MockBean
    private EventoOperaMapper eventoOperaMapper;

    @Test
    void createsAssociationAndHandlesSaveFailure() throws Exception {
        EventoOperaRecord record = new EventoOperaRecord(10, 20);
        when(eventoOperaUseCasePortIn.save(any(EventoOperaRecord.class))).thenReturn(record);
        when(eventoOperaMapper.toEventoOpera(record)).thenReturn(association());
        String body = """
                {"idEvento":10,"idOpera":20}
                """;

        mockMvc.perform(post("/evento-opera").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idEvento").value(10))
                .andExpect(jsonPath("$.idOpera").value(20));

        when(eventoOperaUseCasePortIn.save(any(EventoOperaRecord.class))).thenReturn(null);
        mockMvc.perform(post("/evento-opera").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsCreatedWhenSaveProducesDifferentAssociation() throws Exception {
        EventoOperaRecord saved = new EventoOperaRecord(10, 21);
        EventoOpera response = new EventoOpera();
        response.setIdEvento(10);
        response.setIdOpera(21);
        when(eventoOperaUseCasePortIn.save(any(EventoOperaRecord.class))).thenReturn(saved);
        when(eventoOperaMapper.toEventoOpera(saved)).thenReturn(response);

        mockMvc.perform(post("/evento-opera")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idEvento":10,"idOpera":20}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idOpera").value(21));
    }

    @Test
    void findsAssociationAndReturnsNotFoundWhenMissing() throws Exception {
        EventoOperaRecord record = new EventoOperaRecord(10, 20);
        when(eventoOperaUseCasePortIn.findById(10, 20)).thenReturn(Optional.of(record));
        when(eventoOperaUseCasePortIn.findById(10, 21)).thenReturn(Optional.empty());
        when(eventoOperaMapper.toEventoOpera(record)).thenReturn(association());

        mockMvc.perform(get("/evento-opera/10/20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idOpera").value(20));
        mockMvc.perform(get("/evento-opera/10/21")).andExpect(status().isNotFound());
    }

    @Test
    void listsAssociationsByEventAndWork() throws Exception {
        EventoOperaRecord record = new EventoOperaRecord(10, 20);
        when(eventoOperaUseCasePortIn.findByEvento(10)).thenReturn(List.of(record));
        when(eventoOperaUseCasePortIn.findByEvento(11)).thenReturn(List.of());
        when(eventoOperaUseCasePortIn.findByOpera(20)).thenReturn(List.of(record));
        when(eventoOperaMapper.toEventoOpera(record)).thenReturn(association());

        mockMvc.perform(get("/eventi/10/opere"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idEvento").value(10));
        mockMvc.perform(get("/eventi/11/opere")).andExpect(status().isNotFound());
        mockMvc.perform(get("/opere/20/eventi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOpera").value(20));
    }

    @Test
    void updatesAndDeletesAssociation() throws Exception {
        when(eventoOperaUseCasePortIn.updateById(10, 20, 21)).thenReturn(true, false);
        when(eventoOperaUseCasePortIn.deleteById(10, 20)).thenReturn(true);
        when(eventoOperaUseCasePortIn.deleteById(10, 21)).thenReturn(false);
        String body = """
                {"idOperaNuova":21}
                """;

        mockMvc.perform(patch("/evento-opera/10/20")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/evento-opera/10/20")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/evento-opera/10/20")).andExpect(status().isOk());
        mockMvc.perform(delete("/evento-opera/10/21")).andExpect(status().isNotFound());
    }

    private static EventoOpera association() {
        EventoOpera association = new EventoOpera();
        association.setIdEvento(10);
        association.setIdOpera(20);
        return association;
    }
}
