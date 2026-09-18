package org.core.adapter.port.in;

import org.core.adapter.mapper.OperaMapper;
import org.core.domain.model.OperaRecord;
import org.core.domain.port.in.OperaUseCasePortIn;
import org.openapitools.api.OpereApi;
import org.openapitools.model.CreaOperaRequest;
import org.openapitools.model.ModificaOperaRequest;
import org.openapitools.model.Opera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class OperaController implements OpereApi {

    @Autowired
    OperaUseCasePortIn operaUseCasePortIn;

    @Autowired
    OperaMapper operaMapper;

    @Override
    public ResponseEntity<Void> cancellaOpera(Integer idOpera) {
        return operaUseCasePortIn.deleteById(idOpera) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Opera> cercaOperaById(Integer idOpera) {
        return operaUseCasePortIn.findById(idOpera)
                .map(operaRecord -> ResponseEntity.ok(operaMapper.toOpera(operaRecord)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Opera>> cercaOpereByIdEvento(Integer idEvento) {
        return ResponseEntity.ok(operaUseCasePortIn.findByEvento(idEvento).stream()
                .map(operaMapper::toOpera)
                .toList());
    }

    @Override
    public ResponseEntity<List<Opera>> cercaOpereByNome(String nomeOpera) {
        return ResponseEntity.ok(operaUseCasePortIn.getByNomeLike(nomeOpera).stream()
                .map(operaMapper::toOpera)
                .toList());
    }

    @Override
    public ResponseEntity<List<Opera>> cercaOpereByAutore(String autoreOpera) {
        return ResponseEntity.ok(operaUseCasePortIn.getByAutoreLike(autoreOpera).stream()
                .map(operaMapper::toOpera)
                .toList());
    }

    @Override
    public ResponseEntity<Opera> creaOpera(CreaOperaRequest creaOperaRequest) {
        OperaRecord operaRecord = operaUseCasePortIn.save(
                new OperaRecord(null,
                        creaOperaRequest.getNome(),
                        creaOperaRequest.getAutore(),
                        creaOperaRequest.getTipo()));
        if (operaRecord == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean duplicate = Objects.equals(operaRecord.nome(), creaOperaRequest.getNome())
                && Objects.equals(operaRecord.autore(), creaOperaRequest.getAutore())
                && Objects.equals(operaRecord.tipo(), creaOperaRequest.getTipo());

        return duplicate
                ? ResponseEntity.ok(operaMapper.toOpera(operaRecord))
                : ResponseEntity.status(201).body(operaMapper.toOpera(operaRecord));
    }

    @Override
    public ResponseEntity<List<Opera>> opereAll() {
        List<Opera> opere = operaUseCasePortIn.findAll().stream()
                .map(operaMapper::toOpera)
                .toList();
        return ResponseEntity.ok(opere);
    }

    @Override
    public ResponseEntity<Void> modificaOpera(Integer idOpera, ModificaOperaRequest modificaOperaRequest) {
        OperaRecord operaRecord = new OperaRecord(
                idOpera,
                modificaOperaRequest.getNome(),
                modificaOperaRequest.getAutore(),
                modificaOperaRequest.getTipo()
        );
        return operaUseCasePortIn.updateById(idOpera, operaRecord)
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
