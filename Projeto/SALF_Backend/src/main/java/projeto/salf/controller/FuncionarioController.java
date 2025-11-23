package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.Funcionario;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private Conexao conexao() {
        return SingletonDB.getConexao();
    }

    // Lista todos
    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        try {

            List<Funcionario> lista = Funcionario.listarTodos(conexao());
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Busca funcionário pelo CPF exato.
    @GetMapping("/{cpf}")
    public ResponseEntity<Funcionario> buscarPorCpf(@PathVariable String cpf) {
        try {

            Funcionario f = Funcionario.buscarPorCpf(cpf, conexao());
            if (f == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(f);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // busca por CPF ou nome contendo o termo.
    @GetMapping("/busca")
    public ResponseEntity<List<Funcionario>> buscarPorCpfOuNome(@RequestParam("termo") String termo) {
        try {
            List<Funcionario> lista = Funcionario.buscarPorCpfOuNome(termo, conexao());
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
