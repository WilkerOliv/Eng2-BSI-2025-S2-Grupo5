package projeto.salf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.salf.model.Funcionario;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String senha) {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Funcionario funcModel = new Funcionario();
            Funcionario f = funcModel.buscarPorEmail(email, conn);

            if (f == null) {
                return ResponseEntity.status(404).body("Funcionário não encontrado");
            }

            if (!f.getFuncSenha().equals(senha)) {
                return ResponseEntity.status(401).body("Senha incorreta");
            }

            // se você NÃO quiser devolver a senha no JSON:
            f.setFuncSenha(null);

            return ResponseEntity.ok(f);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscaCPF")
    public ResponseEntity<Funcionario> buscarporCpf(@RequestParam String cpf) {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            Funcionario funcModel = new Funcionario();
            Funcionario f = funcModel.buscarPorCPF(cpf, conn);

            return ResponseEntity.ok(f);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
