package projeto.salf.model;

public class CampanhaVoluntario {

    private Long idCampanha;
    private String cpfVoluntario; // CPF do Voluntário
    private String cargoCampanha; // Cargo/Função do voluntário na campanha

    public CampanhaVoluntario() {
    }

    // Getters e Setters
    public Long getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(Long idCampanha) {
        this.idCampanha = idCampanha;
    }

    public String getCpfVoluntario() {
        return cpfVoluntario;
    }

    public void setCpfVoluntario(String cpfVoluntario) {
        this.cpfVoluntario = cpfVoluntario;
    }

    public String getCargoCampanha() {
        return cargoCampanha;
    }

    public void setCargoCampanha(String cargoCampanha) {
        this.cargoCampanha = cargoCampanha;
    }
}
