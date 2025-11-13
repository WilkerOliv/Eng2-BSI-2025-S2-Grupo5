package projeto.salf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projeto.salf.model.Campanha;
import projeto.salf.utils.Mensagem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CampanhaDAO {

    @Autowired
    private Connection connection;

    private static final String INSERT_SQL = "INSERT INTO campanha (nome, data_inicio, data_fim, observacao, status, ativo) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE campanha SET nome = ?, data_inicio = ?, data_fim = ?, observacao = ?, status = ?, ativo = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, nome, data_inicio, data_fim, observacao, status, ativo FROM campanha WHERE ativo = TRUE ORDER BY data_inicio DESC";
    private static final String SELECT_BY_ID_SQL = "SELECT id, nome, data_inicio, data_fim, observacao, status, ativo FROM campanha WHERE id = ?";
    private static final String INACTIVATE_SQL = "UPDATE campanha SET ativo = FALSE WHERE id = ?";
    private static final String UPDATE_STATUS_SQL = "UPDATE campanha SET status = ? WHERE id = ?";
    private static final String SELECT_FINALIZADAS_SQL = "SELECT id, nome, data_inicio, data_fim, observacao, status, ativo FROM campanha WHERE ativo = TRUE AND status = 'Finalizada' ORDER BY data_inicio DESC";

    public Mensagem salvar(Campanha campanha) {
        if (campanha.getId() == null) {
            return inserir(campanha);
        } else {
            return atualizar(campanha);
        }
    }

    private Mensagem inserir(Campanha campanha) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, campanha.getNome());
            ps.setObject(2, campanha.getDataInicio());
            ps.setObject(3, campanha.getDataFim());
            ps.setString(4, campanha.getObservacao());
            ps.setString(5, campanha.getStatus() != null ? campanha.getStatus() : "Em Andamento");
            ps.setBoolean(6, true);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return new Mensagem("Falha ao inserir campanha, nenhuma linha afetada.", false);
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    campanha.setId(generatedKeys.getLong(1));
                } else {
                    return new Mensagem("Falha ao obter ID da campanha inserida.", false);
                }
            }
            return new Mensagem("Campanha '" + campanha.getNome() + "' inserida com sucesso!", true);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao inserir campanha: " + e.getMessage(), false);
        }
    }

    private Mensagem atualizar(Campanha campanha) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, campanha.getNome());
            ps.setObject(2, campanha.getDataInicio());
            ps.setObject(3, campanha.getDataFim());
            ps.setString(4, campanha.getObservacao());
            ps.setString(5, campanha.getStatus());
            ps.setBoolean(6, campanha.isAtivo());
            ps.setLong(7, campanha.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return new Mensagem("Campanha '" + campanha.getNome() + "' atualizada com sucesso!", true);
            } else {
                return new Mensagem("Campanha com ID " + campanha.getId() + " não encontrada para atualização.", false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao atualizar campanha: " + e.getMessage(), false);
        }
    }

    public List<Campanha> buscarTodos() {
        List<Campanha> campanhas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                campanhas.add(mapRowToCampanha(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campanhas;
    }

    public List<Campanha> buscarFinalizadas() {
        List<Campanha> campanhas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_FINALIZADAS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                campanhas.add(mapRowToCampanha(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campanhas;
    }

    public Campanha buscarPorId(Long id) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCampanha(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Mensagem inativar(Long id) {
        try (PreparedStatement ps = connection.prepareStatement(INACTIVATE_SQL)) {
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return new Mensagem("Campanha inativada com sucesso!", true);
            } else {
                return new Mensagem("Campanha com ID " + id + " não encontrada para inativação.", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao inativar campanha: " + e.getMessage(), false);
        }
    }

    public Mensagem atualizarStatus(Long id, String status) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS_SQL)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return new Mensagem("Status da Campanha atualizado para " + status + " com sucesso!", true);
            } else {
                return new Mensagem("Campanha com ID " + id + " não encontrada para atualizar status.", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao atualizar status da campanha: " + e.getMessage(), false);
        }
    }

    private Campanha mapRowToCampanha(ResultSet rs) throws SQLException {
        Campanha campanha = new Campanha();
        campanha.setId(rs.getLong("id"));
        campanha.setNome(rs.getString("nome"));
        campanha.setDataInicio(rs.getObject("data_inicio", LocalDate.class));
        campanha.setDataFim(rs.getObject("data_fim", LocalDate.class));
        campanha.setObservacao(rs.getString("observacao"));
        campanha.setStatus(rs.getString("status"));
        campanha.setAtivo(rs.getBoolean("ativo"));
        return campanha;
    }
}
