package org.core.domain.port.in;

import org.core.domain.model.OperaRecord;

import java.util.List;
import java.util.Optional;

public interface OperaUseCasePortIn {
    Optional<OperaRecord> findById(Integer id);
    Optional<OperaRecord> findByNomeAndAutoreAndTipo(String nome, String autore, Integer tipo);
    List<OperaRecord> findByEvento(Integer idEvento);
    List<OperaRecord> getByNomeLike(String nomeOpera);
    List<OperaRecord> getByAutoreLike(String autoreOpera);
    OperaRecord save(OperaRecord opera);
    List<OperaRecord> findAll();
    Boolean deleteById(Integer id);
    Boolean updateById(Integer id, OperaRecord opera);
}
