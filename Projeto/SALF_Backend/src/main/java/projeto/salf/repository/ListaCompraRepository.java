package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.ListaCompra;

@Repository
public interface ListaCompraRepository extends JpaRepository<ListaCompra, Integer> {}
