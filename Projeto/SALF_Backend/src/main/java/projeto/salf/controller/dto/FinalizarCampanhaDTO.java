package projeto.salf.controller.dto;

import java.time.LocalDate;

public class FinalizarCampanhaDTO {
    public Double totalArrecadado;
    public LocalDate dataFim; // opcional: se quiser atualizar a data de fim no ato da finalização
    public String observacao; // opcional: observação de fechamento
}
