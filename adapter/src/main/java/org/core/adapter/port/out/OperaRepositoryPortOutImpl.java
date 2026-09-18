package org.core.adapter.port.out;

import org.core.adapter.mapper.OperaMapper;
import org.core.adapter.repository.JpaOperaRepository;
import org.core.adapter.repository.entity.OperaEntity;
import org.core.domain.model.OperaRecord;
import org.core.domain.port.out.OperaRepositoryPortOut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OperaRepositoryPortOutImpl implements OperaRepositoryPortOut {

    private final JpaOperaRepository jpaOperaRepository;
    private final OperaMapper operaMapper;

    public OperaRepositoryPortOutImpl(JpaOperaRepository jpaOperaRepository, OperaMapper operaMapper) {
        this.jpaOperaRepository = jpaOperaRepository;
        this.operaMapper = operaMapper;
    }

    @Override
    public Optional<OperaRecord> findById(Integer id) {
        return jpaOperaRepository.findById(id).map(operaMapper::toOperaRecord);
    }

    @Override
    public Optional<OperaRecord> findByNomeAndAutoreAndTipo(String nome, String autore, Integer tipo) {
        return jpaOperaRepository.findByNomeAndAutoreAndTipo(nome, autore, tipo)
                .map(operaMapper::toOperaRecord);
    }

    @Override
    public List<OperaRecord> findByEvento(Integer idEvento) {
        return jpaOperaRepository.findByIdEvento(idEvento).stream()
                .map(operaMapper::toOperaRecord)
                .toList();
    }

    @Override
    public List<OperaRecord> findByNomeLike(String nomeOpera) {
        return jpaOperaRepository.findByNomeContainingIgnoreCase(nomeOpera).stream()
                .map(operaMapper::toOperaRecord)
                .toList();
    }

    @Override
    public List<OperaRecord> findByAutoreLike(String autoreOpera) {
        return jpaOperaRepository.findByAutoreContainingIgnoreCase(autoreOpera).stream()
                .map(operaMapper::toOperaRecord)
                .toList();
    }

    @Override
    public OperaRecord save(OperaRecord opera) {
        try {
            OperaEntity saved = jpaOperaRepository.save(operaMapper.toOperaEntity(opera));
            return operaMapper.toOperaRecord(saved);
        } catch (DataIntegrityViolationException e) {
            return findByNomeAndAutoreAndTipo(opera.nome(), opera.autore(), opera.tipo())
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public List<OperaRecord> findAll() {
        return jpaOperaRepository.findAll().stream()
                .map(operaMapper::toOperaRecord)
                .toList();
    }

    @Override
    public Boolean deleteById(Integer id) {
        if (!jpaOperaRepository.existsById(id)) {
            return false;
        }
        jpaOperaRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean updateById(Integer id, OperaRecord opera) {
        return jpaOperaRepository.findById(id)
                .map(existing -> {
                    OperaEntity updated = new OperaEntity(
                            existing.getId(),
                            opera.nome(),
                            opera.autore(),
                            opera.tipo()
                    );
                    return jpaOperaRepository.save(updated) != null;
                })
                .orElse(false);
    }
}
