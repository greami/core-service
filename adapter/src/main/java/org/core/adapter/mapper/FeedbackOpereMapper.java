package org.core.adapter.mapper;

import org.core.adapter.repository.entity.FeedbackOpereEntity;
import org.core.domain.model.FeedbackOpereRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.FeedbackOpere;

@Mapper(componentModel = "spring")
public interface FeedbackOpereMapper {

    @Mapping(target = "idFeedback", source = "id")
    @Mapping(target = "idUtente", source = "idUtente")
    @Mapping(target = "idOpera", source = "idOpera")
    @Mapping(target = "voto", source = "voto")
    @Mapping(target = "note", source = "note")
    FeedbackOpere toFeedbackOpere(FeedbackOpereRecord feedbackOpereRecord);

    @Mapping(target = "id", source = "idFeedback")
    @Mapping(target = "idUtente", source = "idUtente")
    @Mapping(target = "idOpera", source = "idOpera")
    @Mapping(target = "voto", source = "voto")
    @Mapping(target = "note", source = "note")
    FeedbackOpereRecord toFeedbackOpereRecord(FeedbackOpere feedbackOpere);

    @Mapping(target = "idUtente", source = "record.idUtente")
    @Mapping(target = "idOpera", source = "record.idOpera")
    @Mapping(target = "voto", source = "record.voto")
    @Mapping(target = "note", source = "record.note")
    FeedbackOpereEntity toFeedbackOpereEntity(FeedbackOpereRecord record);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "idUtente", source = "idUtente")
    @Mapping(target = "idOpera", source = "idOpera")
    @Mapping(target = "voto", source = "voto")
    @Mapping(target = "note", source = "note")
    FeedbackOpereRecord toFeedbackOpereRecord(FeedbackOpereEntity entity);
}
