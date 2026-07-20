package bw_team7.gestione_azienda_energia.csv.services;

import bw_team7.gestione_azienda_energia.comuni.payloads.ComuneDTO;
import bw_team7.gestione_azienda_energia.comuni.services.ComuneService;
import bw_team7.gestione_azienda_energia.province.entities.Provincia;
import bw_team7.gestione_azienda_energia.province.payloads.ProvinciaDTO;
import bw_team7.gestione_azienda_energia.province.services.ProvinciaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class CsvImportService {

    private final ProvinciaService provinciaService;
    private final ComuneService comuneService;

    public CsvImportService(ProvinciaService provinciaService, ComuneService comuneService) {
        this.provinciaService = provinciaService;
        this.comuneService = comuneService;
    }

    public void caricaDatiCsv() {
        if (provinciaService.count() > 0) {
            log.info("Database già popolato con Province e Comuni. Importazione CSV saltata.");
            return;
        }

        log.info("Inizio importazione CSV...");
        importaProvince();
        importaComuni();
        log.info("Importazione CSV completata con successo!");
    }

    private void importaProvince() {
        try {
            ClassPathResource resource = new ClassPathResource("csv/province-italiane.csv");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean isFirstLine = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) { // Salta l'intestazione
                        isFirstLine = false;
                        continue;
                    }

                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        String sigla = parts[0].trim();
                        String nome = parts[1].trim();

                        ProvinciaDTO dto = new ProvinciaDTO(nome, sigla);
                        try {
                            provinciaService.save(dto);
                            count++;
                        } catch (Exception e) {
                            log.warn("Provincia " + nome + " non salvata: " + e.getMessage());
                        }
                    }
                }
                log.info("Importate " + count + " province.");
            }
        } catch (Exception e) {
            log.error("Errore durante l'importazione delle Province: " + e.getMessage());
        }
    }

    private void importaComuni() {
        try {
            ClassPathResource resource = new ClassPathResource("csv/comuni-italiani.csv");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean isFirstLine = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) { // Salta l'intestazione
                        isFirstLine = false;
                        continue;
                    }

                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(";");
                    if (parts.length >= 4) {
                        String nomeComune = parts[2].trim();
                        String nomeProvincia = parts[3].trim();

                        try {
                            Provincia provincia = provinciaService.findByNome(nomeProvincia);

                            ComuneDTO dto = new ComuneDTO(nomeComune, provincia.getId());
                            comuneService.save(dto);
                            count++;
                        } catch (Exception e) {
                            log.warn("Impossibile salvare il comune " + nomeComune + " (Provincia: " + nomeProvincia + "): " + e.getMessage());
                        }
                    }
                }
                log.info("Importati " + count + " comuni.");
            }
        } catch (Exception e) {
            log.error("Errore durante l'importazione dei Comuni: " + e.getMessage());
        }
    }
}