package bw_team7.gestione_azienda_energia.csv.runners;

import bw_team7.gestione_azienda_energia.csv.services.CsvImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CsvRunner implements CommandLineRunner {

    private final CsvImportService csvImportService;

    public CsvRunner(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @Override
    public void run(String... args) throws Exception {
        csvImportService.caricaDatiCsv();
    }
}