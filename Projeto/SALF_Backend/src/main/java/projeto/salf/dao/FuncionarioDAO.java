package projeto.salf.dao;

import projeto.salf.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FuncionarioDAO {

    public Funcionario buscaFuncEmail(String email, Connection conn) {
        String SQL = "SELECT func_nome, func_email, func_senha FROM funcionario WHERE func_email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, email.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario func = new Funcionario();
                    func.setFuncNome(rs.getString("func_nome"));
                    func.setFuncEmail(rs.getString("func_email"));
                    // usa func_senha APENAS aqui, como no seu código original, pra validar login
                    func.setFuncSenha(rs.getString("func_senha"));
                    return func;
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // se quiser, depois troca por log
        }

        return null;
    }

    public Funcionario buscaCPF(String cpf, Connection conn) {
        String SQL = "SELECT func_nome FROM funcionario WHERE func_cpf = ?";

        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, cpf.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario func = new Funcionario();
                    func.setFuncNome(rs.getString("func_nome"));
                    return func;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
