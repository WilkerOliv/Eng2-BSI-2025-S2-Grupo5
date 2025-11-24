package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.AcertoEstoque;

import java.util.Map;

@RestController
@RequestMapping("/api/acertos-estoque")
@CrossOrigin(origins = "*")
public class AcertoEstoqueController {

    private Conexao conexao() {
        return SingletonDB.getConexao();
    }

    // Registrar múltiplos acertos
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody AcertoEstoque.AcertoDTO dto) {
        try {
            AcertoEstoque.registrar(dto, conexao());
            return ResponseEntity.ok(Map.of("mensagem", "Acertos registrados com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao registrar acertos."));
        }
    }

    // Lista agrupada por ID
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(AcertoEstoque.listarAgrupado(conexao()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao listar acertos."));
        }
    }

    // Itens de um acerto
    @GetMapping("/{idAcerto}")
    public ResponseEntity<?> listarItens(@PathVariable Integer idAcerto) {
        try {
            return ResponseEntity.ok(AcertoEstoque.listarItens(idAcerto, conexao()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao listar itens."));
        }
    }
}
