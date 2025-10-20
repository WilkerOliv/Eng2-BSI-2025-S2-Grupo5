package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cesta_basica")
public class CestaBasica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cb_cod")
    private Integer cbCod;

    @Column(name = "descricao", nullable = false, length = 100)
    private String descricao;

    public Integer getCbCod() { return cbCod; }
    public void setCbCod(Integer cbCod) { this.cbCod = cbCod; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
