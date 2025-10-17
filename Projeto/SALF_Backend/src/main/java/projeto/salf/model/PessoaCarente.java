package projeto.salf.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pessoa_carente")
public class PessoaCarente implements Serializable {
    @Id
    @Column(length = 14)
    private String pcCpf;

    private String pcNome;
    private Date pcDataNasc;
    private String pcTelefone;
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public String getPcCpf() { return pcCpf; }
    public void setPcCpf(String pcCpf) { this.pcCpf = pcCpf; }

    public String getPcNome() { return pcNome; }
    public void setPcNome(String pcNome) { this.pcNome = pcNome; }

    public Date getPcDataNasc() { return pcDataNasc; }
    public void setPcDataNasc(Date pcDataNasc) { this.pcDataNasc = pcDataNasc; }

    public String getPcTelefone() { return pcTelefone; }
    public void setPcTelefone(String pcTelefone) { this.pcTelefone = pcTelefone; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}