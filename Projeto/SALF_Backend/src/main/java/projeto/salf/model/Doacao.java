package projeto.salf.model;

import jakarta.persistence.*;
import projeto.salf.dao.DoacaoDAO;
import projeto.salf.dto.DoacaoDTO;
import projeto.salf.dto.ItemDoacaoDTO;

import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doacao")
public class Doacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doa_cod")
    private Integer doaCod;

    @Column(name = "pessoa_carente_pc_cpf", length = 14)
    private String pessoaCarentePcCpf;

    @Column(name = "data_doacao", nullable = false)
    private LocalDate dataDoacao;

    @Column(name = "observacao", length = 100)
    private String observacao;

    public Integer getDoaCod() { return doaCod; }
    public void setDoaCod(Integer doaCod) { this.doaCod = doaCod; }

    public String getPessoaCarentePcCpf() { return pessoaCarentePcCpf; }
    public void setPessoaCarentePcCpf(String pessoaCarentePcCpf) { this.pessoaCarentePcCpf = pessoaCarentePcCpf; }

    public LocalDate getDataDoacao() { return dataDoacao; }
    public void setDataDoacao(LocalDate dataDoacao) { this.dataDoacao = dataDoacao; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    // --------------------------
    // MÉTODOS QUE CHAMAM O DAO
    // --------------------------

    public Integer inserirDoacao(Connection conn) {
        DoacaoDAO dao = new DoacaoDAO();
        return dao.inserirDoacao(this, conn);
    }

    public List<DoacaoDTO> getListaDoacao(Connection conn) {
        DoacaoDAO dao = new DoacaoDAO();
        return dao.getAllDoacoes(conn);
    }

    public List<ItemDoacaoDTO> getItensDoacao(int idDoacao, Connection conn) {
        DoacaoDAO dao = new DoacaoDAO();
        return dao.getItensPorDoacao(idDoacao, conn);
    }
}
