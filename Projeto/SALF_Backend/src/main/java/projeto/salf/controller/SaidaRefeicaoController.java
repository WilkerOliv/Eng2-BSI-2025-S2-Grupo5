package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.SaidaRefeicao;

import java.util.Map;

@RestController
@RequestMapping("/api/saidas-refeicao")
@CrossOrigin(origins = "*")
public class SaidaRefeicaoController {

    private Conexao conexao() {
        return SingletonDB.getConexao();
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody SaidaRefeicao.SaidaDTO dto) {

        try {
            Integer id = SaidaRefeicao.registrar(dto, conexao());
            return ResponseEntity.ok(Map.of("idSaida", id));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao registrar saída."));
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(SaidaRefeicao.listarSaidas(conexao()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao listar saídas."));
        }
    }

    @GetMapping("/{idSaida}/itens")
    public ResponseEntity<?> listarItens(@PathVariable Integer idSaida) {
        try {
            return ResponseEntity.ok(SaidaRefeicao.listarItens(idSaida, conexao()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Falha ao listar itens da saída."));
        }
    }
}
