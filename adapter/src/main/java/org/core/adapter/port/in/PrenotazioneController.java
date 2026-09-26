package org.core.adapter.port.in;

import org.core.domain.port.in.PrenotazioneUseCasePortIn;
import org.openapitools.api.PrenotazioniApi;
import org.openapitools.model.CreaPrenotazioneRequest;
import org.openapitools.model.ModificaPrenotazioneRequest;
import org.openapitools.model.Prenotazione;
import org.openapitools.model.PrenotazioniDisponibiliEvento;
import org.core.adapter.mapper.PrenotazioneMapper;
import org.core.domain.model.PrenotazioneRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class PrenotazioneController  implements PrenotazioniApi
{
   // @Autowired
    //private PrenotazioneRepositoryPortOut prenotazioneRepositoryPortOut;
    //utilizzare lo usecase!

    @Autowired
    PrenotazioneUseCasePortIn prenotazioneUseCasePortIn;

    @Autowired
    private PrenotazioneMapper prenotazioneMapper;

    @Override
    public ResponseEntity<Void> cancellaPrenotazione(Integer idPrenotazione) {
        //return prenotazioneRepositoryPortOut.deleteById(idPrenotazione) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        return prenotazioneUseCasePortIn.deleteById(idPrenotazione) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Prenotazione> cercaPrenotazioneById(Integer idPrenotazione) {
        //return prenotazioneRepositoryPortOut.findById(idPrenotazione)
        //        .map(responseEntity -> ResponseEntity.ok(prenotazioneMapper.toPrenotazione(responseEntity)))
        //        .orElse(ResponseEntity.notFound().build());

        return prenotazioneUseCasePortIn.findById(idPrenotazione)
                .map(prenotazioneRecord -> ResponseEntity.ok(prenotazioneMapper.toPrenotazione(prenotazioneRecord)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Prenotazione>> prenotazioniByData(LocalDateTime dataInizio, LocalDateTime dataFine) {
        List<Prenotazione> prenotazioni = prenotazioneUseCasePortIn.getByDataFilter(dataInizio, dataFine).stream()
                .map(prenotazioneMapper::toPrenotazione)
                .toList();
        return ResponseEntity.ok(prenotazioni);
    }

    @Override
    public ResponseEntity<List<Prenotazione>> cercaPrenotazioniPerEvento(Integer idEvento) {

        //var listByIdEvento = prenotazioneRepositoryPortOut.findByEvento(idEvento).stream()
        //    .map(prenotazioneRecord ->  {
        //      return  prenotazioneMapper.toPrenotazione(prenotazioneRecord);
        //    }).toList();
        //
        //return listByIdEvento.isEmpty()? ResponseEntity.notFound().build(): ResponseEntity.ok(listByIdEvento);


        /*return prenotazioneUseCasePortIn.findByEvento(idEvento).stream()
                .map(prenotazioneRecord -> prenotazioneMapper.toPrenotazione(prenotazioneRecord))
                .toList()
                .isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(
                        prenotazioneUseCasePortIn.findByEvento(idEvento).stream()
                                .map(prenotazioneRecord -> prenotazioneMapper.toPrenotazione(prenotazioneRecord))
                                .toList());
        */
        List<Prenotazione> prenotazioniEvento = prenotazioneUseCasePortIn.findByEvento(idEvento).stream()
                .map(prenotazioneRecord -> prenotazioneMapper.toPrenotazione(prenotazioneRecord))
                .flatMap(prenotazione -> prenotazione != null ? List.of(prenotazione).stream() : List.<Prenotazione>of().stream())
                .toList();

        return prenotazioniEvento.isEmpty() ? ResponseEntity.ok(new ArrayList<>()) : ResponseEntity.ok(prenotazioniEvento);
    }

    @Override
    public ResponseEntity<Prenotazione> creaPrenotazione(CreaPrenotazioneRequest creaPrenotazioneRequest) {
        var disponibilita = prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(creaPrenotazioneRequest.getIdEvento());
        if (disponibilita.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (creaPrenotazioneRequest.getNumeroPartecipanti() > disponibilita.get().numeroPostiDisponibili()) {
            return ResponseEntity.badRequest().build();
        }

        PrenotazioneRecord prenotazioneRecord = prenotazioneUseCasePortIn.save(
                new PrenotazioneRecord(null,
                        creaPrenotazioneRequest.getIdUtente(),
                        creaPrenotazioneRequest.getIdEvento(),
                        creaPrenotazioneRequest.getNumeroPartecipanti(),
                        LocalDateTime.now(),
                        LocalDateTime.now()));
        if (prenotazioneRecord == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean duplicate = Objects.equals(prenotazioneRecord.idUtente(), creaPrenotazioneRequest.getIdUtente())
                && Objects.equals(prenotazioneRecord.idEvento(), creaPrenotazioneRequest.getIdEvento())
                && Objects.equals(prenotazioneRecord.numeroPosti(), creaPrenotazioneRequest.getNumeroPartecipanti());

        return duplicate
                ? ResponseEntity.ok(prenotazioneMapper.toPrenotazione(prenotazioneRecord))
                : ResponseEntity.status(201).body(prenotazioneMapper.toPrenotazione(prenotazioneRecord));
   }

    @Override
    public ResponseEntity<Void> modificaPrenotazione(Integer idPrenotazione, ModificaPrenotazioneRequest modificaPrenotazioneRequest) {
        var prenotazioneEsistente = prenotazioneUseCasePortIn.findById(idPrenotazione);
        if (prenotazioneEsistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var disponibilita = prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(prenotazioneEsistente.get().idEvento());
        if (disponibilita.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int postiMassimiPrenotabili = disponibilita.get().numeroPostiDisponibili() + prenotazioneEsistente.get().numeroPosti();
        if (modificaPrenotazioneRequest.getNumeroPartecipanti() > postiMassimiPrenotabili) {
            return ResponseEntity.badRequest().build();
        }

        return prenotazioneUseCasePortIn.updateById(idPrenotazione, modificaPrenotazioneRequest.getNumeroPartecipanti())
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<List<Prenotazione>> cercaPrenotazioneByIdUtente(Integer idUtente) {
        List<Prenotazione> prenotazioniUtente =prenotazioneUseCasePortIn.findByUtente(idUtente).stream()
                .map(prenotazioneRecord -> ResponseEntity.ok(prenotazioneMapper.toPrenotazione(prenotazioneRecord)))
                .flatMap(responseEntity -> responseEntity.getBody() != null ? List.of(responseEntity.getBody()).stream() : List.<Prenotazione>of().stream())
                .toList();

        return ResponseEntity.ok(prenotazioniUtente);
    }

    @Override
    public ResponseEntity<PrenotazioniDisponibiliEvento> cercaPrenotazioniDisponibiliPerEvento(Integer idEvento) {
        return prenotazioneUseCasePortIn.getPostiDisponibiliByIdEvento(idEvento)
                .map(disponibilita -> {
                    PrenotazioniDisponibiliEvento response = new PrenotazioniDisponibiliEvento();
                    response.setIdEvento(disponibilita.idEvento());
                    response.setNumeroPostiTotali(disponibilita.numeroPostiTotali());
                    response.setNumeroPostiDispobili(disponibilita.numeroPostiDisponibili());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
