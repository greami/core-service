package org.core.adapter.port.out;

import org.core.adapter.mapper.FeedbackOpereMapper;
import org.core.adapter.repository.JpaFeedbackOpereRepository;
import org.core.adapter.repository.entity.FeedbackOpereEntity;
import org.core.domain.model.FeedbackOpereRecord;
import org.core.domain.port.out.FeedbackOpereRepositoryPortOut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeedbackOpereRepositoryPortOutImpl implements FeedbackOpereRepositoryPortOut {

    private final JpaFeedbackOpereRepository jpaFeedbackOpereRepository;
    private final FeedbackOpereMapper feedbackOpereMapper;

    public FeedbackOpereRepositoryPortOutImpl(JpaFeedbackOpereRepository jpaFeedbackOpereRepository,
                                              FeedbackOpereMapper feedbackOpereMapper) {
        this.jpaFeedbackOpereRepository = jpaFeedbackOpereRepository;
        this.feedbackOpereMapper = feedbackOpereMapper;
    }

    @Override
    public Optional<FeedbackOpereRecord> findById(Integer id) {
        return jpaFeedbackOpereRepository.findById(id)
                .map(feedbackOpereMapper::toFeedbackOpereRecord);
    }

    @Override
    public Optional<FeedbackOpereRecord> findByUtenteAndOpera(Integer idUtente, Integer idOpera) {
        return jpaFeedbackOpereRepository.findByIdUtenteAndIdOpera(idUtente, idOpera)
                .map(feedbackOpereMapper::toFeedbackOpereRecord);
    }

    @Override
    public FeedbackOpereRecord save(FeedbackOpereRecord feedbackOpere) {
        try {
            FeedbackOpereEntity saved = jpaFeedbackOpereRepository.save(feedbackOpereMapper.toFeedbackOpereEntity(feedbackOpere));
            return feedbackOpereMapper.toFeedbackOpereRecord(saved);
        } catch (DataIntegrityViolationException e) {
            return findByUtenteAndOpera(feedbackOpere.idUtente(), feedbackOpere.idOpera())
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public List<FeedbackOpereRecord> findAll() {
        return jpaFeedbackOpereRepository.findAll().stream()
                .map(feedbackOpereMapper::toFeedbackOpereRecord)
                .toList();
    }

    @Override
    public Boolean deleteById(Integer id) {
        if (!jpaFeedbackOpereRepository.existsById(id)) {
            return false;
        }
        jpaFeedbackOpereRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean updateById(Integer id, Integer voto, String note) {
        return jpaFeedbackOpereRepository.findById(id)
                .map(existing -> {
                    FeedbackOpereEntity updated = new FeedbackOpereEntity(
                            existing.getId(),
                            existing.getIdUtente(),
                            existing.getIdOpera(),
                            voto,
                            note
                    );
                    return jpaFeedbackOpereRepository.save(updated) != null;
                })
                .orElse(false);
    }

    @Override
    public List<FeedbackOpereRecord> findByOpera(Integer idOpera) {
        return jpaFeedbackOpereRepository.findByIdOpera(idOpera).stream()
                .map(feedbackOpereMapper::toFeedbackOpereRecord)
                .toList();
    }

    @Override
    public List<FeedbackOpereRecord> findByUtente(Integer idUtente) {
        return jpaFeedbackOpereRepository.findByIdUtente(idUtente).stream()
                .map(feedbackOpereMapper::toFeedbackOpereRecord)
                .toList();
    }
}
