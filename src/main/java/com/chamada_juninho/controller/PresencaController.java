package com.chamada_juninho.controller;

import com.chamada_juninho.dto.ChamadaDto;
import com.chamada_juninho.dto.ChamadaRegistroDto;
import com.chamada_juninho.dto.RegistraAlunodto;
import com.chamada_juninho.entity.Periodo;
import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.service.ExtrairDadosAlunosService;
import com.chamada_juninho.service.PresencaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chamada")
public class PresencaController {

    private final PresencaService presencaService;
    private final ExtrairDadosAlunosService extrairService;

    public PresencaController(PresencaService presencaService, ExtrairDadosAlunosService extrairService) {
        this.presencaService = presencaService;
        this.extrairService = extrairService;
    }

    @GetMapping("/alunos-ativos")
    public ResponseEntity<List<RegistraAlunodto>> listarAlunosAtivos() {
        List<RegistraAlunos> ativos = presencaService.listarAlunosAtivos();
        List<RegistraAlunodto> dtos = ativos.stream()
                .map(RegistraAlunodto::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/registrar")
    public ResponseEntity<ChamadaDto> registrarPresenca(@Valid @RequestBody ChamadaRegistroDto dto) {
        ChamadaDto chamada = presencaService.registrarPresenca(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(chamada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChamadaDto> atualizarPresenca(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        boolean presente = body.getOrDefault("presente", false);
        ChamadaDto atualizada = presencaService.atualizarPresenca(id, presente);
        return ResponseEntity.ok(atualizada);
    }

    @GetMapping
    public ResponseEntity<List<ChamadaDto>> listarChamada(
            @RequestParam LocalDate data,
            @RequestParam Periodo periodo) {
        List<ChamadaDto> chamadas = presencaService.listarChamadaPorDataEPeriodo(data, periodo);
        return ResponseEntity.ok(chamadas);
    }

    @PutMapping("/finalizar")
    public ResponseEntity<Map<String, Object>> finalizarChamada(
            @RequestParam LocalDate data,
            @RequestParam Periodo periodo) {
        int registrosFinalizados = presencaService.finalizarChamada(data, periodo);
        return ResponseEntity.ok(Map.of(
                "mensagem", "Chamada finalizada com sucesso",
                "data", data.toString(),
                "periodo", periodo.name(),
                "registrosFinalizados", registrosFinalizados
        ));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportarChamadaDiaria(@RequestParam LocalDate data) {
        try {
            byte[] xlsx = extrairService.exportChamadaDiariaToXlsx(data);
            if (xlsx == null || xlsx.length == 0) {
                return ResponseEntity.noContent().build();
            }

            String filename = "chamada_" + data + ".xlsx";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(xlsx);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
