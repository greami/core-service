package org.core.adapter.port.out;

import org.core.adapter.mapper.EventoMapper;
import org.core.adapter.repository.JpaEventoRepository;
import org.core.adapter.repository.entity.EventoEntity;
import org.core.domain.model.EventoRecord;
import org.core.domain.port.out.EventoRepositoryPortOut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EventoRepositoryPortOutImpl implements EventoRepositoryPortOut {
    private final JpaEventoRepository jpaEventoRepository;
    private final EventoMapper eventoMapper;

    public EventoRepositoryPortOutImpl(JpaEventoRepository jpaEventoRepository, EventoMapper eventoMapper) {
        this.jpaEventoRepository = jpaEventoRepository;
        this.eventoMapper = eventoMapper;
    }

    @Override
    public Optional<EventoRecord> findById(Integer id) {
        return jpaEventoRepository.findById(id)
                .map(entity -> new EventoRecord(
                        entity.getId(),
                        entity.getNome(),
                        entity.getNumeroPosti(),
                        entity.getData(),
                        entity.getDurata()
                ));
    }

    @Override
    public List<EventoRecord> findByNomeLike(String nomeEvento) {
        return jpaEventoRepository.findByNomeContainingIgnoreCase(nomeEvento).stream()
                .map(entity -> new EventoRecord(
                        entity.getId(),
                        entity.getNome(),
                        entity.getNumeroPosti(),
                        entity.getData(),
                        entity.getDurata()
                ))
                .toList();
    }

    @Override
    public Optional<EventoRecord> findByNomeAndDataAndNumeroPostiAndDurata(String nome, LocalDateTime data, Integer numeroPosti, Integer durata) {
        return jpaEventoRepository.findByNomeAndDataAndNumeroPostiAndDurata(nome, data, numeroPosti, durata)
               .map(entity -> new EventoRecord(
                       entity.getId(),
                       entity.getNome(),
                       entity.getNumeroPosti(),
                       entity.getData(),
                       entity.getDurata()
               ));
    }

    @Override
    public EventoRecord save(EventoRecord evento) {
        try {
           EventoEntity eventoEntity = jpaEventoRepository.save(eventoMapper.toEventoEntity(evento));
           return new EventoRecord(
                   eventoEntity.getId(),
                   eventoEntity.getNome(),
                   eventoEntity.getNumeroPosti(),
                   eventoEntity.getData(),
                   eventoEntity.getDurata());
        } catch (DataIntegrityViolationException e) {
           return findByNomeAndDataAndNumeroPostiAndDurata(evento.nome(), evento.data(), evento.numeroPosti(), evento.durata())
                   .orElseThrow(() -> e);
        }
    }

    @Override
    public List<EventoRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine) {
        return jpaEventoRepository.findByDataBetween(dataInizio, dataFine).stream()
                .map(entity -> new EventoRecord(
                        entity.getId(),
                        entity.getNome(),
                        entity.getNumeroPosti(),
                        entity.getData(),
                        entity.getDurata()
                ))
                .toList();
    }

    @Override
    public List<EventoRecord> findAll() {
        return jpaEventoRepository.findAll().stream()
                .map(entity -> new EventoRecord(
                        entity.getId(),
                        entity.getNome(),
                        entity.getNumeroPosti(),
                        entity.getData(),
                        entity.getDurata()
                ))
                .toList();
    }

    @Override
    public Boolean deleteById(Integer id) {
        if (!jpaEventoRepository.existsById(id)) {
            return false;
        }
        jpaEventoRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean updateById(Integer id, EventoRecord evento) {
        return jpaEventoRepository.findById(id)
                .map(existing -> {
                    EventoEntity updated = new EventoEntity(
                            existing.getId(),
                            evento.nome(),
                            evento.numeroPosti(),
                            evento.data(),
                            evento.durata()
                    );
                    return jpaEventoRepository.save(updated) != null;
                })
                .orElse(false);
    }
}
