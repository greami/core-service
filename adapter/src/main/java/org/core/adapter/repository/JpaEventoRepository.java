package org.core.adapter.repository;

import org.core.adapter.repository.entity.EventoEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaEventoRepository extends org.springframework.data.jpa.repository.JpaRepository<EventoEntity, Integer> {

    List<EventoEntity> findByNomeContainingIgnoreCase(String nome);
    List<EventoEntity> findByDataBetween(LocalDateTime dataInizio, LocalDateTime dataFine);
    java.util.Optional<EventoEntity> findByNomeAndDataAndNumeroPostiAndDurata(String nome, LocalDateTime data, Integer numeroPosti, Integer durata);
}
