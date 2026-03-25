package com.chamada_juninho.controller;

import com.chamada_juninho.service.RegistraService;
import com.chamada_juninho.service.ExtrairDadosAlunosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistraController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegistraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistraService registraService;

    @MockBean
    private ExtrairDadosAlunosService extrairService;

    @Test
    public void exportAlunos_shouldReturnXlsFile() throws Exception {
        byte[] dummy = new byte[]{0x50, 0x4B, 0x03, 0x04}; // small sample (ZIP/XLSX-like) but content-type checked
        when(extrairService.exportAlunosToXls()).thenReturn(dummy);

        mockMvc.perform(get("/alunos/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"alunos.xls\""))
                .andExpect(content().contentType("application/vnd.ms-excel"))
                .andExpect(content().bytes(dummy));
    }

    @Test
    public void exportAlunosXlsx_shouldReturnXlsxFile() throws Exception {
        byte[] dummy = new byte[]{0x50, 0x4B, 0x03, 0x04}; // small sample
        when(extrairService.exportAlunosToXlsx()).thenReturn(dummy);

        mockMvc.perform(get("/alunos/export/xlsx"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"alunos.xlsx\""))
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(content().bytes(dummy));
    }
}
