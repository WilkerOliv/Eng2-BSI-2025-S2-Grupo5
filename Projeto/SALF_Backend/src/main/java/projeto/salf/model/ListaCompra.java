package projeto.salf.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "lista_compra")
public class ListaCompra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lc_cod")
    private Integer lcCod;

    @Column(name = "funcionario_func_cpf", nullable = false, length = 14)
    private String funcionarioFuncCpf;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Column(name = "descricao", nullable = false, length = 45)
    private String descricao;

    @Column(name = "status_atendimento", nullable = false)
    private Integer statusAtendimento;

    @OneToMany(mappedBy = "listaCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItensDaLista> itens;

    public ListaCompra() {}

}
