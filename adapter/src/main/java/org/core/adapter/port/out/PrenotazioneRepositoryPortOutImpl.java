package org.core.adapter.port.out;

import org.core.adapter.mapper.PrenotazioneMapper;
import org.core.adapter.repository.JpaPrenotazioneRepository;
import org.core.adapter.repository.entity.PrenotazioneEntity;
import org.core.domain.model.PrenotazioneRecord;
import org.core.domain.port.out.PrenotazioneRepositoryPortOut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PrenotazioneRepositoryPortOutImpl implements PrenotazioneRepositoryPortOut {

    private final JpaPrenotazioneRepository jpaPrenotazioneRepository;
    private final PrenotazioneMapper prenotazioneMapper;

    public PrenotazioneRepositoryPortOutImpl(JpaPrenotazioneRepository jpaPrenotazioneRepository,
                                             PrenotazioneMapper prenotazioneMapper) {
        this.jpaPrenotazioneRepository = jpaPrenotazioneRepository;
        this.prenotazioneMapper = prenotazioneMapper;
    }

    @Override
    public Optional<PrenotazioneRecord> findById(Integer id) {
        return jpaPrenotazioneRepository.findById(id)
                .map(entity -> new PrenotazioneRecord(
                        entity.getId(),
                        entity.getIdUtente(),
                        entity.getIdEvento(),
                        entity.getNumeroPosti(),
                        entity.getDataPrenotazione(),
                        entity.getDataModifica()
                ));
    }

    @Override
    public Optional<PrenotazioneRecord> findByUtenteAndEvento(Integer idUtente, Integer idEvento) {
        return jpaPrenotazioneRepository.findByIdUtenteAndIdEvento(idUtente, idEvento)
                .map(entity -> new PrenotazioneRecord(
                        entity.getId(),
                        entity.getIdUtente(),
                        entity.getIdEvento(),
                        entity.getNumeroPosti(),
                        entity.getDataPrenotazione(),
                        entity.getDataModifica()
                ));
    }

    @Override
    public PrenotazioneRecord save(PrenotazioneRecord prenotazione) {
        try {
            return new PrenotazioneRecord(
                    jpaPrenotazioneRepository.save(prenotazioneMapper.toPrenotazioneEntity(prenotazione)).getId(),
                    prenotazione.idUtente(),
                    prenotazione.idEvento(),
                    prenotazione.numeroPosti(),
                    prenotazione.dataPrenotazione(),
                    prenotazione.dataModifica()
            );
        } catch (DataIntegrityViolationException e) {
            return findByUtenteAndEvento(prenotazione.idUtente(), prenotazione.idEvento())
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public List<PrenotazioneRecord> findAll() {
        return jpaPrenotazioneRepository.findAll().stream()
                .map(entity -> new PrenotazioneRecord(
                        entity.getId(),
                        entity.getIdUtente(),
                        entity.getIdEvento(),
                        entity.getNumeroPosti(),
                        entity.getDataPrenotazione(),
                        entity.getDataModifica()
                ))
                .toList();
    }

    @Override
    public List<PrenotazioneRecord> getByDataFilter(LocalDateTime dataInizio, LocalDateTime dataFine) {
        return jpaPrenotazioneRepository.findByDataPrenotazioneBetween(dataInizio, dataFine).stream()
                .map(entity -> new PrenotazioneRecord(
                        entity.getId(),
                        entity.getIdUtente(),
                        entity.getIdEvento(),
                        entity.getNumeroPosti(),
                        entity.getDataPrenotazione(),
                        entity.getDataModifica()
                ))
                .toList();
    }

    @Override
    public Boolean deleteById(Integer id) {
        if (!jpaPrenotazioneRepository.existsById(id)) {
            return false;
        }
        jpaPrenotazioneRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean updateById(Integer id, Integer numeroPosti) {
        return jpaPrenotazioneRepository.findById(id)
                .map(existing -> {
                    PrenotazioneEntity updated = new PrenotazioneEntity(
                            existing.getId(),
                            existing.getIdUtente(),
                            existing.getIdEvento(),
                            numeroPosti,
                            existing.getDataPrenotazione(),
                            java.time.LocalDateTime.now()
                    );
                    return jpaPrenotazioneRepository.save(updated) != null;
                })
                .orElse(false);
    }


    @Override
    public List<PrenotazioneRecord> findByEvento(Integer id) {
        return jpaPrenotazioneRepository.findByIdEvento(id).stream().map(
                entity -> new PrenotazioneRecord(
                        entity.getId(),
                        entity.getIdUtente(),
                        entity.getIdEvento(),
                        entity.getNumeroPosti(),
                        entity.getDataPrenotazione(),
                        entity.getDataModifica()
                )
        ).toList();
    }

    @Override
    public List<PrenotazioneRecord> findByUtente(Integer id) {
        return jpaPrenotazioneRepository.findByIdUtente(id).stream().map(
                entity -> new PrenotazioneRecord(
                        entity.getId(),
                        entity.getIdUtente(),
                        entity.getIdEvento(),
                        entity.getNumeroPosti(),
                        entity.getDataPrenotazione(),
                        entity.getDataModifica()
                )
        ).toList();
    }
}
