package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.Funcionario;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private void garantirConexao() {
        // A professora pediu: control só verifica e, se não houver, Singleton abre.
        SingletonDB.getConexao();
    }

    // Lista todos (se quiser usar como lista inicial).
    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        try {
            garantirConexao();
            List<Funcionario> lista = Funcionario.listarTodos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Busca funcionário pelo CPF exato.
    @GetMapping("/{cpf}")
    public ResponseEntity<Funcionario> buscarPorCpf(@PathVariable String cpf) {
        try {
            garantirConexao();
            Funcionario f = Funcionario.buscarPorCpf(cpf);
            if (f == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(f);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Autocomplete: busca por CPF ou nome contendo o termo.
    @GetMapping("/busca")
    public ResponseEntity<List<Funcionario>> buscarPorCpfOuNome(@RequestParam("termo") String termo) {
        try {
            garantirConexao();
            List<Funcionario> lista = Funcionario.buscarPorCpfOuNome(termo);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
