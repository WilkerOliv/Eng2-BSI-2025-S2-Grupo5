package projeto.salf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto.salf.model.*;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {}








