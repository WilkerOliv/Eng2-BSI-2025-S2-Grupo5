package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.CestaBasica;
import projeto.salf.repository.CestaBasicaRepository;

import java.util.List;

@Service
public class CestaBasicaService {
    @Autowired
    private CestaBasicaRepository repository;

    public List<CestaBasica> listarTodas() { return repository.findAll(); }
    public CestaBasica salvar(CestaBasica c) { return repository.save(c); }
    public void excluir(Integer id) { repository.deleteById(id); }
}