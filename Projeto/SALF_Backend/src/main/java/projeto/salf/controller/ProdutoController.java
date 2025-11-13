package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.Produto;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private void garantirConexao() {
        SingletonDB.getConexao();
    }


    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos(
            @RequestParam(name = "categoria", required = false) Integer categoria,
            @RequestParam(name = "termo", required = false) String termo) {
        try {
            garantirConexao();

            List<Produto> lista;
            if (categoria != null) {
                lista = Produto.buscarPorCategoriaEDescricao(categoria, termo);
            } else if (termo != null && !termo.isBlank()) {
                lista = Produto.buscarPorDescricao(termo);
            } else {
                lista = Produto.listarTodos();
            }

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Busca produto pelo ID (para uso interno do sistema).
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        try {
            garantirConexao();
            Produto p = Produto.buscarPorId(id);
            if (p == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
