package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.dto.DoacaoDTO;
import projeto.salf.dto.ItemDoacaoDTO;
import projeto.salf.model.Doacao;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.util.List;

@RestController
@RequestMapping("/api/doacao")
@CrossOrigin(origins = "*")
public class DoacaoController {

    @PostMapping("/inserir")
    public ResponseEntity<Integer> inserirDoacao(@RequestBody Doacao doacao) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            Doacao d = new Doacao();
            d.setDataDoacao(doacao.getDataDoacao());
            d.setObservacao(doacao.getObservacao());
            d.setPessoaCarentePcCpf(doacao.getPessoaCarentePcCpf());

            Integer id = d.inserirDoacao(conn);

            conn.commit();
            conn.setAutoCommit(true);

            return ResponseEntity.ok(id);

        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return ResponseEntity.internalServerError().build();

        } finally {

            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/getListaDoacao")
    public ResponseEntity<List<DoacaoDTO>> getListaDoacao() {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(true); // GET → não altera banco

            Doacao d = new Doacao();
            return ResponseEntity.ok(d.getListaDoacao(conn));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            Doacao d = new Doacao();
            boolean ok = d.excluirDoacao(id, conn);

            conn.commit();
            conn.setAutoCommit(true);

            if (!ok) return ResponseEntity.badRequest().build();

            return ResponseEntity.ok().build();

        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return ResponseEntity.internalServerError().build();

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemDoacaoDTO>> listarItensDoacao(@PathVariable int id) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(true); // GET → sem transação

            Doacao d = new Doacao();
            return ResponseEntity.ok(d.getItensDoacao(id, conn));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();

        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
