package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Compra;
import projeto.salf.model.ItensCompra;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/compra")
@CrossOrigin(origins = "*")
public class CompraController {

    @PostMapping()
    public ResponseEntity<Integer> IncluirCompra(@RequestBody Compra compra) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            Compra c = new Compra();
            c.setCompraValorTt(compra.getCompraValorTt());
            c.setDataCompra(compra.getDataCompra());
            c.setFornecCotacaoFornecedorId(compra.getFornecCotacaoFornecedorId());
            c.setFornecCotacaoCotacaoId(compra.getFornecCotacaoCotacaoId());
            c.setFornecedorId(compra.getFornecedorId());
            c.setFuncionarioFuncCpf(compra.getFuncionarioFuncCpf());

            Integer id = c.inserirCompra(conn);

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



    @PostMapping("/itens")
    public ResponseEntity<?> IncluirItensCompra(@RequestParam(required = false) LocalDate validade,
                                                @RequestBody ItensCompra itens) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            Compra compra = new Compra();
            boolean ok = compra.inserirItens(itens, validade, conn);

            conn.commit();
            conn.setAutoCommit(true);

            return ok ? ResponseEntity.ok().build()
                    : ResponseEntity.badRequest().build();

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

    @GetMapping("/listar")
    public ResponseEntity<?> listarCompras() {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();
            Compra c = new Compra();
            return ResponseEntity.ok(c.listarCompras(conn));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<?> listarItensCompra(@PathVariable int id) {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();
            Compra c = new Compra();
            return ResponseEntity.ok(c.listarItens(id, conn));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCompra(@PathVariable int id) {

        Connection conn = null;

        try {
            conn = SingletonDB.getConexao().getConnect();
            conn.setAutoCommit(false);

            Compra compra = new Compra();

            boolean ok = compra.excluirCompra(id, conn);

            if (!ok) {
                conn.rollback();
                return ResponseEntity.badRequest().body("Não foi possível excluir a compra.");
            }

            conn.commit();
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            return ResponseEntity.internalServerError().build();

        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }


}
