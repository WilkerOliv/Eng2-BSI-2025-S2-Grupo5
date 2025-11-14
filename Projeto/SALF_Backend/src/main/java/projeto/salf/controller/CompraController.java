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
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Compra c = new Compra();
            c.setCompraValorTt(compra.getCompraValorTt());
            c.setDataCompra(compra.getDataCompra());
            c.setFornecCotacaoFornecedorId(compra.getFornecCotacaoFornecedorId());
            c.setFornecCotacaoCotacaoId(compra.getFornecCotacaoCotacaoId());
            c.setFornecedorId(compra.getFornecedorId());
            c.setFuncionarioFuncCpf(compra.getFuncionarioFuncCpf());

            Integer id = c.inserirCompra(conn);

            return ResponseEntity.ok(id);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/itens")
    public ResponseEntity<?> IncluirItensCompra(@RequestParam(required = false) LocalDate validade,
                                                @RequestBody ItensCompra itens) {

        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Compra compra = new Compra(); // objeto temporário para acessar método
            boolean ok = compra.inserirItens(itens, validade, conn);

            return ok ? ResponseEntity.ok().build()
                    : ResponseEntity.badRequest().build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
