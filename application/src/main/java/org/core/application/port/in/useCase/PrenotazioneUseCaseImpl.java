package org.core.application.port.in.useCase;

import lombok.RequiredArgsConstructor;
import org.core.domain.model.EventoRecord;
import org.core.domain.model.PrenotazioneRecord;
import org.core.domain.model.PrenotazioniDisponibiliEventoRecord;
import org.core.domain.port.in.EventoUseCasePortIn;
import org.core.domain.port.in.PrenotazioneUseCasePortIn;
import org.core.domain.port.out.PrenotazioneRepositoryPortOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrenotazioneUseCaseImpl implements PrenotazioneUseCasePortIn
{
    @Autowired
    private PrenotazioneRepositoryPortOut prenotazioneRepository;

    @Autowired
    private EventoUseCasePortIn eventoUseCasePortIn;

    @Override
    public Optional<PrenotazioneRecord> findById(Integer id) {
        return prenotazioneRepository.findById(id);
    }

    @Override
    public Optional<PrenotazioneRecord> findByUtenteAndEvento(Integer idUtente, Integer idEvento) {
        return prenotazioneRepository.findByUtenteAndEvento(idUtente, idEvento);
    }

    @Override
    public PrenotazioneRecord save(PrenotazioneRecord prenotazione) {
        Optional<PrenotazioneRecord> existing = prenotazioneRepository.findByUtenteAndEvento(
                prenotazione.idUtente(),
                prenotazione.idEvento()
        );
        if (existing.isPresent()) {
            return existing.get();
        }
        return prenotazioneRepository.save(prenotazione);
    }

    @Override
    public List<PrenotazioneRecord> findAll() {
        return prenotazioneRepository.findAll();
    }

    @Override
    public List<PrenotazioneRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine) {
        return prenotazioneRepository.getByDataFilter(dataInizio, dataFine);
    }

    @Override
    public Boolean deleteById(Integer id) {
        return prenotazioneRepository.deleteById(id);
    }

    @Override
    public Boolean updateById(Integer id, Integer numeroPosti) {
        return prenotazioneRepository.updateById(id, numeroPosti);
    }
    @Override
    public List<PrenotazioneRecord> findByEvento(Integer id) {
        return prenotazioneRepository.findByEvento(id);
    }

    @Override
    public List<PrenotazioneRecord> findByUtente(Integer id) {
        return prenotazioneRepository.findByUtente(id);
    }

    @Override
    public Optional<PrenotazioniDisponibiliEventoRecord> getPostiDisponibiliByIdEvento(Integer idEvento) {
        EventoRecord evento = eventoUseCasePortIn.getById(idEvento);
        if (evento == null) {
            return Optional.empty();
        }

        int postiOccupati = prenotazioneRepository.findByEvento(idEvento).stream()
                .mapToInt(PrenotazioneRecord::numeroPosti)
                .sum();

        return Optional.of(new PrenotazioniDisponibiliEventoRecord(
                idEvento,
                evento.numeroPosti(),
                evento.numeroPosti() - postiOccupati
        ));
    }
}
