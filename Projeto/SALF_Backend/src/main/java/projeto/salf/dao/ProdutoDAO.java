package projeto.salf.dao;


import org.springframework.stereotype.Repository;
import projeto.salf.model.Produto;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {

    public List<Produto> getListaAll(){
        String SQL = "SELECT prod_cod, prod_descr FROM produto order by prod_descr asc";
        List<Produto> listaProduto = new ArrayList<>();
        try {
            Connection conn = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = conn.prepareStatement(SQL);
            try
            {
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    Produto produto = new Produto();
                    produto.setProdCod(rs.getInt("prod_cod"));
                    produto.setProdDescr(rs.getString("prod_descr"));
                    listaProduto.add(produto);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return listaProduto;

    }
}
