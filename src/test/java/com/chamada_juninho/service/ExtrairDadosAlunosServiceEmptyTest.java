package com.chamada_juninho.service;

import com.chamada_juninho.repository.RegistraAlunorepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtrairDadosAlunosServiceEmptyTest {

    @Test
    public void exportAlunosToXls_withEmptyList_returnsNonEmptyWorkbook() throws IOException {
        RegistraAlunorepository repo = Mockito.mock(RegistraAlunorepository.class);
        Mockito.when(repo.findAll()).thenReturn(List.of());

        ExtrairDadosAlunosService service = new ExtrairDadosAlunosService(repo);

        byte[] xls = service.exportAlunosToXls();

        assertNotNull(xls);
        assertTrue(xls.length > 0, "Mesmo com lista vazia, um arquivo xls válido deve ser gerado (bytes > 0)");

        // Try to open it as an HSSFWorkbook to ensure it's a valid BIFF file
        try (Workbook wb = new HSSFWorkbook(new ByteArrayInputStream(xls))) {
            assertEquals(1, wb.getNumberOfSheets());
            assertEquals("Alunos", wb.getSheetAt(0).getSheetName());
        }
    }
}

