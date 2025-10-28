package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class CampanhaVoluntarioId implements Serializable {
    private String voluntarioCpf;
    private Integer campanhaCod;

    public CampanhaVoluntarioId() {}

    public CampanhaVoluntarioId(String voluntarioCpf, Integer campanhaCod) {
        this.voluntarioCpf = voluntarioCpf;
        this.campanhaCod = campanhaCod;
    }

    public String getVoluntarioCpf() {
        return voluntarioCpf;
    }

    public void setVoluntarioCpf(String voluntarioCpf) {
        this.voluntarioCpf = voluntarioCpf;
    }

    public Integer getCampanhaCod() {
        return campanhaCod;
    }

    public void setCampanhaCod(Integer campanhaCod) {
        this.campanhaCod = campanhaCod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CampanhaVoluntarioId)) return false;
        CampanhaVoluntarioId that = (CampanhaVoluntarioId) o;
        return Objects.equals(voluntarioCpf, that.voluntarioCpf) &&
                Objects.equals(campanhaCod, that.campanhaCod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voluntarioCpf, campanhaCod);
    }
}
