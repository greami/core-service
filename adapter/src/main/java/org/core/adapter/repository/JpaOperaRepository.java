package org.core.adapter.repository;

import org.core.adapter.repository.entity.OperaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaOperaRepository extends org.springframework.data.jpa.repository.JpaRepository<OperaEntity, Integer> {
    java.util.Optional<OperaEntity> findByNomeAndAutoreAndTipo(String nome, String autore, Integer tipo);
    List<OperaEntity> findByNomeContainingIgnoreCase(String nome);
    List<OperaEntity> findByAutoreContainingIgnoreCase(String autore);

    @Query(value = "SELECT o.* FROM OPERE o INNER JOIN EVENTO_OPERA eo ON eo.IDOPERA = o.ID WHERE eo.IDEVENTO = :idEvento", nativeQuery = true)
    List<OperaEntity> findByIdEvento(@Param("idEvento") Integer idEvento);
}
