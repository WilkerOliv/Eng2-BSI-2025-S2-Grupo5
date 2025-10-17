package projeto.salf.service;

import org.springframework.stereotype.Service;
import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.NecessidadeProdutoId;
import projeto.salf.model.PessoaCarente;
import projeto.salf.repository.NecessidadeProdutoRepository;
import projeto.salf.repository.PessoaCarenteRepository;

import java.util.List;

@Service
public class NecessidadeService {

    private final PessoaCarenteRepository pessoaRepository;
    private final NecessidadeProdutoRepository necessidadeRepository;

    public NecessidadeService(PessoaCarenteRepository pessoaRepository, NecessidadeProdutoRepository necessidadeRepository) {
        this.pessoaRepository = pessoaRepository;
        this.necessidadeRepository = necessidadeRepository;
    }

    // Pessoas
    public List<PessoaCarente> listarPessoas() {
        return pessoaRepository.findAll();
    }

    public PessoaCarente salvarPessoa(PessoaCarente pessoa) {
        return pessoaRepository.save(pessoa);
    }

    // Necessidades
    public List<NecessidadeProduto> listarNecessidades() {
        return necessidadeRepository.findAll();
    }

    public NecessidadeProduto salvarNecessidade(NecessidadeProduto necessidade) {
        return necessidadeRepository.save(necessidade);
    }

    public boolean excluirNecessidade(String cpf, Integer produtoId) {
        NecessidadeProdutoId id = new NecessidadeProdutoId();
        id.setProdutoId(produtoId);
        id.setPessoaCpf(cpf);
        if (necessidadeRepository.existsById(id)) {
            necessidadeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
