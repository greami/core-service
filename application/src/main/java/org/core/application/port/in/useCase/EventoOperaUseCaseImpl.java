package org.core.application.port.in.useCase;

import lombok.RequiredArgsConstructor;
import org.core.domain.model.EventoOperaRecord;
import org.core.domain.port.in.EventoOperaUseCasePortIn;
import org.core.domain.port.out.EventoOperaRepositoryPortOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoOperaUseCaseImpl implements EventoOperaUseCasePortIn {

    @Autowired
    private EventoOperaRepositoryPortOut eventoOperaRepository;

    @Override
    public Optional<EventoOperaRecord> findById(Integer idEvento, Integer idOpera) {
        return eventoOperaRepository.findById(idEvento, idOpera);
    }

    @Override
    public EventoOperaRecord save(EventoOperaRecord eventoOpera) {
        Optional<EventoOperaRecord> existing = eventoOperaRepository.findById(eventoOpera.idEvento(), eventoOpera.idOpera());
        if (existing.isPresent()) {
            return existing.get();
        }
        return eventoOperaRepository.save(eventoOpera);
    }

    @Override
    public List<EventoOperaRecord> findAll() {
        return eventoOperaRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer idEvento, Integer idOpera) {
        return eventoOperaRepository.deleteById(idEvento, idOpera);
    }

    @Override
    public Boolean updateById(Integer idEvento, Integer idOpera, Integer idOperaNuova) {
        return eventoOperaRepository.updateById(idEvento, idOpera, idOperaNuova);
    }

    @Override
    public List<EventoOperaRecord> findByEvento(Integer idEvento) {
        return eventoOperaRepository.findByEvento(idEvento);
    }

    @Override
    public List<EventoOperaRecord> findByOpera(Integer idOpera) {
        return eventoOperaRepository.findByOpera(idOpera);
    }
}
