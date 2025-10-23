package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class CampanhaVoluntarioId implements Serializable {
    private Integer campanhaIdCampanha;
    private String voluntarioVolCpf;

    public CampanhaVoluntarioId() {}
    public CampanhaVoluntarioId(Integer c, String v) { this.campanhaIdCampanha = c; this.voluntarioVolCpf = v; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CampanhaVoluntarioId)) return false;
        CampanhaVoluntarioId that = (CampanhaVoluntarioId) o;
        return Objects.equals(campanhaIdCampanha, that.campanhaIdCampanha) &&
                Objects.equals(voluntarioVolCpf, that.voluntarioVolCpf);
    }

    @Override
    public int hashCode() { return Objects.hash(campanhaIdCampanha, voluntarioVolCpf); }

    public Integer getCampanhaIdCampanha() { return campanhaIdCampanha; }
    public void setCampanhaIdCampanha(Integer v) { this.campanhaIdCampanha = v; }
    public String getVoluntarioVolCpf() { return voluntarioVolCpf; }
    public void setVoluntarioVolCpf(String v) { this.voluntarioVolCpf = v; }
}
