package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.ItensLista;
import projeto.salf.model.ItensListaId;

@Repository
public interface ItensListaRepository extends JpaRepository<ItensLista, ItensListaId> {}
