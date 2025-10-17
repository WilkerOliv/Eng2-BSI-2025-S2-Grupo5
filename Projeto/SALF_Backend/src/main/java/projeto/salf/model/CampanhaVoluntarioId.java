package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class CampanhaVoluntarioId implements Serializable {

    @Column(name = "campanha_idcampanha")
    private Integer campanhaIdCampanha;

    @Column(name = "voluntario_vol_cpf", length = 28)
    private String voluntarioVolCpf;

    public CampanhaVoluntarioId() {}

    public CampanhaVoluntarioId(Integer campanhaIdCampanha, String voluntarioVolCpf) {
        this.campanhaIdCampanha = campanhaIdCampanha;
        this.voluntarioVolCpf = voluntarioVolCpf;
    }

    public Integer getCampanhaIdCampanha() { return campanhaIdCampanha; }
    public void setCampanhaIdCampanha(Integer campanhaIdCampanha) { this.campanhaIdCampanha = campanhaIdCampanha; }

    public String getVoluntarioVolCpf() { return voluntarioVolCpf; }
    public void setVoluntarioVolCpf(String voluntarioVolCpf) { this.voluntarioVolCpf = voluntarioVolCpf; }
}
