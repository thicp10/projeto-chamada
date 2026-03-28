package com.chamada_juninho.service;

import com.chamada_juninho.entity.Chamada;
import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.repository.ChamadaRepository;
import com.chamada_juninho.repository.RegistraAlunorepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ExtrairDadosAlunosService {

    private final RegistraAlunorepository registraAlunoRepository;
    private final ChamadaRepository chamadaRepository;

    public ExtrairDadosAlunosService(RegistraAlunorepository registraAlunoRepository,
                                     ChamadaRepository chamadaRepository) {
        this.registraAlunoRepository = registraAlunoRepository;
        this.chamadaRepository = chamadaRepository;
    }

    public byte[] exportAlunosToXls() throws IOException {
        List<RegistraAlunos> alunos = registraAlunoRepository.findAll();

        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            writeAlunosToWorkbook(alunos, workbook);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportAlunosToXlsx() throws IOException {
        List<RegistraAlunos> alunos = registraAlunoRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            writeAlunosToWorkbook(alunos, workbook);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void writeAlunosToWorkbook(List<RegistraAlunos> alunos, Workbook workbook) {
        Sheet sheet = workbook.createSheet("Alunos");

        if (alunos.isEmpty()) {
            return;
        }

        Field[] fields = Arrays.stream(RegistraAlunos.class.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))
                .toArray(Field[]::new);

        int rowIdx = 0;
        Row headerRow = sheet.createRow(rowIdx++);
        for (int i = 0; i < fields.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fields[i].getName());
        }

        for (RegistraAlunos aluno : alunos) {
            Row row = sheet.createRow(rowIdx++);
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value;
                try {
                    value = fields[i].get(aluno);
                } catch (IllegalAccessException e) {
                    value = null;
                }
                Cell cell = row.createCell(i);
                setCellValue(cell, value);
            }
        }

        for (int i = 0; i < fields.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public byte[] exportChamadaDiariaToXlsx(LocalDate data) throws IOException {
        List<Chamada> chamadas = chamadaRepository.findByData(data);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Chamada Diaria - " + data);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nome do Aluno");
            headerRow.createCell(1).setCellValue("Data");
            headerRow.createCell(2).setCellValue("Periodo");
            headerRow.createCell(3).setCellValue("Status de Presenca");

            int rowIdx = 1;
            for (Chamada chamada : chamadas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(chamada.getAluno().getNome());
                row.createCell(1).setCellValue(chamada.getData().toString());
                row.createCell(2).setCellValue(chamada.getPeriodo().name());
                row.createCell(3).setCellValue(chamada.isPresente() ? "Presente" : "Ausente");
            }

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setBlank();
            return;
        }
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDate) {
            // store LocalDate as ISO string to avoid timezone issues
            cell.setCellValue(((LocalDate) value).toString());
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).toString());
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
