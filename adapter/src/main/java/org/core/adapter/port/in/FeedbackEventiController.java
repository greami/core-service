package org.core.adapter.port.in;

import org.core.adapter.mapper.FeedbackEventiMapper;
import org.core.domain.model.FeedbackEventiRecord;
import org.core.domain.port.in.FeedbackEventiUseCasePortIn;
import org.openapitools.api.FeedbackEventiApi;
import org.openapitools.model.CreaFeedbackEventiRequest;
import org.openapitools.model.FeedbackEventi;
import org.openapitools.model.ModificaFeedbackEventiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class FeedbackEventiController implements FeedbackEventiApi {

    @Autowired
    FeedbackEventiUseCasePortIn feedbackEventiUseCasePortIn;

    @Autowired
    FeedbackEventiMapper feedbackEventiMapper;

    @Override
    public ResponseEntity<Void> cancellaFeedbackEvento(Integer idFeedback) {
        return feedbackEventiUseCasePortIn.deleteById(idFeedback)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<FeedbackEventi> cercaFeedbackEventoById(Integer idFeedback) {
        return feedbackEventiUseCasePortIn.findById(idFeedback)
                .map(record -> ResponseEntity.ok(feedbackEventiMapper.toFeedbackEventi(record)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<FeedbackEventi>> cercaFeedbackEventoByIdUtente(Integer idUtente) {
        List<FeedbackEventi> feedbacks = feedbackEventiUseCasePortIn.findByUtente(idUtente).stream()
                .map(feedbackEventiMapper::toFeedbackEventi)
                .flatMap(feedback -> feedback != null ? List.of(feedback).stream() : List.<FeedbackEventi>of().stream())
                .toList();
        return ResponseEntity.ok(feedbacks);
    }

    @Override
    public ResponseEntity<List<FeedbackEventi>> cercaFeedbackEventiPerEvento(Integer idEvento) {
        List<FeedbackEventi> feedbacks = feedbackEventiUseCasePortIn.findByEvento(idEvento).stream()
                .map(feedbackEventiMapper::toFeedbackEventi)
                .flatMap(feedback -> feedback != null ? List.of(feedback).stream() : List.<FeedbackEventi>of().stream())
                .toList();
        return feedbacks.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(feedbacks);
    }

    @Override
    public ResponseEntity<FeedbackEventi> creaFeedbackEvento(CreaFeedbackEventiRequest creaFeedbackEventiRequest) {
        FeedbackEventiRecord saved = feedbackEventiUseCasePortIn.save(
                new FeedbackEventiRecord(
                        null,
                        creaFeedbackEventiRequest.getIdUtente(),
                        creaFeedbackEventiRequest.getIdEvento(),
                        creaFeedbackEventiRequest.getVoto(),
                        creaFeedbackEventiRequest.getNote()
                )
        );
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean duplicate = Objects.equals(saved.idUtente(), creaFeedbackEventiRequest.getIdUtente())
                && Objects.equals(saved.idEvento(), creaFeedbackEventiRequest.getIdEvento())
                && Objects.equals(saved.voto(), creaFeedbackEventiRequest.getVoto())
                && Objects.equals(saved.note(), creaFeedbackEventiRequest.getNote());

        return duplicate
                ? ResponseEntity.ok(feedbackEventiMapper.toFeedbackEventi(saved))
                : ResponseEntity.status(201).body(feedbackEventiMapper.toFeedbackEventi(saved));
    }

    @Override
    public ResponseEntity<Void> modificaFeedbackEvento(Integer idFeedback, ModificaFeedbackEventiRequest modificaFeedbackEventiRequest) {
        return feedbackEventiUseCasePortIn.updateById(
                idFeedback,
                modificaFeedbackEventiRequest.getVoto(),
                modificaFeedbackEventiRequest.getNote()
        ) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
