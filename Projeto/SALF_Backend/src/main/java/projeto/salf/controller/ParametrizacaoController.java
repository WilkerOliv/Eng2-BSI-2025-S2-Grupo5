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

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            boolean novo = parametrizacao.salvarOuAtualizar(conn);

            conn.commit();
            conn.setAutoCommit(true);

            return ResponseEntity.ok(novo ? "Empresa cadastrada" : "Empresa atualizada");

        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (conn != null)
                    conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

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

    @GetMapping
    public ResponseEntity<?> getParametrizacao(@RequestParam String email) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(true); // GET NÃO altera banco

            Parametrizacao paModel = new Parametrizacao();
            Parametrizacao pa = paModel.getByEmail(email, conn);

            return (pa != null)
                    ? ResponseEntity.ok(pa)
                    : ResponseEntity.status(404).body("Parametrização não encontrada.");

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

    @GetMapping("/existeEmpresa")
    public ResponseEntity<Boolean> existeEmpresa() {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(true);

            Parametrizacao paModel = new Parametrizacao();
            boolean existe = paModel.existeEmpresa(conn);

            return ResponseEntity.ok(existe);

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

    @GetMapping("/unica")
    public ResponseEntity<?> getUnica() {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(true);

            Parametrizacao paModel = new Parametrizacao();
            Parametrizacao unica = paModel.getUnica(conn);

            return (unica != null)
                    ? ResponseEntity.ok(unica)
                    : ResponseEntity.status(404).body("Parametrização não encontrada.");

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
