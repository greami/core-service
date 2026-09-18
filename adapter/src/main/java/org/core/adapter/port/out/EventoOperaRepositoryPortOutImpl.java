package org.core.adapter.port.out;

import org.core.adapter.mapper.EventoOperaMapper;
import org.core.adapter.repository.JpaEventoOperaRepository;
import org.core.adapter.repository.entity.EventoOperaEntity;
import org.core.adapter.repository.entity.EventoOperaId;
import org.core.domain.model.EventoOperaRecord;
import org.core.domain.port.out.EventoOperaRepositoryPortOut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventoOperaRepositoryPortOutImpl implements EventoOperaRepositoryPortOut {

    private final JpaEventoOperaRepository jpaEventoOperaRepository;
    private final EventoOperaMapper eventoOperaMapper;

    public EventoOperaRepositoryPortOutImpl(JpaEventoOperaRepository jpaEventoOperaRepository,
                                            EventoOperaMapper eventoOperaMapper) {
        this.jpaEventoOperaRepository = jpaEventoOperaRepository;
        this.eventoOperaMapper = eventoOperaMapper;
    }

    @Override
    public java.util.Optional<EventoOperaRecord> findById(Integer idEvento, Integer idOpera) {
        return jpaEventoOperaRepository.findById(new EventoOperaId(idEvento, idOpera))
                .map(eventoOperaMapper::toEventoOperaRecord);
    }

    @Override
    public EventoOperaRecord save(EventoOperaRecord eventoOpera) {
        try {
            EventoOperaEntity saved = jpaEventoOperaRepository.save(eventoOperaMapper.toEventoOperaEntity(eventoOpera));
            return eventoOperaMapper.toEventoOperaRecord(saved);
        } catch (DataIntegrityViolationException e) {
            return findById(eventoOpera.idEvento(), eventoOpera.idOpera())
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public List<EventoOperaRecord> findAll() {
        return jpaEventoOperaRepository.findAll().stream()
                .map(eventoOperaMapper::toEventoOperaRecord)
                .toList();
    }

    @Override
    public Boolean deleteById(Integer idEvento, Integer idOpera) {
        EventoOperaId key = new EventoOperaId(idEvento, idOpera);
        if (!jpaEventoOperaRepository.existsById(key)) {
            return false;
        }
        jpaEventoOperaRepository.deleteById(key);
        return true;
    }

    @Override
    public Boolean updateById(Integer idEvento, Integer idOpera, Integer idOperaNuova) {
        EventoOperaId keyAttuale = new EventoOperaId(idEvento, idOpera);
        if (!jpaEventoOperaRepository.existsById(keyAttuale)) {
            return false;
        }
        if (idOpera.equals(idOperaNuova)) {
            return true;
        }

        EventoOperaId nuovaChiave = new EventoOperaId(idEvento, idOperaNuova);
        if (jpaEventoOperaRepository.existsById(nuovaChiave)) {
            return false;
        }

        EventoOperaEntity updated = new EventoOperaEntity(idEvento, idOperaNuova);
        jpaEventoOperaRepository.save(updated);
        jpaEventoOperaRepository.deleteById(keyAttuale);
        return true;
    }

    @Override
    public List<EventoOperaRecord> findByEvento(Integer idEvento) {
        return jpaEventoOperaRepository.findByIdEvento(idEvento).stream()
                .map(eventoOperaMapper::toEventoOperaRecord)
                .toList();
    }

    @Override
    public List<EventoOperaRecord> findByOpera(Integer idOpera) {
        return jpaEventoOperaRepository.findByIdOpera(idOpera).stream()
                .map(eventoOperaMapper::toEventoOperaRecord)
                .toList();
    }
}
