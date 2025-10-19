package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.CampanhaVoluntario;
import projeto.salf.model.CampanhaVoluntarioId;

import java.util.List;

@Repository
public interface CampanhaVoluntarioRepository extends JpaRepository<CampanhaVoluntario, CampanhaVoluntarioId> {
    List<CampanhaVoluntario> findById_CampanhaIdCampanha(Integer idCampanha);
}
