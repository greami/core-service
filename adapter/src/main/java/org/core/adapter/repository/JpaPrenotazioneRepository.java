package org.core.adapter.repository;

import org.core.adapter.repository.entity.PrenotazioneEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaPrenotazioneRepository extends org.springframework.data.jpa.repository.JpaRepository<PrenotazioneEntity, Integer> {

    List<PrenotazioneEntity> findByIdEvento(Integer idEvento);
    List<PrenotazioneEntity> findByIdUtente(Integer idEvento);
    List<PrenotazioneEntity> findByDataPrenotazioneBetween(LocalDateTime dataInizio, LocalDateTime dataFine);
    java.util.Optional<PrenotazioneEntity> findByIdUtenteAndIdEvento(Integer idUtente, Integer idEvento);
    //Boolean salva(PrenotazioneEntity prenotazione);
}
