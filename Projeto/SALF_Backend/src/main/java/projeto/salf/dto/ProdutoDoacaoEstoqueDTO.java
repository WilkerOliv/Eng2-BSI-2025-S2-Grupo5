package projeto.salf.dto;

import projeto.salf.model.DoacaoProduto;
import projeto.salf.model.Estoque;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDoacaoEstoqueDTO {

    private List<Estoque> estoques = new ArrayList<>();
    private DoacaoProduto doacaoProduto;

    public ProdutoDoacaoEstoqueDTO() {
    }

    public ProdutoDoacaoEstoqueDTO(List<Estoque> estoques, DoacaoProduto doacaoProduto) {
        this.estoques = estoques;
        this.doacaoProduto = doacaoProduto;
    }

    public List<Estoque> getEstoques() {
        return estoques;
    }

    public void setEstoques(List<Estoque> estoques) {
        this.estoques = estoques;
    }

    public DoacaoProduto getDoacaoProduto() {
        return doacaoProduto;
    }

    public void setDoacaoProduto(DoacaoProduto doacaoProduto) {
        this.doacaoProduto = doacaoProduto;
    }
}
