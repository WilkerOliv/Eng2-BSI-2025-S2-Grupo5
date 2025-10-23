package projeto.salf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.salf.model.Parametrizacao;
import projeto.salf.repository.ParametrizacaoDAO;


@Service
public class ParametrizacaoService {

    private final ParametrizacaoDAO paDAO;

    // Injeção por construtor (Spring injeta automaticamente)
    public ParametrizacaoService(ParametrizacaoDAO dao) {
        this.paDAO = dao;
    }

    @Transactional
    public boolean salvarOuAtualizar(Parametrizacao pa) {
        if (!paDAO.existeRegistro(pa)) {
            paDAO.gravar(pa);
            return true;  // novo
        } else {
            paDAO.alterar(pa);
            return false; // atualizado
        }
    }

    public Parametrizacao getByEmail(String email) {
        return paDAO.getRegistroEmail(email);
    }

    public boolean existeEmpresa() {
        return paDAO.ExisteEmpresas();
    }

    public Parametrizacao getUnica(){
        return paDAO.getUnicaEmp();
    }

}
