package projeto.salf.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Parametrizacao;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;

@RestController
@RequestMapping("/api/parametrizacao")
@CrossOrigin("*")
public class ParametrizacaoController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> salvarOuAtualizar(@RequestBody Parametrizacao parametrizacao) {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            // Controller → Model
            boolean novo = parametrizacao.salvarOuAtualizar(conn);

            return ResponseEntity.ok(novo ? "Empresa cadastrada" : "Empresa atualizada");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getParametrizacao(@RequestParam String email) {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Parametrizacao paModel = new Parametrizacao();
            Parametrizacao pa = paModel.getByEmail(email, conn);

            return (pa != null)
                    ? ResponseEntity.ok(pa)
                    : ResponseEntity.status(404).body("Parametrização não encontrada.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/existeEmpresa")
    public ResponseEntity<Boolean> existeEmpresa() {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Parametrizacao paModel = new Parametrizacao();
            boolean existe = paModel.existeEmpresa(conn);

            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/unica")
    public ResponseEntity<?> getUnica() {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Parametrizacao paModel = new Parametrizacao();
            Parametrizacao unica = paModel.getUnica(conn);

            return (unica != null)
                    ? ResponseEntity.ok(unica)
                    : ResponseEntity.status(404).body("Parametrização não encontrada.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
