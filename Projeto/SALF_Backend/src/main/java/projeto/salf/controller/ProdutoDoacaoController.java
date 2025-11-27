package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.dto.ProdutoDoacaoEstoqueDTO;
import projeto.salf.model.DoacaoProduto;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;

@RestController
@RequestMapping("/api/doacao_prod")
@CrossOrigin(origins = "*")
public class ProdutoDoacaoController {

    @PostMapping
    public ResponseEntity<?> insereProdDoacao(@RequestBody ProdutoDoacaoEstoqueDTO dto) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            DoacaoProduto dp = new DoacaoProduto();

            int ok = dp.inserirProdDoacao(dto, conn);

            conn.commit();
            conn.setAutoCommit(true);

            if (ok > 0) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();

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

}
