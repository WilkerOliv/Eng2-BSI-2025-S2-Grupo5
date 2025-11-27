package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Produto;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @GetMapping("/lista")
    public ResponseEntity<?> getListProdutos() {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(true); // GET NÃO ALTERA BANCO

            Produto produto = new Produto();
            List<Produto> produtos = produto.getLista(conn);

            return ResponseEntity.ok(produtos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();

        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
