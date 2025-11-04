package projeto.salf.dao;

import org.springframework.stereotype.Repository;
import projeto.salf.model.Fornecedor;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FornecedorDAO {

    public List<Fornecedor> getAll(){
        String SQL = "SELECT id_fornecedor, nome FROM fornecedor";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Fornecedor> fornecedores = new ArrayList<>();
        try{
            Connection conn = SingletonDB.getConexao().getConnect();
            stmt = conn.prepareStatement(SQL);
            rs = stmt.executeQuery();
            while(rs.next()){
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setNome(rs.getString("nome"));
                fornecedores.add(fornecedor);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return fornecedores;
    }

    public Map<Integer, List<Fornecedor>> getListaAllCotacao() {
        String SQL = """
        SELECT fc.cotacao_id, f.id_fornecedor, f.nome, f.email, f.telefone, f.contato, f.descricao
        FROM fornec_cotacao fc
        JOIN fornecedor f ON f.id_fornecedor = fc.fornecedor_id
        ORDER BY fc.cotacao_id
    """;

        Map<Integer, List<Fornecedor>> fornecedoresCotacao = new HashMap<>();

        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int cotacaoId = rs.getInt("cotacao_id");

                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setNome(rs.getString("nome"));
                fornecedor.setEmail(rs.getString("email"));
                fornecedor.setTelefone(rs.getString("telefone"));
                fornecedor.setContato(rs.getString("contato"));
                fornecedor.setDescricao(rs.getString("descricao"));

                // agrupa os fornecedores dentro da cotação
                fornecedoresCotacao
                        .computeIfAbsent(cotacaoId, k -> new ArrayList<>())
                        .add(fornecedor);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return fornecedoresCotacao;
    }

}
