package org.core.domain.port.out;

import org.core.domain.model.FeedbackEventiRecord;

import java.util.List;
import java.util.Optional;

public interface FeedbackEventiRepositoryPortOut {
    Optional<FeedbackEventiRecord> findById(Integer id);
    Optional<FeedbackEventiRecord> findByUtenteAndEvento(Integer idUtente, Integer idEvento);
    FeedbackEventiRecord save(FeedbackEventiRecord feedbackEventi);
    List<FeedbackEventiRecord> findAll();
    Boolean deleteById(Integer id);
    Boolean updateById(Integer id, Integer voto, String note);
    List<FeedbackEventiRecord> findByEvento(Integer idEvento);
    List<FeedbackEventiRecord> findByUtente(Integer idUtente);
}
