package projeto.salf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projeto.salf.model.CampanhaVoluntario;
import projeto.salf.utils.Mensagem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CampanhaVoluntarioDAO {

    @Autowired
    private Connection connection;

    private static final String INSERT_SQL = "INSERT INTO campanha_voluntario (campanha_id, voluntario_cpf, cargo_campanha) VALUES (?, ?, ?)";
    private static final String DELETE_BY_CAMPANHA_SQL = "DELETE FROM campanha_voluntario WHERE campanha_id = ?";
    private static final String SELECT_BY_CAMPANHA_SQL = "SELECT campanha_id, voluntario_cpf, cargo_campanha FROM campanha_voluntario WHERE campanha_id = ?";

    public Mensagem salvarVoluntarios(Long idCampanha, List<CampanhaVoluntario> voluntarios) {
        // 1. Deleta os vínculos existentes
        try (PreparedStatement psDelete = connection.prepareStatement(DELETE_BY_CAMPANHA_SQL)) {
            psDelete.setLong(1, idCampanha);
            psDelete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro ao limpar vínculos de voluntários existentes: " + e.getMessage(), false);
        }

        // 2. Insere os novos vínculos
        try (PreparedStatement psInsert = connection.prepareStatement(INSERT_SQL)) {
            for (CampanhaVoluntario cv : voluntarios) {
                psInsert.setLong(1, idCampanha);
                psInsert.setString(2, cv.getCpfVoluntario());
                psInsert.setString(3, cv.getCargoCampanha());
                psInsert.addBatch();
            }
            psInsert.executeBatch();
            return new Mensagem("Voluntários vinculados à campanha com sucesso!", true);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro ao vincular voluntários à campanha: " + e.getMessage(), false);
        }
    }

    public List<CampanhaVoluntario> buscarPorCampanha(Long idCampanha) {
        List<CampanhaVoluntario> voluntarios = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CAMPANHA_SQL)) {
            ps.setLong(1, idCampanha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CampanhaVoluntario cv = new CampanhaVoluntario();
                    cv.setIdCampanha(rs.getLong("campanha_id"));
                    cv.setCpfVoluntario(rs.getString("voluntario_cpf"));
                    cv.setCargoCampanha(rs.getString("cargo_campanha"));
                    voluntarios.add(cv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voluntarios;
    }
}
