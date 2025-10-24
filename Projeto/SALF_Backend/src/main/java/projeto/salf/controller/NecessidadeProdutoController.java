package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.NecessidadeProdutoId;
import projeto.salf.service.NecessidadeProdutoService;

import java.util.List;

@RestController
@RequestMapping("/api/necessidades/produtos")
@CrossOrigin(origins = "*")
public class NecessidadeProdutoController {
    private final NecessidadeProdutoService service = new NecessidadeProdutoService();

    @GetMapping
    public List<NecessidadeProduto> listarTodas() { return service.listarTodas(); }

    @PostMapping
    public NecessidadeProduto salvar(@RequestBody NecessidadeProduto necessidade) { return service.salvar(necessidade); }

    @DeleteMapping
    public ResponseEntity<Void> excluir(@RequestBody NecessidadeProdutoId id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
