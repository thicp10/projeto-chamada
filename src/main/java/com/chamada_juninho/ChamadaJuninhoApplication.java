package com.chamada_juninho;

import com.chamada_juninho.service.ExtrairDadosAlunosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ChamadaJuninhoApplication {

    private static final Logger logger = LoggerFactory.getLogger(ChamadaJuninhoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ChamadaJuninhoApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ExtrairDadosAlunosService extrair) {
        return args -> {
            try {
                byte[] xls = extrair.exportAlunosToXls();
                if (xls != null && xls.length > 0) {
                    Path out = Paths.get("alunos.xls");
                    Files.write(out, xls);
                    logger.info("Arquivo XLS gerado em: {}", out.toAbsolutePath());
                } else {
                    logger.info("Nenhum aluno encontrado ou arquivo vazio (export retornou vazio).");
                }
            } catch (IOException e) {
                logger.error("Erro ao exportar alunos para XLS", e);
            }
        };
    }
}
