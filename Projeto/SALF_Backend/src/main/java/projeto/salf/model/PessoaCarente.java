package projeto.salf.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pessoa_carente")
public class PessoaCarente {

    @Id
    @Column(name = "pc_cpf", length = 14)
    private String pcCpf;

    @Column(name = "pc_nome", length = 60, nullable = false)
    private String pcNome;

    @Column(name = "pc_datanasc", nullable = false)
    private LocalDate pcDataNasc;

    @Column(name = "pc_telefone", length = 20, nullable = false)
    private String pcTelefone;

    @Column(name = "rua", length = 45, nullable = false)
    private String rua;

    @Column(name = "bairro", length = 45, nullable = false)
    private String bairro;

    @Column(name = "uf", length = 2, nullable = false)
    private String uf;

    @Column(name = "cidade", length = 45, nullable = false)
    private String cidade;

    @Column(name = "cep", length = 10, nullable = false)
    private String cep;

    public PessoaCarente() {}

    public PessoaCarente(String pcCpf, String pcNome, LocalDate pcDataNasc, String pcTelefone, String rua, String bairro,
                         String uf, String cidade, String cep) {
        this.pcCpf = pcCpf;
        this.pcNome = pcNome;
        this.pcDataNasc = pcDataNasc;
        this.pcTelefone = pcTelefone;
        this.rua = rua;
        this.bairro = bairro;
        this.uf = uf;
        this.cidade = cidade;
        this.cep = cep;
    }

    public String getPcCpf() { return pcCpf; }
    public void setPcCpf(String pcCpf) { this.pcCpf = pcCpf; }

    public String getPcNome() { return pcNome; }
    public void setPcNome(String pcNome) { this.pcNome = pcNome; }

    public LocalDate getPcDataNasc() { return pcDataNasc; }
    public void setPcDataNasc(LocalDate pcDataNasc) { this.pcDataNasc = pcDataNasc; }

    public String getPcTelefone() { return pcTelefone; }
    public void setPcTelefone(String pcTelefone) { this.pcTelefone = pcTelefone; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}
