package projeto.salf.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/* Estoque */
@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "est_cod")
    private Integer estCod;

    @Column(name = "est_prodquantidade", nullable = false)
    private Integer estProdQuantidade;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @ManyToOne
    @JoinColumn(name = "produto_prod_cod", nullable = false)
    private Produto produto;

    public Estoque() {}

    public Estoque(Integer estCod, Integer estProdQuantidade, LocalDate dataValidade, Produto produto) {
        this.estCod = estCod;
        this.estProdQuantidade = estProdQuantidade;
        this.dataValidade = dataValidade;
        this.produto = produto;
    }

    public Integer getEstCod() { return estCod; }
    public void setEstCod(Integer estCod) { this.estCod = estCod; }

    public Integer getEstProdQuantidade() { return estProdQuantidade; }
    public void setEstProdQuantidade(Integer estProdQuantidade) { this.estProdQuantidade = estProdQuantidade; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}