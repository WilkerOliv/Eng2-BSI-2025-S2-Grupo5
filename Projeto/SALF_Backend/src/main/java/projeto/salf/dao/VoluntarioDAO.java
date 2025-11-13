package projeto.salf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projeto.salf.model.Voluntario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VoluntarioDAO {

    @Autowired
    private Connection connection;

    // Assumindo que a inatividade é marcada pela data_fim_voluntario preenchida
    private static final String SELECT_ATIVOS_SQL = "SELECT cpf, nome, telefone, email, rua, bairro, cidade, uf, cep, tipo_acesso, senha, username, data_inicio_voluntario, data_fim_voluntario FROM voluntario WHERE data_fim_voluntario IS NULL ORDER BY nome";

    public List<Voluntario> buscarAtivos() {
        List<Voluntario> voluntarios = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ATIVOS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                voluntarios.add(mapRowToVoluntario(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voluntarios;
    }

    private Voluntario mapRowToVoluntario(ResultSet rs) throws SQLException {
        Voluntario voluntario = new Voluntario();
        voluntario.setCpf(rs.getString("cpf"));
        voluntario.setNome(rs.getString("nome"));
        voluntario.setTelefone(rs.getString("telefone"));
        voluntario.setEmail(rs.getString("email"));
        voluntario.setRua(rs.getString("rua"));
        voluntario.setBairro(rs.getString("bairro"));
        voluntario.setCidade(rs.getString("cidade"));
        voluntario.setUf(rs.getString("uf"));
        voluntario.setCep(rs.getString("cep"));
        voluntario.setTipoAcesso(rs.getInt("tipo_acesso"));
        voluntario.setSenha(rs.getString("senha"));
        voluntario.setUsername(rs.getString("username"));
        voluntario.setDataInicioVoluntario(rs.getObject("data_inicio_voluntario", LocalDate.class));
        voluntario.setDataFimVoluntario(rs.getObject("data_fim_voluntario", LocalDate.class));
        return voluntario;
    }
}
