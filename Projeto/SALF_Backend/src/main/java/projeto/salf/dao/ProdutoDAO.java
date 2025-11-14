package projeto.salf.dao;

import projeto.salf.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public List<Produto> getListaAll(Connection conn) {
        String SQL = "SELECT prod_cod, prod_descr FROM produto ORDER BY prod_descr ASC";
        List<Produto> lista = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();
                p.setProdCod(rs.getInt("prod_cod"));
                p.setProdDescr(rs.getString("prod_descr"));
                lista.add(p);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

}
