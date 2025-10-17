package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.CategoriaProduto;
import projeto.salf.service.CategoriaProdutoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProdutoController {

    private final CategoriaProdutoService service;

    public CategoriaProdutoController(CategoriaProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaProduto> listarCategorias() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProduto> buscarPorId(@PathVariable Integer id) {
        Optional<CategoriaProduto> categoria = service.buscarPorId(id);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaProduto> criarCategoria(@RequestBody CategoriaProduto categoria) {
        return ResponseEntity.ok(service.salvar(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProduto> atualizarCategoria(@PathVariable Integer id, @RequestBody CategoriaProduto categoriaAtualizada) {
        CategoriaProduto atualizado = service.atualizar(id, categoriaAtualizada);
        return (atualizado != null) ? ResponseEntity.ok(atualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable Integer id) {
        boolean removido = service.excluir(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
