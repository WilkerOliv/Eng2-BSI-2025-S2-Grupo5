package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.dao.CampanhaDAO;
import projeto.salf.dao.CampanhaVoluntarioDAO;
import projeto.salf.dao.VoluntarioDAO;
import projeto.salf.model.Campanha;
import projeto.salf.model.CampanhaVoluntario;
import projeto.salf.model.Voluntario;
import projeto.salf.utils.Mensagem;

import java.time.LocalDate;
import java.util.List;

@Service
public class CampanhaService {

    @Autowired
    private CampanhaDAO campanhaDAO;

    @Autowired
    private VoluntarioDAO voluntarioDAO;

    @Autowired
    private CampanhaVoluntarioDAO campanhaVoluntarioDAO;

    public Mensagem salvar(Campanha campanha) {
        // Regra de Negócio da ERS: Campos obrigatórios (Nome, Data Início, Data Fim)
        if (campanha.getNome() == null || campanha.getNome().trim().isEmpty()) {
            return new Mensagem("O nome da campanha é obrigatório.", false);
        }
        if (campanha.getDataInicio() == null) {
            return new Mensagem("A data de início da campanha é obrigatória.", false);
        }
        if (campanha.getDataFim() == null) {
            return new Mensagem("A data de fim da campanha é obrigatória.", false);
        }

        // Regra de Negócio da ERS: Data de Fim não pode ser anterior à Data de Início
        if (campanha.getDataFim().isBefore(campanha.getDataInicio())) {
            return new Mensagem("A data de fim não pode ser anterior à data de início da campanha.", false);
        }

        // Se for um novo cadastro, define o status inicial
        if (campanha.getId() == null) {
            campanha.setStatus("Em Andamento");
            campanha.setAtivo(true);
        }

        return campanhaDAO.salvar(campanha);
    }

    public List<Campanha> buscarTodos() {
        return campanhaDAO.buscarTodos();
    }

    public Campanha buscarPorId(Long id) {
        return campanhaDAO.buscarPorId(id);
    }

    public Mensagem inativar(Long id) {
        // Regra de Negócio da ERS: Exclusão lógica (inativação)
        return campanhaDAO.inativar(id);
    }

    public List<Voluntario> buscarVoluntariosAtivos() {
        return voluntarioDAO.buscarAtivos();
    }

    public Mensagem vincularVoluntarios(Long idCampanha, List<CampanhaVoluntario> voluntarios) {
        if (voluntarios == null || voluntarios.isEmpty()) {
            // Permite desvincular todos, mas se for para vincular, precisa de pelo menos um
            return campanhaVoluntarioDAO.salvarVoluntarios(idCampanha, voluntarios);
        }

        // Regra de Negócio: Deve haver pelo menos um voluntário responsável (cargo principal)
        boolean temResponsavel = voluntarios.stream()
                .anyMatch(cv -> cv.getCargoCampanha() != null && !cv.getCargoCampanha().trim().isEmpty());

        if (!temResponsavel) {
            // Simplificando a regra para ter pelo menos um com cargo preenchido
            return new Mensagem("É necessário informar o cargo de pelo menos um voluntário para vincular à campanha.", false);
        }

        return campanhaVoluntarioDAO.salvarVoluntarios(idCampanha, voluntarios);
    }

    public List<CampanhaVoluntario> buscarVoluntariosDaCampanha(Long idCampanha) {
        return campanhaVoluntarioDAO.buscarPorCampanha(idCampanha);
    }
}
