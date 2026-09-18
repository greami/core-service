package org.core.adapter.repository;

import org.core.adapter.repository.entity.FeedbackEventiEntity;

import java.util.List;

public interface JpaFeedbackEventiRepository extends org.springframework.data.jpa.repository.JpaRepository<FeedbackEventiEntity, Integer> {
    List<FeedbackEventiEntity> findByIdEvento(Integer idEvento);
    List<FeedbackEventiEntity> findByIdUtente(Integer idUtente);
    java.util.Optional<FeedbackEventiEntity> findByIdUtenteAndIdEvento(Integer idUtente, Integer idEvento);
}
