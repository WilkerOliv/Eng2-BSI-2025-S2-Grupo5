package projeto.salf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.salf.model.Voluntario;
import projeto.salf.repository.VoluntarioRepository;

import java.time.LocalDate;

@Service
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;

    public VoluntarioService(VoluntarioRepository voluntarioRepository) {
        this.voluntarioRepository = voluntarioRepository;
    }

    @Transactional
    public Voluntario criarOuAtualizar(Voluntario v) {
        // regra mínima: garantir datas
        if (v.getDataInicioVoluntario() == null) {
            v.setDataInicioVoluntario(LocalDate.now());
        }
        return voluntarioRepository.save(v);
    }

    public Voluntario buscar(String cpf) {
        return voluntarioRepository.findById(cpf).orElse(null);
    }
}
