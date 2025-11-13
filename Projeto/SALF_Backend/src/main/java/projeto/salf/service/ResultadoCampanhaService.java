package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.dao.CampanhaDAO;
import projeto.salf.dao.ResultadoCampanhaDAO;
import projeto.salf.model.Campanha;
import projeto.salf.model.ResultadoCampanha;
import projeto.salf.utils.Mensagem;

import java.util.List;

@Service
public class ResultadoCampanhaService {

    @Autowired
    private ResultadoCampanhaDAO resultadoCampanhaDAO;

    @Autowired
    private CampanhaDAO campanhaDAO;

    public List<Campanha> buscarCampanhasFinalizadas() {
        return campanhaDAO.buscarFinalizadas();
    }

    public Mensagem registrarResultado(ResultadoCampanha resultado) {
        // 1. Verificar se a campanha existe
        Campanha campanha = campanhaDAO.buscarPorId(resultado.getIdCampanha());
        if (campanha == null) {
            return new Mensagem("Campanha não encontrada.", false);
        }

        // Regra de Negócio da ERS: Só permitir lançar resultados para campanhas com status “finalizada”
        if (!"Finalizada".equalsIgnoreCase(campanha.getStatus())) {
            return new Mensagem("O resultado só pode ser lançado para campanhas com status 'Finalizada'. Status atual: " + campanha.getStatus(), false);
        }

        // Regra de Negócio da ERS: Não permitir lançar resultado mais de uma vez
        ResultadoCampanha resultadoExistente = resultadoCampanhaDAO.buscarPorCampanhaId(resultado.getIdCampanha());
        if (resultadoExistente != null) {
            return new Mensagem("O resultado para esta campanha já foi lançado.", false);
        }

        // Regra de Negócio da ERS: Campos obrigatórios (Valor Arrecadado, Famílias Atendidas)
        if (resultado.getValorArrecadado() == null || resultado.getValorArrecadado() < 0) {
            return new Mensagem("O valor arrecadado é obrigatório e deve ser positivo.", false);
        }
        if (resultado.getFamiliasAtendidas() == null || resultado.getFamiliasAtendidas() < 0) {
            return new Mensagem("O número de famílias atendidas é obrigatório e deve ser positivo.", false);
        }

        return resultadoCampanhaDAO.salvar(resultado);
    }
}
