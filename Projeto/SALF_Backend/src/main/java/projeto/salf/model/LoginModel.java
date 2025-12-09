package projeto.salf.model;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.FuncionarioDAO;
import projeto.salf.model.Funcionario;

import java.util.Map;

public class LoginModel {

    private final Conexao conexao;
    private final FuncionarioDAO dao;

    public LoginModel(Conexao conexao) {
        this.conexao = conexao;
        this.dao = new FuncionarioDAO(conexao);
    }

    public Map<String, Object> login(String email, String senha) {

        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            return Map.of("sucesso", false, "mensagem", "Dados inválidos");
        }

        Funcionario f = dao.buscarPorEmail(email);

        if (f == null) {
            return Map.of("sucesso", false, "mensagem", "Funcionário não encontrado");
        }

        if (!senha.equals(f.getFuncSenha())) {
            return Map.of("sucesso", false, "mensagem", "Senha incorreta");
        }

        if (f.getDataDemissao() != null) {
            return Map.of(
                    "sucesso", false,
                    "mensagem", "Seu acesso foi desativado."
            );
        }

        return Map.of(
                "sucesso", true,
                "mensagem", "Login realizado com sucesso",
                "func", Map.of(
                        "cpf", f.getFuncCpf(),
                        "nome", f.getFuncNome(),
                        "email", f.getFuncEmail(),
                        "tipoAcesso", f.getTipoAcesso()
                )
        );
    }
}
