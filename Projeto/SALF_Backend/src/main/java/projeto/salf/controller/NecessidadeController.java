package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.PessoaCarente;
import projeto.salf.service.NecessidadeService;

import java.util.List;

@RestController
@RequestMapping("/api/necessidades")
@CrossOrigin(origins = "*")
public class NecessidadeController {

    private final NecessidadeService service;

    public NecessidadeController(NecessidadeService service) {
        this.service = service;
    }

    // Pessoas Carentes
    @GetMapping("/pessoas")
    public List<PessoaCarente> listarPessoas() {
        return service.listarPessoas();
    }

    @PostMapping("/pessoas")
    public ResponseEntity<PessoaCarente> cadastrarPessoa(@RequestBody PessoaCarente pessoa) {
        return ResponseEntity.ok(service.salvarPessoa(pessoa));
    }

    // Necessidades de Produtos
    @GetMapping("/produtos")
    public List<NecessidadeProduto> listarNecessidades() {
        return service.listarNecessidades();
    }

    @PostMapping("/produtos")
    public ResponseEntity<NecessidadeProduto> registrarNecessidade(@RequestBody NecessidadeProduto necessidade) {
        return ResponseEntity.ok(service.salvarNecessidade(necessidade));
    }

    @DeleteMapping("/produtos/{cpf}/{produtoId}")
    public ResponseEntity<Void> excluirNecessidade(@PathVariable String cpf, @PathVariable Integer produtoId) {
        boolean removido = service.excluirNecessidade(cpf, produtoId);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
