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

        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            DoacaoProduto dp = new DoacaoProduto();  // ← DO JEITO QUE VOCÊ QUER

            int ok = dp.inserirProdDoacao(dto, conn);

            if(ok > 0){
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
