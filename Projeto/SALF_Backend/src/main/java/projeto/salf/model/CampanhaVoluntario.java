package projeto.salf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "campanha_has_voluntario")
public class CampanhaVoluntario {

    @EmbeddedId
    private CampanhaVoluntarioId id;

    @ManyToOne
    @MapsId("campanhaIdCampanha")
    @JoinColumn(name = "campanha_idcampanha")
    private Campanha campanha;

    @ManyToOne
    @MapsId("voluntarioVolCpf")
    @JoinColumn(name = "voluntario_vol_cpf")
    private Voluntario voluntario;

    @Column(name = "cargocampanha", length = 50)
    private String cargoCampanha;

    public CampanhaVoluntario() {}

    public CampanhaVoluntario(CampanhaVoluntarioId id, Campanha campanha, Voluntario voluntario, String cargoCampanha) {
        this.id = id;
        this.campanha = campanha;
        this.voluntario = voluntario;
        this.cargoCampanha = cargoCampanha;
    }

    public CampanhaVoluntarioId getId() { return id; }
    public void setId(CampanhaVoluntarioId id) { this.id = id; }

    public Campanha getCampanha() { return campanha; }
    public void setCampanha(Campanha campanha) { this.campanha = campanha; }

    public Voluntario getVoluntario() { return voluntario; }
    public void setVoluntario(Voluntario voluntario) { this.voluntario = voluntario; }

    public String getCargoCampanha() { return cargoCampanha; }
    public void setCargoCampanha(String cargoCampanha) { this.cargoCampanha = cargoCampanha; }
}
