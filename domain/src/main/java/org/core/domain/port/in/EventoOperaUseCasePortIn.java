package org.core.domain.port.in;

import org.core.domain.model.EventoOperaRecord;

import java.util.List;
import java.util.Optional;

public interface EventoOperaUseCasePortIn {
    Optional<EventoOperaRecord> findById(Integer idEvento, Integer idOpera);
    EventoOperaRecord save(EventoOperaRecord eventoOpera);
    List<EventoOperaRecord> findAll();
    Boolean deleteById(Integer idEvento, Integer idOpera);
    Boolean updateById(Integer idEvento, Integer idOpera, Integer idOperaNuova);
    List<EventoOperaRecord> findByEvento(Integer idEvento);
    List<EventoOperaRecord> findByOpera(Integer idOpera);
}
