package projeto.salf.dao;

import projeto.salf.model.Cotacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CotacaoDAO {

    public List<Cotacao> getCotacao(Connection conn) {

        String SQL = "SELECT * FROM cotacao";
        List<Cotacao> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cotacao c = new Cotacao();
                c.setIdCotacao(rs.getInt("id_cotacao"));

                java.sql.Date da = rs.getDate("data_abertura");
                java.sql.Date df = rs.getDate("data_fechamento");

                c.setDataAbertura(da != null ? da.toLocalDate() : null);
                c.setDataFechamento(df != null ? df.toLocalDate() : null);

                lista.add(c);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
}
