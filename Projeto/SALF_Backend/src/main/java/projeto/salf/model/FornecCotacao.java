//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "fornec_cotacao")
//@IdClass(FornecCotacaoId.class)
//public class FornecCotacao implements Serializable {
//
//    @Id
//    @Column(name = "fornecedor_id")
//    private Integer fornecedorId;
//
//    @Id
//    @Column(name = "cotacao_id")
//    private Integer cotacaoId;
//
//    @Column(name = "status_realizou", nullable = false)
//    private Integer statusRealizou;
//
//    public Integer getFornecedorId() { return fornecedorId; }
//    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }
//
//    public Integer getCotacaoId() { return cotacaoId; }
//    public void setCotacaoId(Integer cotacaoId) { this.cotacaoId = cotacaoId; }
//
//    public Integer getStatusRealizou() { return statusRealizou; }
//    public void setStatusRealizou(Integer statusRealizou) { this.statusRealizou = statusRealizou; }
//}
