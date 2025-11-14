package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Fornecedor;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fornecedor")
@CrossOrigin(origins = "*")
public class FornecedorController {

    @GetMapping("/all")
    public ResponseEntity<List<Fornecedor>> listaFornecedores() {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Fornecedor fornecedor = new Fornecedor();
            List<Fornecedor> lista = fornecedor.getListaFornecedores(conn);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/todosPorCotacao")
    public ResponseEntity<Map<Integer, List<Fornecedor>>> listaFornecedoresPorCotacao() {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Fornecedor fornecedor = new Fornecedor();
            Map<Integer, List<Fornecedor>> lista = fornecedor.getListaFornecedoresPorCotacao(conn);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
