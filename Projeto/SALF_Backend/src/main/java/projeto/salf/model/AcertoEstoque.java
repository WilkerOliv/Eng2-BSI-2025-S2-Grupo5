package projeto.salf.model;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.AcertoEstoqueDAO;

import java.time.LocalDate;
import java.util.List;

public class AcertoEstoque {

    private Integer idAcerto;
    private String funcionarioCpf;
    private LocalDate data;
    private String motivo;

    private Integer estCod;
    private Integer quantidadeAnterior;
    private Integer novaQuantidade;
    private String produtoNome;
    private LocalDate validade;

    // ========================= DTO PRINCIPAL =========================
    public static class AcertoDTO {
        public String funcionarioCpf;
        public List<ItemDTO> itens;
        public String dataAcerto;
    }

    public static class ItemDTO {
        public Integer estCod;
        public Integer quantidadeAnterior;
        public Integer novaQuantidade;
        public String motivo;
    }

    // ========================= DAO =========================
    private static AcertoEstoqueDAO getDAO() {
        return new AcertoEstoqueDAO();
    }

    // ========================= OPERAÇÕES =========================

    public static void registrar(AcertoDTO dto, Conexao c) {
        getDAO().registrar(dto, c);
    }

    public static List<AcertoEstoque> listarAgrupado(Conexao c) {
        return getDAO().listarAgrupado(c);
    }

    public static List<AcertoEstoque> listarItens(Integer idAcerto, Conexao c) {
        return getDAO().listarItens(idAcerto, c);
    }

    // ========================= GET/SET =========================

    public Integer getIdAcerto() { return idAcerto; }
    public void setIdAcerto(Integer idAcerto) { this.idAcerto = idAcerto; }

    public String getFuncionarioCpf() { return funcionarioCpf; }
    public void setFuncionarioCpf(String funcionarioCpf) { this.funcionarioCpf = funcionarioCpf; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Integer getEstCod() { return estCod; }
    public void setEstCod(Integer estCod) { this.estCod = estCod; }

    public Integer getQuantidadeAnterior() { return quantidadeAnterior; }
    public void setQuantidadeAnterior(Integer quantidadeAnterior) { this.quantidadeAnterior = quantidadeAnterior; }

    public Integer getNovaQuantidade() { return novaQuantidade; }
    public void setNovaQuantidade(Integer novaQuantidade) { this.novaQuantidade = novaQuantidade; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
}
