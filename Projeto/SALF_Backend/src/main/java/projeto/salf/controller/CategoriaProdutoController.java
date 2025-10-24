package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.CategoriaProduto;
import projeto.salf.service.CategoriaProdutoService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProdutoController {
    private final CategoriaProdutoService service = new CategoriaProdutoService();

    @GetMapping
    public List<CategoriaProduto> listarTodas() { return service.listarTodas(); }

    @PostMapping
    public CategoriaProduto salvar(@RequestBody CategoriaProduto categoria) { return service.salvar(categoria); }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProduto> atualizar(@PathVariable Integer id, @RequestBody CategoriaProduto categoria) {
        categoria.setCatCod(id);
        return ResponseEntity.ok(service.salvar(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
