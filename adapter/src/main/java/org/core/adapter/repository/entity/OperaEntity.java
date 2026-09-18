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

@Entity
@Table(name = "OPERE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "AUTORE", nullable = false)
    private String autore;

    @Column(name = "TIPO", nullable = false)
    private Integer tipo;
}
