package org.core.adapter.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "EVENTO_OPERA")
@IdClass(EventoOperaId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoOperaEntity {

    @Id
    @Column(name = "IDEVENTO", nullable = false)
    private Integer idEvento;

    @Id
    @Column(name = "IDOPERA", nullable = false)
    private Integer idOpera;
}
