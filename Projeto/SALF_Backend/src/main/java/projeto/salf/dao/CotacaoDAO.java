package projeto.salf.dao;

import org.springframework.stereotype.Service;
import projeto.salf.model.Cotacao;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class CotacaoDAO {


    public List<Cotacao> getCotacao() {
        String SQL = "SELECT * FROM cotacao";

        List<Cotacao> lista = new ArrayList<>();
        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cotacao c = new Cotacao();
                c.setIdCotacao(rs.getInt("id_cotacao"));              // <- nomes do BD
                // Se seu atributo é LocalDate:
                java.sql.Date da = rs.getDate("data_abertura");
                java.sql.Date df = rs.getDate("data_fechamento");
                c.setDataAbertura(da != null ? da.toLocalDate() : null);
                c.setDataFechamento(df != null ? df.toLocalDate() : null);

                // (Se os campos da sua classe forem java.util.Date, pode usar rs.getDate direto.)
                lista.add(c);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

}
