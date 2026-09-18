package org.core.domain.port.out;

import org.core.domain.model.OperaRecord;

import java.util.List;
import java.util.Optional;

public interface OperaRepositoryPortOut {
    Optional<OperaRecord> findById(Integer id);
    Optional<OperaRecord> findByNomeAndAutoreAndTipo(String nome, String autore, Integer tipo);
    List<OperaRecord> findByEvento(Integer idEvento);
    List<OperaRecord> findByNomeLike(String nomeOpera);
    List<OperaRecord> findByAutoreLike(String autoreOpera);
    OperaRecord save(OperaRecord opera);
    List<OperaRecord> findAll();
    Boolean deleteById(Integer id);
    Boolean updateById(Integer id, OperaRecord opera);
}
