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
@Table(name = "FEEDBACK_EVENTI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackEventiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "IDUTENTE", nullable = false)
    private Integer idUtente;

    @Column(name = "IDEVENTO", nullable = false)
    private Integer idEvento;

    @Column(name = "VOTO", nullable = false)
    private Integer voto;

    @Column(name = "NOTE", nullable = false)
    private String note;
}
