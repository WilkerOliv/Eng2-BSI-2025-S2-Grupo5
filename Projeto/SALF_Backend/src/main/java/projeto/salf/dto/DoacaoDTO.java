package projeto.salf.dto;

import java.util.List;

public class DoacaoDTO {
    private Integer doaCod;
    private String dataDoacao;
    private String observacao;
    private List<Integer> produtos;

    public Integer getDoaCod() {
        return doaCod;
    }

    public void setDoaCod(Integer doaCod) {
        this.doaCod = doaCod;
    }

    public String getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(String dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<Integer> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Integer> produtos) {
        this.produtos = produtos;
    }
}

