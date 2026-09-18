package org.core.adapter.repository;

import org.core.adapter.repository.entity.EventoOperaEntity;
import org.core.adapter.repository.entity.EventoOperaId;

import java.util.List;

public interface JpaEventoOperaRepository extends org.springframework.data.jpa.repository.JpaRepository<EventoOperaEntity, EventoOperaId> {
    List<EventoOperaEntity> findByIdEvento(Integer idEvento);
    List<EventoOperaEntity> findByIdOpera(Integer idOpera);
}
