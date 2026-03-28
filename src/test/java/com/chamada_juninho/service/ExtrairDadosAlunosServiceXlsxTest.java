package com.chamada_juninho.service;

import com.chamada_juninho.entity.RegistraAlunos;
import com.chamada_juninho.repository.ChamadaRepository;
import com.chamada_juninho.repository.RegistraAlunorepository;
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

public class ExtrairDadosAlunosServiceXlsxTest {

    @Test
    public void exportAlunosToXlsx_generatesWorkbookWithHeadersAndData() throws IOException {
        // Arrange: mock repository with one aluno
        RegistraAlunorepository repo = Mockito.mock(RegistraAlunorepository.class);
        RegistraAlunos aluno = RegistraAlunos.builder()
                .id(1L)
                .inativo(false)
                .nome("Joao")
                .telefone("999888777L")
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
        byte[] xlsx = service.exportAlunosToXlsx();

        // Assert basic
        assertNotNull(xlsx, "O array retornado não deve ser nulo");
        assertTrue(xlsx.length > 0, "O array retornado deve ter conteúdo");

        // Assert workbook content
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(xlsx))) {
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
}

