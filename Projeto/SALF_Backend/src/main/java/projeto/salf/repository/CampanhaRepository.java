package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.Campanha;

@Repository
public interface CampanhaRepository extends JpaRepository<Campanha, Integer> {
}
