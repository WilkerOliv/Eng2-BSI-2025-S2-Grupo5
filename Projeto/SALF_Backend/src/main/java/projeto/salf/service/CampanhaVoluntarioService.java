package projeto.salf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.salf.model.CampanhaVoluntario;
import projeto.salf.model.CampanhaVoluntarioId;
import projeto.salf.repository.CampanhaVoluntarioRepository;

import java.util.List;

@Service
public class CampanhaVoluntarioService {

    private final CampanhaVoluntarioRepository repo;

    public CampanhaVoluntarioService(CampanhaVoluntarioRepository repo) {
        this.repo = repo;
    }

    public List<CampanhaVoluntario> listarPorCampanha(Integer idCampanha) {
        return repo.findById_CampanhaIdCampanha(idCampanha);
    }

    @Transactional
    public CampanhaVoluntario vincular(CampanhaVoluntario entidade) {
        // entidade já deve vir com o ID composto preenchido (idCampanha e cpfVoluntario)
        return repo.save(entidade);
    }

    @Transactional
    public void desvincular(Integer idCampanha, String cpfVoluntario) {
        CampanhaVoluntarioId id = new CampanhaVoluntarioId(idCampanha, cpfVoluntario);
        repo.deleteById(id);
    }
}
