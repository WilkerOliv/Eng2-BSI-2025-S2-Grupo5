package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import projeto.salf.dao.FuncionarioDAO;
import projeto.salf.model.*;


@Service
public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService(FuncionarioDAO FuncionarioDAO) {
        this.funcionarioDAO = FuncionarioDAO;
    }


    public Funcionario buscaCPF(String cpf) {
        return funcionarioDAO.buscaCPF(cpf);
    }
}