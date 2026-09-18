package org.core.adapter.mapper;

import org.core.adapter.repository.entity.FeedbackEventiEntity;
import org.core.domain.model.FeedbackEventiRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.FeedbackEventi;

@Mapper(componentModel = "spring")
public interface FeedbackEventiMapper {

    @Mapping(target = "idFeedback", source = "id")
    @Mapping(target = "idUtente", source = "idUtente")
    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "voto", source = "voto")
    @Mapping(target = "note", source = "note")
    FeedbackEventi toFeedbackEventi(FeedbackEventiRecord feedbackEventiRecord);

    @Mapping(target = "id", source = "idFeedback")
    @Mapping(target = "idUtente", source = "idUtente")
    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "voto", source = "voto")
    @Mapping(target = "note", source = "note")
    FeedbackEventiRecord toFeedbackEventiRecord(FeedbackEventi feedbackEventi);

    @Mapping(target = "idUtente", source = "record.idUtente")
    @Mapping(target = "idEvento", source = "record.idEvento")
    @Mapping(target = "voto", source = "record.voto")
    @Mapping(target = "note", source = "record.note")
    FeedbackEventiEntity toFeedbackEventiEntity(FeedbackEventiRecord record);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "idUtente", source = "idUtente")
    @Mapping(target = "idEvento", source = "idEvento")
    @Mapping(target = "voto", source = "voto")
    @Mapping(target = "note", source = "note")
    FeedbackEventiRecord toFeedbackEventiRecord(FeedbackEventiEntity entity);
}
