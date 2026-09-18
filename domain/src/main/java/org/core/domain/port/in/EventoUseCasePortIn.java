package org.core.domain.port.in;

import org.core.domain.model.EventoRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventoUseCasePortIn
{
    List<EventoRecord> findAll();
    EventoRecord save(EventoRecord eventoRecord);
    List<EventoRecord> getByNomeLike(String nomeEvento);
    Optional<EventoRecord> findByNomeAndDataAndNumeroPostiAndDurata(String nome, LocalDateTime data, Integer numeroPosti, Integer durata);
    List<EventoRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine);
    Boolean deleteById(Integer idEvento);
    Boolean updateById(Integer eventoId, EventoRecord evento);
    EventoRecord getById(Integer eventoId);
}
