package org.core.domain.port.in;

import org.core.domain.model.PrenotazioneRecord;
import org.core.domain.model.PrenotazioniDisponibiliEventoRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrenotazioneUseCasePortIn
{
    Optional<PrenotazioneRecord> findById(Integer id);
    Optional<PrenotazioneRecord> findByUtenteAndEvento(Integer idUtente, Integer idEvento);
    PrenotazioneRecord save(PrenotazioneRecord prenotazione);
    List<PrenotazioneRecord> findAll();
    List<PrenotazioneRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine);
    Boolean deleteById(Integer id);
    Boolean updateById(Integer id, Integer numeroPosti);
    List<PrenotazioneRecord> findByEvento(Integer id);
    List<PrenotazioneRecord> findByUtente(Integer id);
    Optional<PrenotazioniDisponibiliEventoRecord> getPostiDisponibiliByIdEvento(Integer idEvento);

}
