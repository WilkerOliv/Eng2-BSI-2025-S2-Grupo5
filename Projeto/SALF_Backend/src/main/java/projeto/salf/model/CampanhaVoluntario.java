package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "campanha_voluntario")
@IdClass(CampanhaVoluntarioId.class)
public class CampanhaVoluntario implements Serializable {

    @Id
    @Column(name = "campanha_id_campanha")
    private Integer campanhaIdCampanha;

    @Id
    @Column(name = "voluntario_vol_cpf", length = 14)
    private String voluntarioVolCpf;

    @Column(name = "cargo_campanha", length = 50)
    private String cargoCampanha;

    public Integer getCampanhaIdCampanha() { return campanhaIdCampanha; }
    public void setCampanhaIdCampanha(Integer campanhaIdCampanha) { this.campanhaIdCampanha = campanhaIdCampanha; }

    public String getVoluntarioVolCpf() { return voluntarioVolCpf; }
    public void setVoluntarioVolCpf(String voluntarioVolCpf) { this.voluntarioVolCpf = voluntarioVolCpf; }

    public String getCargoCampanha() { return cargoCampanha; }
    public void setCargoCampanha(String cargoCampanha) { this.cargoCampanha = cargoCampanha; }
}
