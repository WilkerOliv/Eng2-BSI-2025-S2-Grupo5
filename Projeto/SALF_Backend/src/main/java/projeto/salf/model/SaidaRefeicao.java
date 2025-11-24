package projeto.salf.model;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.SaidaRefeicaoDAO;
import java.time.LocalDate;
import java.util.List;

public class SaidaRefeicao {

    private Integer idSaida;
    private LocalDate dataRegistro;
    private String funcionarioCpf;
    private String observacao;

    // ========== DTOs ==========
    public static class ItemDTO {
        public Integer estCod;
        public Integer quantidade;
        public String produtoNome;
        public LocalDate validade;
    }

    public static class SaidaDTO {
        public String funcionarioCpf;
        public String observacao;
        public List<ItemDTO> itens;
    }

    private static SaidaRefeicaoDAO getDAO() {
        return new SaidaRefeicaoDAO();
    }

    // ========== OPERAÇÕES PRINCIPAIS ==========

    public static Integer registrar(SaidaDTO dto, Conexao c) {

        if (dto.funcionarioCpf == null || dto.funcionarioCpf.isBlank())
            throw new IllegalArgumentException("CPF do funcionário é obrigatório.");

        if (dto.itens == null || dto.itens.isEmpty())
            throw new IllegalArgumentException("A saída deve possuir itens.");

        // Chama DAO
        return getDAO().registrar(dto, c);
    }

    public static List<SaidaRefeicao> listarSaidas(Conexao c) {
        return getDAO().listarSaidas(c);
    }

    public static List<ItemDTO> listarItens(Integer idSaida, Conexao c) {
        return getDAO().listarItens(idSaida, c);
    }

    // Getters e setters
    public Integer getIdSaida() { return idSaida; }
    public void setIdSaida(Integer idSaida) { this.idSaida = idSaida; }

    public LocalDate getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDate dataRegistro) { this.dataRegistro = dataRegistro; }

    public String getFuncionarioCpf() { return funcionarioCpf; }
    public void setFuncionarioCpf(String funcionarioCpf) { this.funcionarioCpf = funcionarioCpf; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
