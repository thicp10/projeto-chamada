package com.chamada_juninho.service;

import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.repository.RegistraAlunorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChamadaService {
    @Autowired
    private RegistraAlunorepository registraAlunorepository;

    public List<RegistraAlunos> listarTodos() {
        return registraAlunorepository.findAll();
    }

    public Optional<RegistraAlunos> buscarPorId(Long id) {
        return registraAlunorepository.findById(id);

    }

    public RegistraAlunos salvar(RegistraAlunos aluno) {
        return registraAlunorepository.save(aluno);
    }

    public RegistraAlunos atualizar(Long id, RegistraAlunos aluno) {
        RegistraAlunos existingAluno = registraAlunorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
        existingAluno.setId(id);
        existingAluno.setNome(aluno.getNome());
        existingAluno.setTelefone(aluno.getTelefone());

        return registraAlunorepository.save(existingAluno);
    }

    public void deletar(Long id) {
        registraAlunorepository.deleteById(id);
    }
}
