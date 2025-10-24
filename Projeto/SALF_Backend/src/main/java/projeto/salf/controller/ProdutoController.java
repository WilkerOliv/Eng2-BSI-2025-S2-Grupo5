package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Produto;
import projeto.salf.service.ProdutoService;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
    private final ProdutoService service = new ProdutoService();

    @GetMapping
    public List<Produto> listarTodos() { return service.listarTodos(); }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) { return service.salvar(produto); }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Integer id, @RequestBody Produto produto) {
        produto.setProdCod(id);
        return ResponseEntity.ok(service.salvar(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
