package org.core.domain.port.out;

import org.core.domain.model.PrenotazioneRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrenotazioneRepositoryPortOut {
    Optional<PrenotazioneRecord> findById(Integer id);
    Optional<PrenotazioneRecord> findByUtenteAndEvento(Integer idUtente, Integer idEvento);
    PrenotazioneRecord save(PrenotazioneRecord prenotazione);
    List<PrenotazioneRecord> findAll();
    List<PrenotazioneRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine);
    Boolean deleteById(Integer id);
    Boolean updateById(Integer id, Integer numeroPosti);
    List<PrenotazioneRecord> findByEvento(Integer id);
    List<PrenotazioneRecord> findByUtente(Integer id);
}
