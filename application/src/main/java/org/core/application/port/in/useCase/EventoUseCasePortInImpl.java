package org.core.application.port.in.useCase;

import lombok.RequiredArgsConstructor;
import org.core.domain.model.EventoRecord;
import org.core.domain.port.in.EventoUseCasePortIn;
import org.core.domain.port.out.EventoRepositoryPortOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoUseCasePortInImpl implements EventoUseCasePortIn
{
    @Autowired
    private EventoRepositoryPortOut eventoRepository;

    @Override
    public List<EventoRecord> findAll() {
        return eventoRepository.findAll();
    }

    @Override
    public List<EventoRecord> getByNomeLike(String nomeEvento) {
        return eventoRepository.findByNomeLike(nomeEvento);
    }

    @Override
    public Optional<EventoRecord> findByNomeAndDataAndNumeroPostiAndDurata(String nome, LocalDateTime data, Integer numeroPosti, Integer durata) {
        return eventoRepository.findByNomeAndDataAndNumeroPostiAndDurata(nome, data, numeroPosti, durata);
    }

    @Override
    public EventoRecord save(EventoRecord eventoRecord) {
        Optional<EventoRecord> existing = eventoRepository.findByNomeAndDataAndNumeroPostiAndDurata(
                eventoRecord.nome(),
                eventoRecord.data(),
                eventoRecord.numeroPosti(),
                eventoRecord.durata()
        );
        if (existing.isPresent()) {
            return existing.get();
        }
        return eventoRepository.save(eventoRecord);
    }

    @Override
    public List<EventoRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine) {
        return eventoRepository.getByDataFilter(dataInizio, dataFine);
    }

    @Override
    public Boolean deleteById(Integer idEvento) {
        return eventoRepository.deleteById(idEvento);
    }

    @Override
    public Boolean updateById(Integer eventoId, EventoRecord evento) {
        return eventoRepository.updateById(eventoId, evento);
    }

    @Override
    public EventoRecord getById(Integer eventoId) {
        return eventoRepository.findById(eventoId).orElse(null);
    }


}
