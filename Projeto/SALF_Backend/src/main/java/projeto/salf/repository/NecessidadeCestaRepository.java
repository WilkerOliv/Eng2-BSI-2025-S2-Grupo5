package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.NecessidadeCesta;
import projeto.salf.model.NecessidadeCestaId;

@Repository
public interface NecessidadeCestaRepository extends JpaRepository<NecessidadeCesta, NecessidadeCestaId> {}
