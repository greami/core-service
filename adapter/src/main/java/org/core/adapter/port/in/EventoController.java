package org.core.adapter.port.in;

import org.core.adapter.mapper.EventoMapper;
import org.core.domain.model.EventoRecord;
import org.core.domain.port.in.EventoUseCasePortIn;
import org.openapitools.api.EventiApi;
import org.openapitools.model.CreaEventoRequest;
import org.openapitools.model.Evento;
import org.openapitools.model.ModificaEventoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
public class EventoController implements EventiApi {

    @Autowired
    EventoUseCasePortIn eventoUseCasePortIn;

    @Autowired
    EventoMapper eventoMapper;

    @Override
    public ResponseEntity<Void> cancellaEvento(Integer idEvento) {
        return eventoUseCasePortIn.deleteById(idEvento) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Evento> cercaEvento(Integer idEvento) {
        EventoRecord eventoRecord = eventoUseCasePortIn.getById(idEvento);
        return eventoRecord != null ? ResponseEntity.ok(eventoMapper.toEvento(eventoRecord)) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<Evento>> cercaEventoByNome(String nomeEvento) {
        return ResponseEntity.ok(
                eventoUseCasePortIn.getByNomeLike(nomeEvento).stream()
                        .map(eventoMapper::toEvento)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<Evento> creaEvento(CreaEventoRequest creaEventoRequest) {
        EventoRecord evento = eventoUseCasePortIn.save(
                new EventoRecord(
                        null,
                        creaEventoRequest.getNome(),
                        creaEventoRequest.getNumeroPosti(),
                        creaEventoRequest.getData(),
                        creaEventoRequest.getDurata()
                )
        );
        if (evento == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean duplicate = Objects.equals(evento.nome(), creaEventoRequest.getNome())
                && Objects.equals(evento.numeroPosti(), creaEventoRequest.getNumeroPosti())
                && Objects.equals(evento.data(), creaEventoRequest.getData())
                && Objects.equals(evento.durata(), creaEventoRequest.getDurata());

        return duplicate
                ? ResponseEntity.ok(eventoMapper.toEvento(evento))
                : ResponseEntity.status(201).body(eventoMapper.toEvento(evento));
    }

    @Override
    public ResponseEntity<List<Evento>> eventiAll() {
        return ResponseEntity.ok(eventoUseCasePortIn.findAll()
                .stream().map(eventoRecord -> eventoMapper.toEvento(eventoRecord)).toList());
    }

    @Override
    public ResponseEntity<List<Evento>> eventiByData(LocalDateTime dataInizio, LocalDateTime dataFine) {
        return ResponseEntity.ok(eventoUseCasePortIn.getByDataFilter(dataInizio, dataFine)
                .stream().map(eventoRecord -> eventoMapper.toEvento(eventoRecord)).toList());
    }

    @Override
    public ResponseEntity<Evento> modificaEvento(Integer idEvento, ModificaEventoRequest modificaEventoRequest)
    {
        return eventoUseCasePortIn.updateById(idEvento, new EventoRecord(
                modificaEventoRequest.getIdEvento(),
                modificaEventoRequest.getNome(),
                modificaEventoRequest.getNumeroPosti(),
                modificaEventoRequest.getData(),
                modificaEventoRequest.getDurata()
        )) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


}
