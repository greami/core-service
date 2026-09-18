package org.core.domain.model;

import java.time.LocalDateTime;

public record PrenotazioneRecord(Integer id, Integer idUtente, Integer idEvento, Integer numeroPosti, LocalDateTime dataPrenotazione, LocalDateTime dataModifica)
{
}
