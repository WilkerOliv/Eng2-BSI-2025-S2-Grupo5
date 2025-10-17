package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {}
