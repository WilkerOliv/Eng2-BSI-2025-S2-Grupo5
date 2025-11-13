package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.dao.FornecedorDAO;
import projeto.salf.model.Fornecedor;
import projeto.salf.utils.Mensagem;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorDAO fornecedorDAO;

    public Mensagem salvar(Fornecedor fornecedor) {
        // Regra de Negócio da ERS: Campos obrigatórios (Nome, CNPJ, Telefone, Email)
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            return new Mensagem("O nome do fornecedor é obrigatório.", false);
        }
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().trim().isEmpty()) {
            return new Mensagem("O CNPJ do fornecedor é obrigatório.", false);
        }
        if (fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().isEmpty()) {
            return new Mensagem("O telefone do fornecedor é obrigatório.", false);
        }
        if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().isEmpty()) {
            return new Mensagem("O e-mail do fornecedor é obrigatório.", false);
        }
        
        // Poderia haver validação de formato de CNPJ/Email, mas vou manter o foco no que a ERS exige explicitamente (obrigatoriedade)
        
        return fornecedorDAO.salvar(fornecedor);
    }

    public List<Fornecedor> buscarTodos() {
        return fornecedorDAO.buscarTodos();
    }

    public Fornecedor buscarPorId(Long id) {
        return fornecedorDAO.buscarPorId(id);
    }

    public Mensagem inativar(Long id) {
        // Regra de Negócio da ERS: Exclusão lógica (inativação)
        return fornecedorDAO.inativar(id);
    }
}
