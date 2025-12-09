package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.CestaBasicaModel;

import java.sql.Connection;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/apis/cesta")
@CrossOrigin(origins = "*")
public class CestaBasicaRestController {

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> dados) {

        Connection conn = null;

        try {
            SingletonDB.conectar();

            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            CestaBasicaModel model = new CestaBasicaModel();

            Map<String, Object> resp = model.registrar(dados, conn);

            if (!(boolean) resp.get("sucesso")) {
                conn.rollback();
                return ResponseEntity.badRequest().body(resp);
            }

            conn.commit();
            return ResponseEntity.ok(resp);

        } catch (Exception e) {

            e.printStackTrace();

            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}

            return ResponseEntity.internalServerError().body(
                    Map.of("sucesso", false, "mensagem", "Erro inesperado no servidor")
            );

        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        try {
            SingletonDB.conectar();
            Connection conn = SingletonDB.getConexao().getConnect();

            CestaBasicaModel model = new CestaBasicaModel();
            return ResponseEntity.ok(model.listar(conn));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/itens/{id}")
    public ResponseEntity<?> listarItens(@PathVariable Integer id) {
        try {
            SingletonDB.conectar();
            Connection conn = SingletonDB.getConexao().getConnect();

            CestaBasicaModel model = new CestaBasicaModel();
            return ResponseEntity.ok(model.listarItens(id, conn));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
