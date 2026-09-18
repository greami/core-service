package org.core.adapter.mapper;

import org.core.adapter.repository.entity.EventoOperaEntity;
import org.core.domain.model.EventoOperaRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.EventoOpera;

@Mapper(componentModel = "spring")
public interface EventoOperaMapper {

    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "idOpera", source = "idOpera")
    EventoOpera toEventoOpera(EventoOperaRecord eventoOperaRecord);

    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "idOpera", source = "idOpera")
    EventoOperaRecord toEventoOperaRecord(EventoOpera eventoOpera);

    @Mapping(target = "idEvento", source = "record.idEvento")
    @Mapping(target = "idOpera", source = "record.idOpera")
    EventoOperaEntity toEventoOperaEntity(EventoOperaRecord record);

    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "idOpera", source = "idOpera")
    EventoOperaRecord toEventoOperaRecord(EventoOperaEntity entity);
}
