package com.chamada_juninho.repository;

import com.chamada_juninho.entity.RegistraAlunos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistraAlunorepository extends JpaRepository<RegistraAlunos, Long> {

}
