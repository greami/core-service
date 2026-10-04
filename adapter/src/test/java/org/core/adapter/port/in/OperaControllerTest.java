package org.core.adapter.port.in;

import org.core.adapter.mapper.OperaMapper;
import org.core.domain.model.OperaRecord;
import org.core.domain.port.in.OperaUseCasePortIn;
import org.openapitools.model.Opera;
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

@WebMvcTest(OperaController.class)
class OperaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperaUseCasePortIn operaUseCasePortIn;

    @MockBean
    private OperaMapper operaMapper;

    @Test
    void listsAndSearchesWorks() throws Exception {
        OperaRecord record = new OperaRecord(1, "Guernica", "Picasso", 1);
        when(operaUseCasePortIn.findAll()).thenReturn(List.of(record));
        when(operaUseCasePortIn.findByEvento(10)).thenReturn(List.of(record));
        when(operaUseCasePortIn.getByNomeLike("Guer")).thenReturn(List.of(record));
        when(operaUseCasePortIn.getByAutoreLike("Picasso")).thenReturn(List.of(record));
        when(operaMapper.toOpera(record)).thenReturn(opera(1, "Guernica"));

        mockMvc.perform(get("/opere"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idOpera").value(1))
                .andExpect(jsonPath("$[0].nome").value("Guernica"));
        mockMvc.perform(get("/opere/by-evento/10"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].autore").value("Picasso"));
        mockMvc.perform(get("/opere/by-nome/Guer"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].idOpera").value(1));
        mockMvc.perform(get("/opere/by-autore/Picasso"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].tipo").value(1));
    }

    @Test
    void findsWorkAndReturnsNotFoundWhenMissing() throws Exception {
        OperaRecord record = new OperaRecord(1, "Guernica", "Picasso", 1);
        when(operaUseCasePortIn.findById(1)).thenReturn(Optional.of(record));
        when(operaUseCasePortIn.findById(2)).thenReturn(Optional.empty());
        when(operaMapper.toOpera(record)).thenReturn(opera(1, "Guernica"));

        mockMvc.perform(get("/opere/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Guernica"));
        mockMvc.perform(get("/opere/2")).andExpect(status().isNotFound());
    }

    @Test
    void createsWorkAndHandlesSaveFailure() throws Exception {
        OperaRecord record = new OperaRecord(1, "Guernica", "Picasso", 1);
        when(operaUseCasePortIn.save(any(OperaRecord.class))).thenReturn(record);
        when(operaMapper.toOpera(record)).thenReturn(opera(1, "Guernica"));
        String body = """
                {"nome":"Guernica","autore":"Picasso","tipo":1}
                """;

        mockMvc.perform(post("/opere").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idOpera").value(1))
                .andExpect(jsonPath("$.autore").value("Picasso"));

        when(operaUseCasePortIn.save(any(OperaRecord.class))).thenReturn(null);
        mockMvc.perform(post("/opere").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsCreatedWhenSaveProducesDifferentWork() throws Exception {
        OperaRecord saved = new OperaRecord(2, "Guernica salvata", "Picasso", 1);
        when(operaUseCasePortIn.save(any(OperaRecord.class))).thenReturn(saved);
        when(operaMapper.toOpera(saved)).thenReturn(opera(2, "Guernica salvata"));

        mockMvc.perform(post("/opere")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Guernica","autore":"Picasso","tipo":1}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idOpera").value(2));
    }

    @Test
    void updatesAndDeletesWork() throws Exception {
        when(operaUseCasePortIn.updateById(org.mockito.ArgumentMatchers.eq(1), any(OperaRecord.class)))
                .thenReturn(true, false);
        when(operaUseCasePortIn.deleteById(1)).thenReturn(true);
        when(operaUseCasePortIn.deleteById(2)).thenReturn(false);
        String body = """
                {"nome":"Guernica","autore":"Picasso","tipo":1}
                """;

        mockMvc.perform(patch("/opere/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/opere/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/opere/1")).andExpect(status().isOk());
        mockMvc.perform(delete("/opere/2")).andExpect(status().isNotFound());
    }

    private static Opera opera(int id, String nome) {
        Opera opera = new Opera();
        opera.setIdOpera(id);
        opera.setNome(nome);
        opera.setAutore("Picasso");
        opera.setTipo(1);
        return opera;
    }
}
