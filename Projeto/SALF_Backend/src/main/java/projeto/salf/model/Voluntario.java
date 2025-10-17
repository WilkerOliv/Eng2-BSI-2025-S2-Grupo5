package projeto.salf.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "voluntario")
public class Voluntario {

    @Id
    @Column(name = "vol_cpf", length = 14)
    private String volCpf;

    @Column(name = "vol_nome", length = 60, nullable = false)
    private String volNome;

    @Column(name = "vol_telefone", length = 20, nullable = false)
    private String volTelefone;

    @Column(name = "rua", length = 45, nullable = false)
    private String rua;

    @Column(name = "bairro", length = 45, nullable = false)
    private String bairro;

    @Column(name = "cidade", length = 45, nullable = false)
    private String cidade;

    @Column(name = "email", length = 45, nullable = false)
    private String email;

    @Column(name = "tipoaecsso", nullable = false)
    private Integer tipoAecsso;

    @Column(name = "senha", length = 30, nullable = false)
    private String senha;

    @Column(name = "datafimvoluntario", nullable = false)
    private LocalDate dataFimVoluntario;

    @Column(name = "datainiciovoluntario", nullable = false)
    private LocalDate dataInicioVoluntario;

    @Column(name = "uf", length = 2, nullable = false)
    private String uf;

    @Column(name = "cep", length = 10, nullable = false)
    private String cep;

    @Column(name = "username", length = 20, nullable = false)
    private String userName;

    public Voluntario() {}

    public Voluntario(String volCpf, String volNome, String volTelefone, String rua, String bairro,
                      String cidade, String email, Integer tipoAecsso, String senha, LocalDate dataFimVoluntario,
                      LocalDate dataInicioVoluntario, String uf, String cep, String userName) {
        this.volCpf = volCpf;
        this.volNome = volNome;
        this.volTelefone = volTelefone;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.email = email;
        this.tipoAecsso = tipoAecsso;
        this.senha = senha;
        this.dataFimVoluntario = dataFimVoluntario;
        this.dataInicioVoluntario = dataInicioVoluntario;
        this.uf = uf;
        this.cep = cep;
        this.userName = userName;
    }

    public String getVolCpf() { return volCpf; }
    public void setVolCpf(String volCpf) { this.volCpf = volCpf; }

    public String getVolNome() { return volNome; }
    public void setVolNome(String volNome) { this.volNome = volNome; }

    public String getVolTelefone() { return volTelefone; }
    public void setVolTelefone(String volTelefone) { this.volTelefone = volTelefone; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getTipoAecsso() { return tipoAecsso; }
    public void setTipoAecsso(Integer tipoAecsso) { this.tipoAecsso = tipoAecsso; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDate getDataFimVoluntario() { return dataFimVoluntario; }
    public void setDataFimVoluntario(LocalDate dataFimVoluntario) { this.dataFimVoluntario = dataFimVoluntario; }

    public LocalDate getDataInicioVoluntario() { return dataInicioVoluntario; }
    public void setDataInicioVoluntario(LocalDate dataInicioVoluntario) { this.dataInicioVoluntario = dataInicioVoluntario; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
