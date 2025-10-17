package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.NecessidadeProdutoId;

@Repository
public interface NecessidadeProdutoRepository extends JpaRepository<NecessidadeProduto, NecessidadeProdutoId> {}
