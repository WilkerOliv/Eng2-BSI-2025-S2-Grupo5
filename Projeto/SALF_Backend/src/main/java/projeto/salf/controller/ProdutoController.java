package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.Produto;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private Conexao conexao() {
        return SingletonDB.getConexao();
    }


    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos(
            @RequestParam(name = "categoria", required = false) Integer categoria,
            @RequestParam(name = "termo", required = false) String termo) {
        try {
            List<Produto> lista;
            if (categoria != null) {
                lista = Produto.buscarPorCategoriaEDescricao(categoria, termo, conexao());
            } else if (termo != null && !termo.isBlank()) {
                lista = Produto.buscarPorDescricao(termo, conexao());
            } else {
                lista = Produto.listarTodos(conexao());
            }

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Busca produto pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        try {
            Produto p = Produto.buscarPorId(id, conexao());
            if (p == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
