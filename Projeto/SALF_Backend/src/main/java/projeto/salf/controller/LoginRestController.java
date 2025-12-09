package projeto.salf.controller;

import org.springframework.web.bind.annotation.*;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.model.LoginModel;

import java.util.Map;

@RestController
@RequestMapping("/apis/login")
@CrossOrigin(origins = "*")
public class LoginRestController {

    @PostMapping
    public Map<String, Object> login(@RequestBody Map<String, String> dados) {

        String email = dados.get("email");
        String senha = dados.get("senha");

        if (!SingletonDB.conectar()) {
            return Map.of("sucesso", false, "mensagem", "Falha ao conectar ao banco");
        }
        Conexao cx = SingletonDB.getConexao();

        try {
            LoginModel model = new LoginModel(cx);
            return model.login(email, senha);

        } finally {
            SingletonDB.close();
        }
    }
}
