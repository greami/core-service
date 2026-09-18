package org.core.adapter.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventoOperaId implements Serializable {
    private Integer idEvento;
    private Integer idOpera;
}
