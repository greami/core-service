package org.core.application.port.in.useCase;

import lombok.RequiredArgsConstructor;
import org.core.domain.model.OperaRecord;
import org.core.domain.port.in.OperaUseCasePortIn;
import org.core.domain.port.out.OperaRepositoryPortOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperaUseCaseImpl implements OperaUseCasePortIn {

    @Autowired
    private OperaRepositoryPortOut operaRepository;

    @Override
    public Optional<OperaRecord> findById(Integer id) {
        return operaRepository.findById(id);
    }

    @Override
    public Optional<OperaRecord> findByNomeAndAutoreAndTipo(String nome, String autore, Integer tipo) {
        return operaRepository.findByNomeAndAutoreAndTipo(nome, autore, tipo);
    }

    @Override
    public List<OperaRecord> findByEvento(Integer idEvento) {
        return operaRepository.findByEvento(idEvento);
    }

    @Override
    public List<OperaRecord> getByNomeLike(String nomeOpera) {
        return operaRepository.findByNomeLike(nomeOpera);
    }

    @Override
    public List<OperaRecord> getByAutoreLike(String autoreOpera) {
        return operaRepository.findByAutoreLike(autoreOpera);
    }

    @Override
    public OperaRecord save(OperaRecord opera) {
        Optional<OperaRecord> existing = operaRepository.findByNomeAndAutoreAndTipo(opera.nome(), opera.autore(), opera.tipo());
        if (existing.isPresent()) {
            return existing.get();
        }
        return operaRepository.save(opera);
    }

    @Override
    public List<OperaRecord> findAll() {
        return operaRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer id) {
        return operaRepository.deleteById(id);
    }

    @Override
    public Boolean updateById(Integer id, OperaRecord opera) {
        return operaRepository.updateById(id, opera);
    }
}
