package org.core.adapter.port.in;

import org.core.adapter.mapper.FeedbackOpereMapper;
import org.core.domain.model.FeedbackOpereRecord;
import org.core.domain.port.in.FeedbackOpereUseCasePortIn;
import org.openapitools.model.FeedbackOpere;
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

@WebMvcTest(FeedbackOpereController.class)
class FeedbackOpereControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackOpereUseCasePortIn feedbackOpereUseCasePortIn;

    @MockBean
    private FeedbackOpereMapper feedbackOpereMapper;

    @Test
    void createsFeedbackAndHandlesSaveFailure() throws Exception {
        FeedbackOpereRecord record = new FeedbackOpereRecord(1, 10, 20, 5, "Ottimo");
        when(feedbackOpereUseCasePortIn.save(any(FeedbackOpereRecord.class))).thenReturn(record);
        when(feedbackOpereMapper.toFeedbackOpere(record)).thenReturn(feedback());
        String body = """
                {"idUtente":10,"idOpera":20,"voto":5,"note":"Ottimo"}
                """;

        mockMvc.perform(post("/feedback-opere")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idFeedback").value(1))
                .andExpect(jsonPath("$.note").value("Ottimo"));

        when(feedbackOpereUseCasePortIn.save(any(FeedbackOpereRecord.class))).thenReturn(null);
        mockMvc.perform(post("/feedback-opere")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsCreatedWhenSaveProducesDifferentFeedback() throws Exception {
        FeedbackOpereRecord saved = new FeedbackOpereRecord(2, 10, 20, 4, "Nota salvata");
        FeedbackOpere response = feedback();
        response.setIdFeedback(2);
        response.setVoto(4);
        response.setNote("Nota salvata");
        when(feedbackOpereUseCasePortIn.save(any(FeedbackOpereRecord.class))).thenReturn(saved);
        when(feedbackOpereMapper.toFeedbackOpere(saved)).thenReturn(response);

        mockMvc.perform(post("/feedback-opere")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idUtente":10,"idOpera":20,"voto":5,"note":"Ottimo"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.voto").value(4));
    }

    @Test
    void findsFeedbackAndReturnsNotFoundWhenMissing() throws Exception {
        FeedbackOpereRecord record = new FeedbackOpereRecord(1, 10, 20, 5, "Ottimo");
        when(feedbackOpereUseCasePortIn.findById(1)).thenReturn(Optional.of(record));
        when(feedbackOpereUseCasePortIn.findById(2)).thenReturn(Optional.empty());
        when(feedbackOpereMapper.toFeedbackOpere(record)).thenReturn(feedback());

        mockMvc.perform(get("/feedback-opere/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idOpera").value(20));
        mockMvc.perform(get("/feedback-opere/2")).andExpect(status().isNotFound());
    }

    @Test
    void listsFeedbackByUserAndWork() throws Exception {
        FeedbackOpereRecord record = new FeedbackOpereRecord(1, 10, 20, 5, "Ottimo");
        when(feedbackOpereUseCasePortIn.findByUtente(10)).thenReturn(List.of(record));
        when(feedbackOpereUseCasePortIn.findByOpera(20)).thenReturn(List.of(record));
        when(feedbackOpereMapper.toFeedbackOpere(record)).thenReturn(feedback());

        mockMvc.perform(get("/feedback-opere/cercaFeedbackOperaByIdUtente/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idUtente").value(10));
        mockMvc.perform(get("/opere/20/feedback-opere"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFeedback").value(1));
    }

    @Test
    void updatesAndDeletesFeedback() throws Exception {
        when(feedbackOpereUseCasePortIn.updateById(1, 4, "Buono")).thenReturn(true);
        when(feedbackOpereUseCasePortIn.updateById(2, 4, "Buono")).thenReturn(false);
        when(feedbackOpereUseCasePortIn.deleteById(1)).thenReturn(true);
        when(feedbackOpereUseCasePortIn.deleteById(2)).thenReturn(false);
        String body = """
                {"voto":4,"note":"Buono"}
                """;

        mockMvc.perform(patch("/feedback-opere/1")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/feedback-opere/2")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/feedback-opere/1")).andExpect(status().isOk());
        mockMvc.perform(delete("/feedback-opere/2")).andExpect(status().isNotFound());
    }

    private static FeedbackOpere feedback() {
        FeedbackOpere feedback = new FeedbackOpere();
        feedback.setIdFeedback(1);
        feedback.setIdUtente(10);
        feedback.setIdOpera(20);
        feedback.setVoto(5);
        feedback.setNote("Ottimo");
        return feedback;
    }
}
