package org.core.adapter.port.in;

import org.core.adapter.mapper.FeedbackEventiMapper;
import org.core.domain.model.FeedbackEventiRecord;
import org.core.domain.port.in.FeedbackEventiUseCasePortIn;
import org.openapitools.model.FeedbackEventi;
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

@WebMvcTest(FeedbackEventiController.class)
class FeedbackEventiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackEventiUseCasePortIn feedbackEventiUseCasePortIn;

    @MockBean
    private FeedbackEventiMapper feedbackEventiMapper;

    @Test
    void createsFeedbackAndHandlesSaveFailure() throws Exception {
        FeedbackEventiRecord record = new FeedbackEventiRecord(1, 10, 20, 5, "Ottimo");
        when(feedbackEventiUseCasePortIn.save(any(FeedbackEventiRecord.class))).thenReturn(record);
        when(feedbackEventiMapper.toFeedbackEventi(record)).thenReturn(feedback());
        String body = """
                {"idUtente":10,"idEvento":20,"voto":5,"note":"Ottimo"}
                """;

        mockMvc.perform(post("/feedback-eventi")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idFeedback").value(1))
                .andExpect(jsonPath("$.note").value("Ottimo"));

        when(feedbackEventiUseCasePortIn.save(any(FeedbackEventiRecord.class))).thenReturn(null);
        mockMvc.perform(post("/feedback-eventi")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsCreatedWhenSaveProducesDifferentFeedback() throws Exception {
        FeedbackEventiRecord saved = new FeedbackEventiRecord(2, 10, 20, 4, "Nota salvata");
        FeedbackEventi response = feedback();
        response.setIdFeedback(2);
        response.setVoto(4);
        response.setNote("Nota salvata");
        when(feedbackEventiUseCasePortIn.save(any(FeedbackEventiRecord.class))).thenReturn(saved);
        when(feedbackEventiMapper.toFeedbackEventi(saved)).thenReturn(response);

        mockMvc.perform(post("/feedback-eventi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idUtente":10,"idEvento":20,"voto":5,"note":"Ottimo"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.voto").value(4));
    }

    @Test
    void findsFeedbackAndReturnsNotFoundWhenMissing() throws Exception {
        FeedbackEventiRecord record = new FeedbackEventiRecord(1, 10, 20, 5, "Ottimo");
        when(feedbackEventiUseCasePortIn.findById(1)).thenReturn(Optional.of(record));
        when(feedbackEventiUseCasePortIn.findById(2)).thenReturn(Optional.empty());
        when(feedbackEventiMapper.toFeedbackEventi(record)).thenReturn(feedback());

        mockMvc.perform(get("/feedback-eventi/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idEvento").value(20));
        mockMvc.perform(get("/feedback-eventi/2")).andExpect(status().isNotFound());
    }

    @Test
    void listsFeedbackByUserAndEvent() throws Exception {
        FeedbackEventiRecord record = new FeedbackEventiRecord(1, 10, 20, 5, "Ottimo");
        when(feedbackEventiUseCasePortIn.findByUtente(10)).thenReturn(List.of(record));
        when(feedbackEventiUseCasePortIn.findByEvento(20)).thenReturn(List.of(record));
        when(feedbackEventiUseCasePortIn.findByEvento(21)).thenReturn(List.of());
        when(feedbackEventiMapper.toFeedbackEventi(record)).thenReturn(feedback());

        mockMvc.perform(get("/feedback-eventi/cercaFeedbackEventoByIdUtente/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idUtente").value(10));
        mockMvc.perform(get("/eventi/20/feedback-eventi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFeedback").value(1));
        mockMvc.perform(get("/eventi/21/feedback-eventi")).andExpect(status().isNotFound());
    }

    @Test
    void updatesAndDeletesFeedback() throws Exception {
        when(feedbackEventiUseCasePortIn.updateById(1, 4, "Buono")).thenReturn(true);
        when(feedbackEventiUseCasePortIn.updateById(2, 4, "Buono")).thenReturn(false);
        when(feedbackEventiUseCasePortIn.deleteById(1)).thenReturn(true);
        when(feedbackEventiUseCasePortIn.deleteById(2)).thenReturn(false);
        String body = """
                {"voto":4,"note":"Buono"}
                """;

        mockMvc.perform(patch("/feedback-eventi/1")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/feedback-eventi/2")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/feedback-eventi/1")).andExpect(status().isOk());
        mockMvc.perform(delete("/feedback-eventi/2")).andExpect(status().isNotFound());
    }

    private static FeedbackEventi feedback() {
        FeedbackEventi feedback = new FeedbackEventi();
        feedback.setIdFeedback(1);
        feedback.setIdUtente(10);
        feedback.setIdEvento(20);
        feedback.setVoto(5);
        feedback.setNote("Ottimo");
        return feedback;
    }
}
