package org.core.domain.model;

import java.time.LocalDateTime;

public record EventoRecord(Integer idEvento, String nome, Integer numeroPosti, LocalDateTime data, Integer durata) {
}
