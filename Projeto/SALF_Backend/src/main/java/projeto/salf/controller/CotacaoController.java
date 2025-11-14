package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Cotacao;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.util.List;

@RestController
@RequestMapping("/api/cotacao")
@CrossOrigin(origins = "*")
public class CotacaoController {

    @GetMapping("/lista")
    public ResponseEntity<List<Cotacao>> getCotacao() {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Cotacao cot = new Cotacao();  // ← JEITO QUE VOCÊ PEDIU
            List<Cotacao> lista = cot.getListaCotacao(conn);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
