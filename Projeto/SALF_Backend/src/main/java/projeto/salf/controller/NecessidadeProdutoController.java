package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.NecessidadeProduto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/necessidades/produtos")
@CrossOrigin(origins = "*")
public class NecessidadeProdutoController {

    private void garantirConexao() {
        SingletonDB.getConexao();
    }

    @GetMapping
    public ResponseEntity<List<NecessidadeProduto>> listar(
            @RequestParam(name = "cpf", required = false) String cpf,
            @RequestParam(name = "termo", required = false) String termo) {
        try {
            garantirConexao();

            List<NecessidadeProduto> lista;
            if (cpf != null && !cpf.isBlank()) {
                lista = NecessidadeProduto.listarPorPessoa(cpf);
            } else if (termo != null && !termo.isBlank()) {
                lista = NecessidadeProduto.buscarPorTermo(termo);
            } else {
                lista = NecessidadeProduto.listarTodas();
            }

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody NecessidadeProduto necessidade) {
        try {
            garantirConexao();

            // converte data se vier nula ou string vazia (no JSON ela pode vir como null)
            if (necessidade.getData() == null) {
                necessidade.setData(LocalDate.now());
            }

            necessidade.salvar();
            return ResponseEntity.ok(necessidade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao salvar necessidade de produto."));
        }
    }

    /**
     * DTO simples para exclusão.
     */
    public static class NecessidadeKeyDTO {
        public String pessoaCarentePcCpf;
        public Integer produtoProdCod;
    }


    @DeleteMapping
    public ResponseEntity<?> excluir(@RequestBody NecessidadeKeyDTO dto) {
        try {
            garantirConexao();
            boolean ok = NecessidadeProduto.excluir(dto.pessoaCarentePcCpf, dto.produtoProdCod);
            if (!ok) {
                return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir necessidade."));
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro ao excluir necessidade."));
        }
    }
}
