package projeto.salf.controller.dto;

import java.time.LocalDate;

public class CriarCampanhaDTO {
    public String descricao;
    public LocalDate dataInicio;
    public LocalDate dataFim;
    public String funcionarioCpf; // responsável interno (FK existente em Campanha)
    public String observacao;
}
