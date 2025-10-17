package projeto.salf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cesta_basica")
public class CestaBasica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cb_cod")
    private Integer cbCod;

    @Column(name = "descricao", length = 100, nullable = false)
    private String descricao;

    public CestaBasica() {}

    public CestaBasica(Integer cbCod, String descricao) {
        this.cbCod = cbCod;
        this.descricao = descricao;
    }

    public Integer getCbCod() { return cbCod; }
    public void setCbCod(Integer cbCod) { this.cbCod = cbCod; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
