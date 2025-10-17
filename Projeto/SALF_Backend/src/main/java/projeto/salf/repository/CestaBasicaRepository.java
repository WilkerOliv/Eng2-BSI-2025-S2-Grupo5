package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.CestaBasica;

@Repository
public interface CestaBasicaRepository extends JpaRepository<CestaBasica, Integer> {}
