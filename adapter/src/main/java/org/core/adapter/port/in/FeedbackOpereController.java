package org.core.adapter.port.in;

import org.core.adapter.mapper.FeedbackOpereMapper;
import org.core.domain.model.FeedbackOpereRecord;
import org.core.domain.port.in.FeedbackOpereUseCasePortIn;
import org.openapitools.api.FeedbackOpereApi;
import org.openapitools.model.CreaFeedbackOpereRequest;
import org.openapitools.model.FeedbackOpere;
import org.openapitools.model.ModificaFeedbackOpereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class FeedbackOpereController implements FeedbackOpereApi {

    @Autowired
    FeedbackOpereUseCasePortIn feedbackOpereUseCasePortIn;

    @Autowired
    FeedbackOpereMapper feedbackOpereMapper;

    @Override
    public ResponseEntity<Void> cancellaFeedbackOpera(Integer idFeedback) {
        return feedbackOpereUseCasePortIn.deleteById(idFeedback)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<FeedbackOpere> cercaFeedbackOperaById(Integer idFeedback) {
        return feedbackOpereUseCasePortIn.findById(idFeedback)
                .map(record -> ResponseEntity.ok(feedbackOpereMapper.toFeedbackOpere(record)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<FeedbackOpere>> cercaFeedbackOperaByIdUtente(Integer idUtente) {
        List<FeedbackOpere> feedbacks = feedbackOpereUseCasePortIn.findByUtente(idUtente).stream()
                .map(feedbackOpereMapper::toFeedbackOpere)
                .flatMap(feedback -> feedback != null ? List.of(feedback).stream() : List.<FeedbackOpere>of().stream())
                .toList();
        return ResponseEntity.ok(feedbacks);
    }

    @Override
    public ResponseEntity<List<FeedbackOpere>> cercaFeedbackOperePerOpera(Integer idOpera) {
        List<FeedbackOpere> feedbacks = feedbackOpereUseCasePortIn.findByOpera(idOpera).stream()
                .map(feedbackOpereMapper::toFeedbackOpere)
                .flatMap(feedback -> feedback != null ? List.of(feedback).stream() : List.<FeedbackOpere>of().stream())
                .toList();
        return ResponseEntity.ok(feedbacks);
    }

    @Override
    public ResponseEntity<FeedbackOpere> creaFeedbackOpera(CreaFeedbackOpereRequest creaFeedbackOpereRequest) {
        FeedbackOpereRecord saved = feedbackOpereUseCasePortIn.save(
                new FeedbackOpereRecord(
                        null,
                        creaFeedbackOpereRequest.getIdUtente(),
                        creaFeedbackOpereRequest.getIdOpera(),
                        creaFeedbackOpereRequest.getVoto(),
                        creaFeedbackOpereRequest.getNote()
                )
        );
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean duplicate = Objects.equals(saved.idUtente(), creaFeedbackOpereRequest.getIdUtente())
                && Objects.equals(saved.idOpera(), creaFeedbackOpereRequest.getIdOpera())
                && Objects.equals(saved.voto(), creaFeedbackOpereRequest.getVoto())
                && Objects.equals(saved.note(), creaFeedbackOpereRequest.getNote());

        return duplicate
                ? ResponseEntity.ok(feedbackOpereMapper.toFeedbackOpere(saved))
                : ResponseEntity.status(201).body(feedbackOpereMapper.toFeedbackOpere(saved));
    }

    @Override
    public ResponseEntity<Void> modificaFeedbackOpera(Integer idFeedback, ModificaFeedbackOpereRequest modificaFeedbackOpereRequest) {
        return feedbackOpereUseCasePortIn.updateById(
                idFeedback,
                modificaFeedbackOpereRequest.getVoto(),
                modificaFeedbackOpereRequest.getNote()
        ) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
