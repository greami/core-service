package org.core.adapter.repository;

import org.core.adapter.repository.entity.FeedbackOpereEntity;

import java.util.List;

public interface JpaFeedbackOpereRepository extends org.springframework.data.jpa.repository.JpaRepository<FeedbackOpereEntity, Integer> {
    List<FeedbackOpereEntity> findByIdOpera(Integer idOpera);
    List<FeedbackOpereEntity> findByIdUtente(Integer idUtente);
    java.util.Optional<FeedbackOpereEntity> findByIdUtenteAndIdOpera(Integer idUtente, Integer idOpera);
}
