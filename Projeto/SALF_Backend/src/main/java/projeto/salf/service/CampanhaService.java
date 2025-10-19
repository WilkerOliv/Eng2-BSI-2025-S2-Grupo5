package projeto.salf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.salf.model.Campanha;
import projeto.salf.model.Funcionario;
import projeto.salf.repository.CampanhaRepository;
import projeto.salf.repository.FuncionarioRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public CampanhaService(CampanhaRepository campanhaRepository,
                           FuncionarioRepository funcionarioRepository) {
        this.campanhaRepository = campanhaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Transactional
    public Campanha criarCampanha(String descricao, LocalDate dtIni, LocalDate dtFim,
                                  String funcionarioCpf, String observacao) {

        Funcionario func = funcionarioRepository.findById(funcionarioCpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado: " + funcionarioCpf));

        Campanha c = new Campanha();
        c.setCampanhaDescr(descricao);
        c.setCampanhaDtIni(dtIni);
        c.setCampanhaDtFim(dtFim);
        c.setCampanhaTotalArrecado(0.0);
        c.setObservacao(observacao);
        c.setFuncionario(func);

        return campanhaRepository.save(c);
    }

    @Transactional
    public Campanha finalizarCampanha(Integer idCampanha, Double totalArrecadado,
                                      LocalDate dataFim, String observacao) {

        Campanha c = campanhaRepository.findById(idCampanha)
                .orElseThrow(() -> new NoSuchElementException("Campanha não encontrada: " + idCampanha));

        if (dataFim != null) {
            c.setCampanhaDtFim(dataFim);
        }
        if (observacao != null) {
            c.setObservacao(observacao);
        }
        c.setCampanhaTotalArrecado(totalArrecadado != null ? totalArrecadado : 0.0);

        return campanhaRepository.save(c);
    }

    public Campanha buscarPorId(Integer id) {
        return campanhaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Campanha não encontrada: " + id));
    }
}
