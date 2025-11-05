package projeto.salf.dao;

import org.springframework.stereotype.Repository;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Date;

@Repository
public class EstoqueDAO {


    public boolean insereItensEstoque(int quantidade, LocalDate validade, int produtoCod) {
        final String SQL = "INSERT INTO estoque (est_prod_quantidade, produto_prod_cod, data_validade) VALUES (?,?,?)";
        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoCod);
            if (validade != null) {
                stmt.setDate(3, java.sql.Date.valueOf(validade));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
