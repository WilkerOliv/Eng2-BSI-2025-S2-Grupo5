//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "doacao_necessidade_cesta")
//@IdClass(DoacaoNecessidadeCestaId.class)
//public class DoacaoNecessidadeCesta implements Serializable {
//
//    @Id
//    @Column(name = "doacao_doa_cod")
//    private Integer doacaoDoaCod;
//
//    @Id
//    @Column(name = "necessidade_cesta_pessoa_carente_pc_cpf", length = 14)
//    private String necessidadeCestaPessoaCarentePcCpf;
//
//    @Id
//    @Column(name = "necessidade_cesta_cesta_basica_cb_cod")
//    private Integer necessidadeCestaCestaBasicaCbCod;
//
//    @Column(name = "quantidade", nullable = false)
//    private Integer quantidade;
//
//    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
//    public void setDoacaoDoaCod(Integer doacaoDoaCod) { this.doacaoDoaCod = doacaoDoaCod; }
//
//    public String getNecessidadeCestaPessoaCarentePcCpf() { return necessidadeCestaPessoaCarentePcCpf; }
//    public void setNecessidadeCestaPessoaCarentePcCpf(String v) { this.necessidadeCestaPessoaCarentePcCpf = v; }
//
//    public Integer getNecessidadeCestaCestaBasicaCbCod() { return necessidadeCestaCestaBasicaCbCod; }
//    public void setNecessidadeCestaCestaBasicaCbCod(Integer v) { this.necessidadeCestaCestaBasicaCbCod = v; }
//
//    public Integer getQuantidade() { return quantidade; }
//    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
//}
