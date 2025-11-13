package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.PessoaCarente;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas-carentes")
@CrossOrigin(origins = "*")
public class PessoaCarenteController {

    private void garantirConexao() {
        SingletonDB.getConexao();
    }

    @GetMapping
    public ResponseEntity<List<PessoaCarente>> listarTodos() {
        try {
            garantirConexao();
            List<PessoaCarente> lista = PessoaCarente.listarTodos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<PessoaCarente> buscarPorCpf(@PathVariable String cpf) {
        try {
            garantirConexao();
            PessoaCarente p = PessoaCarente.buscarPorCpf(cpf);
            if (p == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Autocomplete para a tela de necessidades.
    @GetMapping("/busca")
    public ResponseEntity<List<PessoaCarente>> buscarPorCpfOuNome(@RequestParam("termo") String termo) {
        try {
            garantirConexao();
            List<PessoaCarente> lista = PessoaCarente.buscarPorCpfOuNome(termo);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
