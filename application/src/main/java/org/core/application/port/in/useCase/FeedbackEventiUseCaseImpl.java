package org.core.application.port.in.useCase;

import lombok.RequiredArgsConstructor;
import org.core.domain.model.FeedbackEventiRecord;
import org.core.domain.port.in.FeedbackEventiUseCasePortIn;
import org.core.domain.port.out.FeedbackEventiRepositoryPortOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackEventiUseCaseImpl implements FeedbackEventiUseCasePortIn {

    @Autowired
    private FeedbackEventiRepositoryPortOut feedbackEventiRepository;

    @Override
    public Optional<FeedbackEventiRecord> findById(Integer id) {
        return feedbackEventiRepository.findById(id);
    }

    @Override
    public Optional<FeedbackEventiRecord> findByUtenteAndEvento(Integer idUtente, Integer idEvento) {
        return feedbackEventiRepository.findByUtenteAndEvento(idUtente, idEvento);
    }

    @Override
    public FeedbackEventiRecord save(FeedbackEventiRecord feedbackEventi) {
        Optional<FeedbackEventiRecord> existing = feedbackEventiRepository.findByUtenteAndEvento(
                feedbackEventi.idUtente(),
                feedbackEventi.idEvento()
        );
        if (existing.isPresent()) {
            return existing.get();
        }
        return feedbackEventiRepository.save(feedbackEventi);
    }

    @Override
    public List<FeedbackEventiRecord> findAll() {
        return feedbackEventiRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer id) {
        return feedbackEventiRepository.deleteById(id);
    }

    @Override
    public Boolean updateById(Integer id, Integer voto, String note) {
        return feedbackEventiRepository.updateById(id, voto, note);
    }

    @Override
    public List<FeedbackEventiRecord> findByEvento(Integer idEvento) {
        return feedbackEventiRepository.findByEvento(idEvento);
    }

    @Override
    public List<FeedbackEventiRecord> findByUtente(Integer idUtente) {
        return feedbackEventiRepository.findByUtente(idUtente);
    }
}
