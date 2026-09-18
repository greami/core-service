package org.core.adapter.port.out;

import org.core.adapter.mapper.FeedbackEventiMapper;
import org.core.adapter.repository.JpaFeedbackEventiRepository;
import org.core.adapter.repository.entity.FeedbackEventiEntity;
import org.core.domain.model.FeedbackEventiRecord;
import org.core.domain.port.out.FeedbackEventiRepositoryPortOut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeedbackEventiRepositoryPortOutImpl implements FeedbackEventiRepositoryPortOut {

    private final JpaFeedbackEventiRepository jpaFeedbackEventiRepository;
    private final FeedbackEventiMapper feedbackEventiMapper;

    public FeedbackEventiRepositoryPortOutImpl(JpaFeedbackEventiRepository jpaFeedbackEventiRepository,
                                                FeedbackEventiMapper feedbackEventiMapper) {
        this.jpaFeedbackEventiRepository = jpaFeedbackEventiRepository;
        this.feedbackEventiMapper = feedbackEventiMapper;
    }

    @Override
    public Optional<FeedbackEventiRecord> findById(Integer id) {
        return jpaFeedbackEventiRepository.findById(id)
                .map(feedbackEventiMapper::toFeedbackEventiRecord);
    }

    @Override
    public Optional<FeedbackEventiRecord> findByUtenteAndEvento(Integer idUtente, Integer idEvento) {
        return jpaFeedbackEventiRepository.findByIdUtenteAndIdEvento(idUtente, idEvento)
                .map(feedbackEventiMapper::toFeedbackEventiRecord);
    }

    @Override
    public FeedbackEventiRecord save(FeedbackEventiRecord feedbackEventi) {
        try {
            FeedbackEventiEntity saved = jpaFeedbackEventiRepository.save(feedbackEventiMapper.toFeedbackEventiEntity(feedbackEventi));
            return feedbackEventiMapper.toFeedbackEventiRecord(saved);
        } catch (DataIntegrityViolationException e) {
            return findByUtenteAndEvento(feedbackEventi.idUtente(), feedbackEventi.idEvento())
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public List<FeedbackEventiRecord> findAll() {
        return jpaFeedbackEventiRepository.findAll().stream()
                .map(feedbackEventiMapper::toFeedbackEventiRecord)
                .toList();
    }

    @Override
    public Boolean deleteById(Integer id) {
        if (!jpaFeedbackEventiRepository.existsById(id)) {
            return false;
        }
        jpaFeedbackEventiRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean updateById(Integer id, Integer voto, String note) {
        return jpaFeedbackEventiRepository.findById(id)
                .map(existing -> {
                    FeedbackEventiEntity updated = new FeedbackEventiEntity(
                            existing.getId(),
                            existing.getIdUtente(),
                            existing.getIdEvento(),
                            voto,
                            note
                    );
                    return jpaFeedbackEventiRepository.save(updated) != null;
                })
                .orElse(false);
    }

    @Override
    public List<FeedbackEventiRecord> findByEvento(Integer idEvento) {
        return jpaFeedbackEventiRepository.findByIdEvento(idEvento).stream()
                .map(feedbackEventiMapper::toFeedbackEventiRecord)
                .toList();
    }

    @Override
    public List<FeedbackEventiRecord> findByUtente(Integer idUtente) {
        return jpaFeedbackEventiRepository.findByIdUtente(idUtente).stream()
                .map(feedbackEventiMapper::toFeedbackEventiRecord)
                .toList();
    }
}
