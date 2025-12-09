package projeto.salf.model;

import projeto.salf.dao.CestaBasicaDAO;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class CestaBasicaModel {

    private Integer cbCod;
    private String descricao;

    public Integer getCbCod() { return cbCod; }
    public void setCbCod(Integer cbCod) { this.cbCod = cbCod; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @SuppressWarnings("unchecked")
    public Map<String, Object> registrar(Map<String, Object> dados, Connection conn) {

        String descricao = toStr(dados.get("descricao"));
        List<Map<String, Object>> itens =
                (List<Map<String, Object>>) dados.get("itens");

        if (descricao == null || descricao.isBlank()) {
            return Map.of("sucesso", false, "mensagem", "Descrição da cesta é obrigatória");
        }

        if (itens == null || itens.isEmpty()) {
            return Map.of("sucesso", false, "mensagem", "Nenhum item informado");
        }

        try {
            CestaBasicaDAO dao = new CestaBasicaDAO();
            boolean ok = dao.registrarCesta(descricao, itens, conn);

            if (!ok) {
                return Map.of("sucesso", false, "mensagem", "Falha ao registrar a cesta");
            }

            return Map.of("sucesso", true, "mensagem", "Cesta registrada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("sucesso", false, "mensagem", "Erro inesperado");
        }
    }

    public List<Map<String, Object>> listar(Connection conn) {
        CestaBasicaDAO dao = new CestaBasicaDAO();
        return dao.listarCestas(conn);
    }

    public List<Map<String, Object>> listarItens(Integer cbCod, Connection conn) {
        if (cbCod == null || cbCod <= 0) return List.of();
        CestaBasicaDAO dao = new CestaBasicaDAO();
        return dao.listarItensDaCesta(cbCod, conn);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> atualizarItens(Integer id, Map<String, Object> dados, Connection conn) {

        if (id == null || id <= 0) {
            return Map.of("sucesso", false, "mensagem", "Código da cesta inválido");
        }

        List<Map<String, Object>> itens =
                (List<Map<String, Object>>) dados.get("itens");

        if (itens == null || itens.isEmpty()) {
            return Map.of("sucesso", false, "mensagem", "A cesta precisa ter pelo menos um item");
        }

        try {
            CestaBasicaDAO dao = new CestaBasicaDAO();
            boolean ok = dao.substituirItensCesta(id, itens, conn);

            if (!ok) {
                return Map.of("sucesso", false, "mensagem", "Falha ao atualizar itens da cesta");
            }

            return Map.of("sucesso", true, "mensagem", "Itens da cesta atualizados com sucesso");

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("sucesso", false, "mensagem", "Erro inesperado ao atualizar itens da cesta");
        }
    }

    public Map<String, Object> excluir(Integer id, Connection conn) {
        if (id == null || id <= 0)
            return Map.of("sucesso", false, "mensagem", "Código inválido");

        try {
            CestaBasicaDAO dao = new CestaBasicaDAO();
            boolean ok = dao.excluirCesta(id, conn);

            if (!ok) {
                return Map.of("sucesso", false, "mensagem", "Falha ao excluir");
            }

            return Map.of("sucesso", true, "mensagem", "Cesta excluída com sucesso");

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("sucesso", false, "mensagem", "Erro inesperado");
        }
    }

    public Map<String, Object> alterarDescricao(Integer id, Map<String, Object> dados, Connection conn) {

        String nova = toStr(dados.get("descricao"));
        if (nova == null || nova.isBlank())
            return Map.of("sucesso", false, "mensagem", "Descrição obrigatória");

        try {
            CestaBasicaDAO dao = new CestaBasicaDAO();
            boolean ok = dao.atualizarDescricao(id, nova, conn);

            if (!ok) {
                return Map.of("sucesso", false, "mensagem", "Falha ao alterar");
            }

            return Map.of("sucesso", true, "mensagem", "Atualizado com sucesso");

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("sucesso", false, "mensagem", "Erro inesperado");
        }
    }

    private static String toStr(Object o) {
        return o != null ? o.toString() : null;
    }
}
