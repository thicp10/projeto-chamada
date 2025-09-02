package com.chamada_juninho.controller;

import com.chamada_juninho.dto.RegistraAlunodto;
import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.service.ChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class RegistraController {
    @Autowired
    ChamadaService alunoService;


    @GetMapping
    public ResponseEntity<List<RegistraAlunodto>> listarAlunos() {
        List<RegistraAlunos> registraAlunos = alunoService.listarTodos();
        List<RegistraAlunodto> registraAlunodtos = registraAlunos.stream().map(RegistraAlunodto::fromEntity).toList();
        return ResponseEntity.ok(registraAlunodtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistraAlunodto> buscarAlunoPorId(@PathVariable Long id) {
        Optional<RegistraAlunos> alunoOptional = alunoService.buscarPorId(id);
        return alunoOptional.map(aluno -> ResponseEntity.ok(RegistraAlunodto.fromEntity(aluno)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegistraAlunodto> adicionarAluno(@RequestBody RegistraAlunodto aluno) {
        RegistraAlunos alunoEntity = aluno.toEntity();
        RegistraAlunos alunoSalvo = alunoService.salvar(alunoEntity);

        URI location = ServletUriComponentsBuilder
                .fromPath("/{id}")
                .buildAndExpand(alunoSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(RegistraAlunodto.fromEntity(alunoSalvo));

    }


    @PutMapping("/{id}")
    public ResponseEntity<RegistraAlunodto> atualizarAluno(@PathVariable Long id, @RequestBody RegistraAlunodto alunoDto) {
        RegistraAlunos alunoEntity = alunoDto.toEntity();
        RegistraAlunos alunoAtualizado = alunoService.atualizar(id, alunoEntity);
        return ResponseEntity.ok(RegistraAlunodto.fromEntity(alunoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        alunoService.deletar(id);
        return ResponseEntity.noContent().build();

    }

}
