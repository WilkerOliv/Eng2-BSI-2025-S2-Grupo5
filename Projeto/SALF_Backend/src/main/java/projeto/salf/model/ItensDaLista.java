package projeto.salf.model;

public class ItensDaLista {
    private Integer produtoProdCod;
    private Integer listaLcCod;
    private String listaFuncionarioCpf;
    private Integer quantidade;

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }

    public Integer getListaLcCod() { return listaLcCod; }
    public void setListaLcCod(Integer listaLcCod) { this.listaLcCod = listaLcCod; }

    public String getListaFuncionarioCpf() { return listaFuncionarioCpf; }
    public void setListaFuncionarioCpf(String listaFuncionarioCpf) { this.listaFuncionarioCpf = listaFuncionarioCpf; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
