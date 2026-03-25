package com.chamada_juninho.repository;

import com.chamada_juninho.entity.RegistraAlunos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistraAlunorepository extends JpaRepository<RegistraAlunos, Long> {

    List<RegistraAlunos> findByInativoFalse();
}
