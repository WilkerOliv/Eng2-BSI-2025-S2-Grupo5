package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.NecessidadeProduto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/necessidades/produtos")
@CrossOrigin(origins = "*")
public class NecessidadeProdutoController {

    private Conexao conexao() {
        return SingletonDB.getConexao();
    }

    // LISTA AGRUPADA (UM POR NEC_ID)
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(NecessidadeProduto.listarAgrupado(conexao()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao listar necessidades"));
        }
    }

    // OBTER ITENS DE UMA LISTA
    @GetMapping("/{necId}")
    public ResponseEntity<?> listarItens(@PathVariable Integer necId) {
        try {
            return ResponseEntity.ok(NecessidadeProduto.listarItens(necId, conexao()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao listar itens"));
        }
    }

    // CRIAR LISTA NOVA
    @PostMapping("/lista")
    public ResponseEntity<?> criarLista(@RequestBody NecessidadeProduto.ListaDTO dto) {
        try {
            Integer id = NecessidadeProduto.criarLista(dto, conexao());
            return ResponseEntity.ok(Map.of("necId", id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao criar lista"));
        }
    }

    // EDITAR LISTA
    @PutMapping("/lista/{necId}")
    public ResponseEntity<?> atualizarLista(@PathVariable Integer necId,
                                            @RequestBody NecessidadeProduto.ListaDTO dto) {
        try {
            NecessidadeProduto.atualizarLista(necId, dto, conexao());
            return ResponseEntity.ok(Map.of("mensagem", "Lista atualizada"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao atualizar lista"));
        }
    }

    // EXCLUIR LISTA INTEIRA
    @DeleteMapping("/lista/{necId}")
    public ResponseEntity<?> excluirLista(@PathVariable Integer necId) {
        try {
            NecessidadeProduto.excluirLista(necId, conexao());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao excluir lista"));
        }
    }

    // EXCLUIR ITEM
    @DeleteMapping("/item")
    public ResponseEntity<?> excluirItem(@RequestParam Integer necId,
                                         @RequestParam Integer produtoCod) {
        try {
            NecessidadeProduto.excluirItem(necId, produtoCod, conexao());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao excluir item"));
        }
    }
}
