package projeto.salf.dao;

import org.springframework.stereotype.Repository;
import projeto.salf.model.Funcionario;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class FuncionarioDAO {

    public Funcionario buscaFuncEmail(String email) {
        String SQL = "SELECT func_nome, func_email, func_senha FROM funcionario WHERE func_email = ?";
        try (Connection con = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = con.prepareStatement(SQL)) {

            stmt.setString(1, email.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario func = new Funcionario();
                    func.setFuncNome(rs.getString("func_nome"));
                    func.setFuncEmail(rs.getString("func_email"));
                    func.setFuncSenha(rs.getString("func_senha"));
                    return func;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // ideal: trocar por log
        }
        return null;
    }


}
