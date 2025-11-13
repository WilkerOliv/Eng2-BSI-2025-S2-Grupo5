package projeto.salf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projeto.salf.model.ResultadoCampanha;
import projeto.salf.utils.Mensagem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class ResultadoCampanhaDAO {

    @Autowired
    private Connection connection;

    private static final String INSERT_SQL = "INSERT INTO resultado_campanha (campanha_id, valor_arrecadado, familias_atendidas, produtos_arrecadados, observacao, data_registro) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_CAMPANHA_ID_SQL = "SELECT id, campanha_id, valor_arrecadado, familias_atendidas, produtos_arrecadados, observacao, data_registro FROM resultado_campanha WHERE campanha_id = ?";

    public Mensagem salvar(ResultadoCampanha resultado) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, resultado.getIdCampanha());
            ps.setDouble(2, resultado.getValorArrecadado());
            ps.setInt(3, resultado.getFamiliasAtendidas());
            ps.setInt(4, resultado.getProdutosArrecadados());
            ps.setString(5, resultado.getObservacao());
            ps.setObject(6, LocalDate.now()); // Data de registro é a data atual

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return new Mensagem("Falha ao registrar resultado da campanha, nenhuma linha afetada.", false);
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    resultado.setId(generatedKeys.getLong(1));
                }
            }
            return new Mensagem("Resultado da campanha registrado com sucesso!", true);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao registrar resultado da campanha: " + e.getMessage(), false);
        }
    }

    public ResultadoCampanha buscarPorCampanhaId(Long idCampanha) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CAMPANHA_ID_SQL)) {
            ps.setLong(1, idCampanha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToResultadoCampanha(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResultadoCampanha mapRowToResultadoCampanha(ResultSet rs) throws SQLException {
        ResultadoCampanha resultado = new ResultadoCampanha();
        resultado.setId(rs.getLong("id"));
        resultado.setIdCampanha(rs.getLong("campanha_id"));
        resultado.setValorArrecadado(rs.getDouble("valor_arrecadado"));
        resultado.setFamiliasAtendidas(rs.getInt("familias_atendidas"));
        resultado.setProdutosArrecadados(rs.getInt("produtos_arrecadados"));
        resultado.setObservacao(rs.getString("observacao"));
        resultado.setDataRegistro(rs.getObject("data_registro", LocalDate.class));
        return resultado;
    }
}
