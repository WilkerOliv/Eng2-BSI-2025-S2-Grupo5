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

    @Transactional(readOnly = true)
    public List<CampanhaVoluntario> listarPorCampanha(Integer idCampanha) {
        return repo.findByCampanhaIdCampanha(idCampanha);
    }

    @Transactional
    public CampanhaVoluntario vincular(CampanhaVoluntario entidade) {
        return repo.save(entidade);
    }

    @Transactional
    public void desvincular(Integer idCampanha, String cpfVoluntario) {
        repo.deleteByCampanhaIdCampanhaAndVoluntarioVolCpf(idCampanha, cpfVoluntario);

    }
}
