package com.chamada_juninho.controller;

import com.chamada_juninho.dto.RegistraAlunodto;
import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.service.ChamadaService;
import com.chamada_juninho.service.ExtrairDadosAlunosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/alunos")
public class RegistraController {
    @Autowired
    ChamadaService alunoService;

    @Autowired
    ExtrairDadosAlunosService extrairService;

    @GetMapping
    public ResponseEntity<List<RegistraAlunodto>> listarAlunos() {
        List<RegistraAlunos> registraAlunos = alunoService.listarTodos();
        List<RegistraAlunodto> registraAlunodtos = registraAlunos.stream().map(RegistraAlunodto::fromEntity).toList();
        return ResponseEntity.ok(registraAlunodtos);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAlunos() {
        try {
            byte[] xls = extrairService.exportAlunosToXls();
            if (xls == null || xls.length == 0) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"alunos.xls\"")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(xls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/xlsx")
    public ResponseEntity<byte[]> exportAlunosXlsx() {
        try {
            byte[] xlsx = extrairService.exportAlunosToXlsx();
            if (xlsx == null || xlsx.length == 0) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"alunos.xlsx\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(xlsx);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistraAlunodto> buscarAlunoPorId(@PathVariable Long id) {
        RegistraAlunos aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(RegistraAlunodto.fromEntity(aluno));
    }

    @PostMapping
    public ResponseEntity<RegistraAlunodto> adicionarAluno(@Valid @RequestBody RegistraAlunodto aluno) {
        RegistraAlunos alunoEntity = aluno.toEntity();
        RegistraAlunos alunoSalvo = alunoService.salvar(alunoEntity);

        URI location = ServletUriComponentsBuilder
                .fromPath("/{id}")
                .buildAndExpand(alunoSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(RegistraAlunodto.fromEntity(alunoSalvo));

    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistraAlunodto> atualizarAluno(@PathVariable Long id, @Valid @RequestBody RegistraAlunodto alunoDto) {
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
