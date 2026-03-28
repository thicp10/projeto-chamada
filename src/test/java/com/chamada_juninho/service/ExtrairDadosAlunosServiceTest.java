package com.chamada_juninho.service;

import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.repository.ChamadaRepository;
import com.chamada_juninho.repository.RegistraAlunorepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtrairDadosAlunosServiceTest {

    @Test
    public void exportAlunosToXls_generatesWorkbookWithHeadersAndData() throws IOException {
        // Arrange: mock repository with one aluno
        RegistraAlunorepository repo = Mockito.mock(RegistraAlunorepository.class);
        RegistraAlunos aluno = RegistraAlunos.builder()
                .id(1L)
                .inativo(false)
                .nome("Joao")
                .telefone("999888777")
                .matricula("M123")
                .idade((byte) 10)
                .declaracao("decl")
                .escola("Escola A")
                .periodo("Manha")
                .responsavel("Mae")
                .rgResponsavel("RG1")
                .rgCrianca("RGC1")
                .cpfCrianca("CPF1")
                .observacao("observacao")
                .build();
        Mockito.when(repo.findAll()).thenReturn(List.of(aluno));

        ChamadaRepository chamadaRepo = Mockito.mock(ChamadaRepository.class);
        ExtrairDadosAlunosService service = new ExtrairDadosAlunosService(repo, chamadaRepo);

        // Act
        byte[] xls = service.exportAlunosToXls();

        // Assert basic
        assertNotNull(xls, "O array retornado não deve ser nulo");
        assertTrue(xls.length > 0, "O array retornado deve ter conteúdo");

        // Assert workbook content
        try (Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(xls))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals("Alunos", sheet.getSheetName());

            Row header = sheet.getRow(0);
            assertNotNull(header, "Linha de cabeçalho não deve ser nula");

            // Encontrar índice da coluna 'nome'
            int nomeIdx = -1;
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell c = header.getCell(i);
                if (c != null && "nome".equals(c.getStringCellValue())) {
                    nomeIdx = i;
                    break;
                }
            }
            assertTrue(nomeIdx >= 0, "Cabeçalho deve conter a coluna 'nome'");

            Row dataRow = sheet.getRow(1);
            assertNotNull(dataRow, "Linha de dados do aluno não deve ser nula");
            Cell nomeCell = dataRow.getCell(nomeIdx);
            assertNotNull(nomeCell, "Célula do nome não deve ser nula");
            assertEquals("Joao", nomeCell.getStringCellValue());
        }
    }

    @Test
    public void exportAlunosToXlsx_generatesWorkbookWithHeadersAndData() throws IOException {
        // Arrange: mock repository with one aluno
        RegistraAlunorepository repo = Mockito.mock(RegistraAlunorepository.class);
        RegistraAlunos aluno = RegistraAlunos.builder()
                .id(2L)
                .inativo(false)
                .nome("Maria")
                .telefone("111222333")
                .matricula("M456")
                .idade((byte) 12)
                .declaracao("decl2")
                .escola("Escola B")
                .periodo("Tarde")
                .responsavel("Pai")
                .rgResponsavel("RG2")
                .rgCrianca("RGC2")
                .cpfCrianca("CPF2")
                .observacao("observacao2")
                .build();
        Mockito.when(repo.findAll()).thenReturn(List.of(aluno));

        ChamadaRepository chamadaRepo = Mockito.mock(ChamadaRepository.class);
        ExtrairDadosAlunosService service = new ExtrairDadosAlunosService(repo, chamadaRepo);

        // Act
        byte[] xlsx = service.exportAlunosToXlsx();

        // Assert basic
        assertNotNull(xlsx, "O array retornado não deve ser nulo");
        assertTrue(xlsx.length > 0, "O array retornado deve ter conteúdo");

        // Assert workbook content (XSSF)
        try (Workbook workbook = new XSSFWorkbook(new java.io.ByteArrayInputStream(xlsx))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals("Alunos", sheet.getSheetName());

            Row header = sheet.getRow(0);
            assertNotNull(header, "Linha de cabeçalho não deve ser nula");

            int nomeIdx = -1;
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell c = header.getCell(i);
                if (c != null && "nome".equals(c.getStringCellValue())) {
                    nomeIdx = i;
                    break;
                }
            }
            assertTrue(nomeIdx >= 0, "Cabeçalho deve conter a coluna 'nome'");

            Row dataRow = sheet.getRow(1);
            assertNotNull(dataRow, "Linha de dados do aluno não deve ser nula");
            Cell nomeCell = dataRow.getCell(nomeIdx);
            assertNotNull(nomeCell, "Célula do nome não deve ser nula");
            assertEquals("Maria", nomeCell.getStringCellValue());
        }
    }
}
