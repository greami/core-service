package org.core.domain.port.out;

import org.core.domain.model.EventoRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventoRepositoryPortOut
{
    Optional<EventoRecord> findById(Integer id);
    List<EventoRecord> findByNomeLike(String nomeEvento);
    Optional<EventoRecord> findByNomeAndDataAndNumeroPostiAndDurata(String nome, LocalDateTime data, Integer numeroPosti, Integer durata);
    EventoRecord save(EventoRecord evento);
    List<EventoRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine);
    List<EventoRecord> findAll();
    Boolean deleteById(Integer id);
    Boolean updateById(Integer id, EventoRecord evento);

}
