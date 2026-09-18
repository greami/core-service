package org.core.adapter.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "EVENTI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "NUMEROPOSTI", nullable = false)
    private Integer numeroPosti;

    @Column(name = "DATA", nullable = false)
    private LocalDateTime data;

    @Column(name = "DURATA", nullable = false)
    private Integer durata;
}
