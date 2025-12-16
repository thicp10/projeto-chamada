package com.chamada_juninho.service;

import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.exception.ResourceNotFoundException;
import com.chamada_juninho.repository.RegistraAlunorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChamadaService {
    @Autowired
    private RegistraAlunorepository registraAlunorepository;

    public List<RegistraAlunos> listarTodos() {
        return registraAlunorepository.findAll();
    }

    public RegistraAlunos buscarPorId(Long id) {
        return registraAlunorepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + id));
    }

    public RegistraAlunos salvar(RegistraAlunos aluno) {
        return registraAlunorepository.save(aluno);
    }

    public RegistraAlunos atualizar(Long id, RegistraAlunos aluno) {
        RegistraAlunos existingAluno = buscarPorId(id); // Reutiliza o método que já lança a exceção
        existingAluno.setId(id);
        existingAluno.setNome(aluno.getNome());
        existingAluno.setTelefone(aluno.getTelefone());

        return registraAlunorepository.save(existingAluno);
    }

    public void deletar(Long id) {
        if (!registraAlunorepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno não encontrado com o ID: " + id);
        }
        registraAlunorepository.deleteById(id);
    }
}
