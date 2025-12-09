package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.DoacaoPCModel;

import java.sql.Connection;
import java.util.Map;

@RestController
@RequestMapping("/apis/doacao_pc")
@CrossOrigin(origins = "*")
public class DoacaoPessoaCarenteRestController {

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> body) {

        Connection conn = null;

        try {
            if (!SingletonDB.conectar()) {
                return ResponseEntity.status(500)
                        .body(Map.of("sucesso", false, "mensagem", "Falha ao conectar ao banco"));
            }

            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            DoacaoPCModel model = new DoacaoPCModel();

            Map<String, Object> resp = model.registrar(body, conn);

            boolean ok = Boolean.TRUE.equals(resp.get("sucesso"));
            if (!ok) {
                conn.rollback();
                return ResponseEntity.badRequest().body(resp);
            }

            conn.commit();
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            e.printStackTrace();

            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}

            return ResponseEntity.status(500)
                    .body(Map.of("sucesso", false, "mensagem", "Erro ao registrar doação"));

        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }
}
