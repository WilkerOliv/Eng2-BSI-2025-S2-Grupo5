package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.PessoaCarente;

@Repository
public interface PessoaCarenteRepository extends JpaRepository<PessoaCarente, String> {}
