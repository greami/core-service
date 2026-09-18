package org.core.adapter.port.in;

import org.core.adapter.mapper.EventoOperaMapper;
import org.core.domain.model.EventoOperaRecord;
import org.core.domain.port.in.EventoOperaUseCasePortIn;
import org.openapitools.api.EventoOperaApi;
import org.openapitools.model.CreaEventoOperaRequest;
import org.openapitools.model.EventoOpera;
import org.openapitools.model.ModificaEventoOperaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class EventoOperaController implements EventoOperaApi {

    @Autowired
    EventoOperaUseCasePortIn eventoOperaUseCasePortIn;

    @Autowired
    EventoOperaMapper eventoOperaMapper;

    @Override
    public ResponseEntity<Void> cancellaEventoOpera(Integer idEvento, Integer idOpera) {
        return eventoOperaUseCasePortIn.deleteById(idEvento, idOpera)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<EventoOpera> cercaEventoOperaById(Integer idEvento, Integer idOpera) {
        return eventoOperaUseCasePortIn.findById(idEvento, idOpera)
                .map(eventoOperaRecord -> ResponseEntity.ok(eventoOperaMapper.toEventoOpera(eventoOperaRecord)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<EventoOpera>> cercaOperePerEvento(Integer idEvento) {
        List<EventoOpera> eventoOpere = eventoOperaUseCasePortIn.findByEvento(idEvento).stream()
                .map(eventoOperaMapper::toEventoOpera)
                .flatMap(eventoOpera -> eventoOpera != null ? List.of(eventoOpera).stream() : List.<EventoOpera>of().stream())
                .toList();
        return eventoOpere.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(eventoOpere);
    }

    @Override
    public ResponseEntity<List<EventoOpera>> cercaEventiPerOpera(Integer idOpera) {
        List<EventoOpera> operaEventi = eventoOperaUseCasePortIn.findByOpera(idOpera).stream()
                .map(eventoOperaMapper::toEventoOpera)
                .flatMap(eventoOpera -> eventoOpera != null ? List.of(eventoOpera).stream() : List.<EventoOpera>of().stream())
                .toList();
        return operaEventi.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(operaEventi);
    }

    @Override
    public ResponseEntity<EventoOpera> creaEventoOpera(CreaEventoOperaRequest creaEventoOperaRequest) {
        EventoOperaRecord eventoOperaRecord = eventoOperaUseCasePortIn.save(
                new EventoOperaRecord(
                        creaEventoOperaRequest.getIdEvento(),
                        creaEventoOperaRequest.getIdOpera()
                )
        );
        if (eventoOperaRecord == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean duplicate = Objects.equals(eventoOperaRecord.idEvento(), creaEventoOperaRequest.getIdEvento())
                && Objects.equals(eventoOperaRecord.idOpera(), creaEventoOperaRequest.getIdOpera());

        return duplicate
                ? ResponseEntity.ok(eventoOperaMapper.toEventoOpera(eventoOperaRecord))
                : ResponseEntity.status(201).body(eventoOperaMapper.toEventoOpera(eventoOperaRecord));
    }

    @Override
    public ResponseEntity<Void> modificaEventoOpera(Integer idEvento, Integer idOpera, ModificaEventoOperaRequest modificaEventoOperaRequest) {
        return eventoOperaUseCasePortIn.updateById(idEvento, idOpera, modificaEventoOperaRequest.getIdOperaNuova())
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
