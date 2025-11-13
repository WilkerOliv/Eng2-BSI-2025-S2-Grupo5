package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.CategoriaProduto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProdutoController {

    private void garantirConexao() {
        SingletonDB.getConexao(); // se estiver fechada, o Singleton abre
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProduto>> listarTodas() {
        try {
            garantirConexao();
            List<CategoriaProduto> lista = CategoriaProduto.listarTodas();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProduto> buscarPorId(@PathVariable Integer id) {
        try {
            garantirConexao();
            CategoriaProduto c = CategoriaProduto.buscarPorId(id);
            if (c == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody CategoriaProduto categoria) {
        try {
            garantirConexao();
            categoria.salvar();
            return ResponseEntity.ok(categoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao salvar categoria."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id,
                                       @RequestBody CategoriaProduto categoria) {
        try {
            garantirConexao();
            categoria.setCatCod(id);
            categoria.salvar();
            return ResponseEntity.ok(categoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao atualizar categoria."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        try {
            garantirConexao();
            boolean ok = CategoriaProduto.excluir(id);
            if (!ok) {
                return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir categoria."));
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir categoria."));
        }
    }
}
