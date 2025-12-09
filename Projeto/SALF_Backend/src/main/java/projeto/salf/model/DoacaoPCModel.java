package projeto.salf.model;

import projeto.salf.dao.DoacaoPCDAO;

import java.sql.Connection;
import java.util.Map;

public class DoacaoPCModel {

    public Map<String, Object> registrar(Map<String, Object> payload, Connection conn) {
        DoacaoPCDAO dao = new DoacaoPCDAO();
        return dao.registrarDoacao(payload, conn);
    }
}
