package projeto.salf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.salf.model.*;
import projeto.salf.repository.CampanhaRepository;
import projeto.salf.repository.CampanhaVoluntarioRepository;
import projeto.salf.repository.VoluntarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CampanhaVoluntarioService {

    private final CampanhaRepository campanhaRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final CampanhaVoluntarioRepository campanhaVoluntarioRepository;

    public CampanhaVoluntarioService(CampanhaRepository campanhaRepository,
                                     VoluntarioRepository voluntarioRepository,
                                     CampanhaVoluntarioRepository campanhaVoluntarioRepository) {
        this.campanhaRepository = campanhaRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.campanhaVoluntarioRepository = campanhaVoluntarioRepository;
    }

    @Transactional
    public List<CampanhaVoluntario> atribuir(Integer idCampanha, List<CampanhaVoluntario> itens) {
        Campanha c = campanhaRepository.findById(idCampanha)
                .orElseThrow(() -> new NoSuchElementException("Campanha não encontrada: " + idCampanha));

        List<CampanhaVoluntario> salvos = new ArrayList<>();
        for (CampanhaVoluntario cv : itens) {
            Voluntario v = voluntarioRepository.findById(cv.getId().getVoluntarioVolCpf())
                    .orElseThrow(() -> new NoSuchElementException("Voluntário não encontrado: " + cv.getId().getVoluntarioVolCpf()));

            cv.setCampanha(c);
            cv.setVoluntario(v);
            salvos.add(campanhaVoluntarioRepository.save(cv));
        }
        return salvos;
    }
}
