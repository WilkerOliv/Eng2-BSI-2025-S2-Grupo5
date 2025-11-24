package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.Produto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private Conexao conexao() {
        return SingletonDB.getConexao();
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        try {
            List<Produto> lista = Produto.listarTodos(conexao());
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id) {
        try {
            Produto p = Produto.buscarPorId(id, conexao());
            if (p == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar");
        }
    }

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody Produto p) {
        try {
            p.salvar(conexao());
            return ResponseEntity.ok(p);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao inserir");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody Produto p) {
        try {
            p.setProdCod(id);
            p.salvar(conexao());
            return ResponseEntity.ok(p);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        try {
            boolean ok = Produto.excluir(id, conexao());
            if (!ok) return ResponseEntity.status(500).body("Erro ao excluir");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao excluir");
        }
    }
}
