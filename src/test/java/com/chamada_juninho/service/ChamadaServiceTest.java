package com.chamada_juninho.service;

import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.repository.RegistraAlunorepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ChamadaServiceTest {

    @Mock
    private RegistraAlunorepository registraAlunorepository;

    @InjectMocks
    private ChamadaService chamadaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_deveRetornarListaDeAlunos() {
        List<RegistraAlunos> alunos = Arrays.asList(new RegistraAlunos(), new RegistraAlunos());
        when(registraAlunorepository.findAll()).thenReturn(alunos);

        List<RegistraAlunos> resultado = chamadaService.listarTodos();

        assertEquals(2, resultado.size());
        verify(registraAlunorepository).findAll();
    }

    @Test
    void buscarPorId_deveRetornarAlunoQuandoEncontrado() {
        RegistraAlunos aluno = new RegistraAlunos();
        aluno.setId(1L);
        when(registraAlunorepository.findById(1L)).thenReturn(java.util.Optional.of(aluno));

        RegistraAlunos resultado = chamadaService.buscarPorId(1L);

        // agora o método retorna a entidade diretamente
        assertEquals(1L, resultado.getId());
    }

    @Test
    void salvar_deveSalvarAluno() {
        RegistraAlunos aluno = new RegistraAlunos();
        when(registraAlunorepository.save(aluno)).thenReturn(aluno);

        RegistraAlunos resultado = chamadaService.salvar(aluno);

        assertEquals(aluno, resultado);
        verify(registraAlunorepository).save(aluno);
    }

    @Test
    void atualizar_deveAtualizarAlunoQuandoEncontrado() {
        RegistraAlunos aluno = new RegistraAlunos();
        aluno.setNome("Novo Nome");
//        aluno.setTelefone("123456");

        RegistraAlunos existente = new RegistraAlunos();
        existente.setId(1L);

        when(registraAlunorepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
        when(registraAlunorepository.save(any(RegistraAlunos.class))).thenReturn(existente);

        RegistraAlunos resultado = chamadaService.atualizar(1L, aluno);

        assertEquals("Novo Nome", resultado.getNome());
//        assertEquals("123456", resultado.getTelefone());
    }

    @Test
    void deletar_deveChamarDeleteById() {
        // Mock existsById to allow deletion
        when(registraAlunorepository.existsById(1L)).thenReturn(true);

        chamadaService.deletar(1L);
        verify(registraAlunorepository).deleteById(1L);
    }
}