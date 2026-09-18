package org.core.domain.port.in;

import org.core.domain.model.FeedbackOpereRecord;

import java.util.List;
import java.util.Optional;

public interface FeedbackOpereUseCasePortIn {
    Optional<FeedbackOpereRecord> findById(Integer id);
    Optional<FeedbackOpereRecord> findByUtenteAndOpera(Integer idUtente, Integer idOpera);
    FeedbackOpereRecord save(FeedbackOpereRecord feedbackOpere);
    List<FeedbackOpereRecord> findAll();
    Boolean deleteById(Integer id);
    Boolean updateById(Integer id, Integer voto, String note);
    List<FeedbackOpereRecord> findByOpera(Integer idOpera);
    List<FeedbackOpereRecord> findByUtente(Integer idUtente);
}
