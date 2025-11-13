package projeto.salf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projeto.salf.model.Fornecedor;
import projeto.salf.utils.Mensagem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FornecedorDAO {

    @Autowired
    private Connection connection; // Assumindo que a conexão é injetada (DataSource ou similar)

    private static final String INSERT_SQL = "INSERT INTO fornecedor (nome, cnpj, telefone, email, contato, descricao, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE fornecedor SET nome = ?, cnpj = ?, telefone = ?, email = ?, contato = ?, descricao = ?, ativo = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, nome, cnpj, telefone, email, contato, descricao, ativo FROM fornecedor WHERE ativo = TRUE ORDER BY nome";
    private static final String SELECT_BY_ID_SQL = "SELECT id, nome, cnpj, telefone, email, contato, descricao, ativo FROM fornecedor WHERE id = ?";
    private static final String INACTIVATE_SQL = "UPDATE fornecedor SET ativo = FALSE WHERE id = ?";

    public Mensagem salvar(Fornecedor fornecedor) {
        if (fornecedor.getId() == null) {
            return inserir(fornecedor);
        } else {
            return atualizar(fornecedor);
        }
    }

    private Mensagem inserir(Fornecedor fornecedor) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getCnpj());
            ps.setString(3, fornecedor.getTelefone());
            ps.setString(4, fornecedor.getEmail());
            ps.setString(5, fornecedor.getContato());
            ps.setString(6, fornecedor.getDescricao());
            ps.setBoolean(7, true); // Sempre ativo ao inserir

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return new Mensagem("Falha ao inserir fornecedor, nenhuma linha afetada.", false);
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    fornecedor.setId(generatedKeys.getLong(1));
                } else {
                    return new Mensagem("Falha ao obter ID do fornecedor inserido.", false);
                }
            }
            return new Mensagem("Fornecedor " + fornecedor.getNome() + " inserido com sucesso!", true);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao inserir fornecedor: " + e.getMessage(), false);
        }
    }

    private Mensagem atualizar(Fornecedor fornecedor) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getCnpj());
            ps.setString(3, fornecedor.getTelefone());
            ps.setString(4, fornecedor.getEmail());
            ps.setString(5, fornecedor.getContato());
            ps.setString(6, fornecedor.getDescricao());
            ps.setBoolean(7, fornecedor.isAtivo());
            ps.setLong(8, fornecedor.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return new Mensagem("Fornecedor " + fornecedor.getNome() + " atualizado com sucesso!", true);
            } else {
                return new Mensagem("Fornecedor com ID " + fornecedor.getId() + " não encontrado para atualização.", false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao atualizar fornecedor: " + e.getMessage(), false);
        }
    }

    public List<Fornecedor> buscarTodos() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                fornecedores.add(mapRowToFornecedor(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Em um sistema real, logar o erro e retornar uma lista vazia ou lançar exceção de serviço
        }
        return fornecedores;
    }

    public Fornecedor buscarPorId(Long id) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToFornecedor(rs);
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
                return new Mensagem("Fornecedor inativado com sucesso!", true);
            } else {
                return new Mensagem("Fornecedor com ID " + id + " não encontrado para inativação.", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Mensagem("Erro de banco de dados ao inativar fornecedor: " + e.getMessage(), false);
        }
    }

    private Fornecedor mapRowToFornecedor(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getLong("id"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setCnpj(rs.getString("cnpj"));
        fornecedor.setTelefone(rs.getString("telefone"));
        fornecedor.setEmail(rs.getString("email"));
        fornecedor.setContato(rs.getString("contato"));
        fornecedor.setDescricao(rs.getString("descricao"));
        fornecedor.setAtivo(rs.getBoolean("ativo"));
        return fornecedor;
    }
}
