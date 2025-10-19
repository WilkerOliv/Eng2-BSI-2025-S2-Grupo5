package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.Voluntario;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, String> {
}
