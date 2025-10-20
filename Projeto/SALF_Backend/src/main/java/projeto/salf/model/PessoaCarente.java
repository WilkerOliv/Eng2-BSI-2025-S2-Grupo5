package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "pessoa_carente")
public class PessoaCarente implements Serializable {

    @Id
    @Column(name = "pc_cpf", length = 14)
    private String pcCpf;

    @Column(name = "pc_nome", nullable = false, length = 60)
    private String pcNome;

    @Column(name = "pc_data_nasc", nullable = false)
    private LocalDate pcDataNasc;

    @Column(name = "pc_telefone", nullable = false, length = 20)
    private String pcTelefone;

    @Column(name = "rua", nullable = false, length = 45)
    private String rua;

    @Column(name = "bairro", nullable = false, length = 45)
    private String bairro;

    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @Column(name = "cidade", nullable = false, length = 45)
    private String cidade;

    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

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
