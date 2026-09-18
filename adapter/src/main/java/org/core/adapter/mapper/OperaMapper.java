package org.core.adapter.mapper;

import org.core.adapter.repository.entity.OperaEntity;
import org.core.domain.model.OperaRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.Opera;

@Mapper(componentModel = "spring")
public interface OperaMapper {

    @Mapping(target = "idOpera", source = "idOpera")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "autore", source = "autore")
    @Mapping(target = "tipo", source = "tipo")
    Opera toOpera(OperaRecord operaRecord);

    @Mapping(target = "idOpera", source = "idOpera")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "autore", source = "autore")
    @Mapping(target = "tipo", source = "tipo")
    OperaRecord toOperaRecord(Opera opera);

    @Mapping(target = "nome", source = "record.nome")
    @Mapping(target = "autore", source = "record.autore")
    @Mapping(target = "tipo", source = "record.tipo")
    OperaEntity toOperaEntity(OperaRecord record);

    @Mapping(target = "idOpera", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "autore", source = "autore")
    @Mapping(target = "tipo", source = "tipo")
    OperaRecord toOperaRecord(OperaEntity entity);
}
