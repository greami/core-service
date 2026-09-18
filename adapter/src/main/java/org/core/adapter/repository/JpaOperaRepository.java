package org.core.adapter.repository;

import org.core.adapter.repository.entity.OperaEntity;

public interface JpaOperaRepository extends org.springframework.data.jpa.repository.JpaRepository<OperaEntity, Integer> {
    java.util.Optional<OperaEntity> findByNomeAndAutoreAndTipo(String nome, String autore, Integer tipo);
}
