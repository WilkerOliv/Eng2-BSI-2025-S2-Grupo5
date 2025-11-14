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
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Doacao d = new Doacao();
            d.setDataDoacao(doacao.getDataDoacao());
            d.setObservacao(doacao.getObservacao());
            d.setPessoaCarentePcCpf(doacao.getPessoaCarentePcCpf());

            Integer id = d.inserirDoacao(conn);

            return ResponseEntity.ok(id);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getListaDoacao")
    public ResponseEntity<List<DoacaoDTO>> getListaDoacao() {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();
            Doacao d = new Doacao();
            return ResponseEntity.ok(d.getListaDoacao(conn));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemDoacaoDTO>> listarItensDoacao(@PathVariable int id) {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();
            Doacao d = new Doacao();
            return ResponseEntity.ok(d.getItensDoacao(id, conn));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
