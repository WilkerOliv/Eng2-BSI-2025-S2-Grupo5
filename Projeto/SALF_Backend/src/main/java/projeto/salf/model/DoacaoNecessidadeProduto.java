//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "doacao_necessidade_produto")
//@IdClass(DoacaoNecessidadeProdutoId.class)
//public class DoacaoNecessidadeProduto implements Serializable {
//
//    @Id
//    @Column(name = "doacao_doa_cod")
//    private Integer doacaoDoaCod;
//
//    @Id
//    @Column(name = "necessidade_produto_pessoa_carente_pc_cpf", length = 14)
//    private String necessidadeProdutoPessoaCarentePcCpf;
//
//    @Id
//    @Column(name = "necessidade_produto_produto_prod_cod")
//    private Integer necessidadeProdutoProdutoProdCod;
//
//    @Column(name = "quantidade", nullable = false)
//    private Integer quantidade;
//
//    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
//    public void setDoacaoDoaCod(Integer doacaoDoaCod) { this.doacaoDoaCod = doacaoDoaCod; }
//
//    public String getNecessidadeProdutoPessoaCarentePcCpf() { return necessidadeProdutoPessoaCarentePcCpf; }
//    public void setNecessidadeProdutoPessoaCarentePcCpf(String v) { this.necessidadeProdutoPessoaCarentePcCpf = v; }
//
//    public Integer getNecessidadeProdutoProdutoProdCod() { return necessidadeProdutoProdutoProdCod; }
//    public void setNecessidadeProdutoProdutoProdCod(Integer v) { this.necessidadeProdutoProdutoProdCod = v; }
//
//    public Integer getQuantidade() { return quantidade; }
//    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
//}
