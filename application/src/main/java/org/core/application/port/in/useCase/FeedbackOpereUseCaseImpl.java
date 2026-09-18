package org.core.application.port.in.useCase;

import lombok.RequiredArgsConstructor;
import org.core.domain.model.FeedbackOpereRecord;
import org.core.domain.port.in.FeedbackOpereUseCasePortIn;
import org.core.domain.port.out.FeedbackOpereRepositoryPortOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackOpereUseCaseImpl implements FeedbackOpereUseCasePortIn {

    @Autowired
    private FeedbackOpereRepositoryPortOut feedbackOpereRepository;

    @Override
    public Optional<FeedbackOpereRecord> findById(Integer id) {
        return feedbackOpereRepository.findById(id);
    }

    @Override
    public Optional<FeedbackOpereRecord> findByUtenteAndOpera(Integer idUtente, Integer idOpera) {
        return feedbackOpereRepository.findByUtenteAndOpera(idUtente, idOpera);
    }

    @Override
    public FeedbackOpereRecord save(FeedbackOpereRecord feedbackOpere) {
        Optional<FeedbackOpereRecord> existing = feedbackOpereRepository.findByUtenteAndOpera(
                feedbackOpere.idUtente(),
                feedbackOpere.idOpera()
        );
        if (existing.isPresent()) {
            return existing.get();
        }
        return feedbackOpereRepository.save(feedbackOpere);
    }

    @Override
    public List<FeedbackOpereRecord> findAll() {
        return feedbackOpereRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer id) {
        return feedbackOpereRepository.deleteById(id);
    }

    @Override
    public Boolean updateById(Integer id, Integer voto, String note) {
        return feedbackOpereRepository.updateById(id, voto, note);
    }

    @Override
    public List<FeedbackOpereRecord> findByOpera(Integer idOpera) {
        return feedbackOpereRepository.findByOpera(idOpera);
    }

    @Override
    public List<FeedbackOpereRecord> findByUtente(Integer idUtente) {
        return feedbackOpereRepository.findByUtente(idUtente);
    }
}
