package org.core.adapter.mapper;

import org.core.adapter.repository.entity.EventoEntity;
import org.core.domain.model.EventoRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.CreaEventoRequest;
import org.openapitools.model.Evento;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "numeroPosti", source = "numeroPosti")
    @Mapping(target = "data", source = "data")
    @Mapping(target = "durata", source = "durata")
    Evento toEvento(EventoRecord eventoRecord);

    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "numeroPosti", source = "numeroPosti")
    @Mapping(target = "data", source = "data")
    @Mapping(target = "durata", source = "durata")
    EventoRecord toEventoRecord(Evento evento);

    @Mapping(target = "nome", source = "record.nome")
    @Mapping(target = "numeroPosti", source = "record.numeroPosti")
    @Mapping(target = "data", source = "record.data")
    @Mapping(target = "durata", source = "record.durata")
    EventoEntity toEventoEntity(EventoRecord record);
}
