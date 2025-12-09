package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.Funcionario;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioRestController {

    private Funcionario.FuncionarioModel criarModel() {
        SingletonDB.conectar();
        Conexao cx = SingletonDB.getConexao();
        return new Funcionario.FuncionarioModel(cx);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Map<String, Object> dados) {
        try {
            var model = criarModel();
            Map<String, Object> resp = model.cadastrar(dados);

            boolean ok = Boolean.TRUE.equals(resp.get("sucesso"));
            return ok ? ResponseEntity.ok(resp)
                    : ResponseEntity.badRequest().body(resp);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("sucesso", false, "mensagem", "Erro ao cadastrar funcionário"));
        } finally {
            SingletonDB.close();
        }
    }

    @PutMapping("/atualizar/{cpf}")
    public ResponseEntity<?> atualizar(@PathVariable String cpf,
                                       @RequestBody Map<String, Object> dados) {
        try {
            var model = criarModel();
            Map<String, Object> resp = model.atualizar(cpf, dados);

            boolean ok = Boolean.TRUE.equals(resp.get("sucesso"));
            return ok ? ResponseEntity.ok(resp)
                    : ResponseEntity.badRequest().body(resp);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("sucesso", false, "mensagem", "Erro ao atualizar funcionário"));
        } finally {
            SingletonDB.close();
        }
    }

    @DeleteMapping("/excluir/{cpf}")
    public ResponseEntity<?> excluir(@PathVariable String cpf) {
        try {
            var model = criarModel();
            Map<String, Object> resp = model.excluir(cpf);

            boolean ok = Boolean.TRUE.equals(resp.get("sucesso"));
            return ok ? ResponseEntity.ok(resp)
                    : ResponseEntity.badRequest().body(resp);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("sucesso", false, "mensagem", "Erro ao excluir funcionário"));
        } finally {
            SingletonDB.close();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        try {
            var model = criarModel();
            List<Map<String, Object>> lista = model.listar();
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("mensagem", "Erro ao listar funcionários"));
        } finally {
            SingletonDB.close();
        }
    }

    @GetMapping("/buscar/{cpf:.+}")
    public ResponseEntity<?> buscar(@PathVariable String cpf) {
        try {
            var model = criarModel();
            Map<String, Object> row = model.buscarCpf(cpf);

            if (row == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("sucesso", false, "mensagem", "Funcionário não encontrado"));
            }

            return ResponseEntity.ok(row);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("mensagem", "Erro ao buscar funcionário"));
        } finally {
            SingletonDB.close();
        }
    }

    @GetMapping("/{cpf:.+}")
    public ResponseEntity<?> buscarCompat(@PathVariable String cpf) {
        return buscar(cpf);
    }
}
