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
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CsvImportService {

    private static final Map<String, String> ALIAS_PROVINCIA = new HashMap<>();

    static {
        ALIAS_PROVINCIA.put("Ascoli Piceno", "Ascoli-Piceno");
        ALIAS_PROVINCIA.put("Bolzano/Bozen", "Bolzano");
        ALIAS_PROVINCIA.put("Forlì-Cesena", "Forli-Cesena");
        ALIAS_PROVINCIA.put("La Spezia", "La-Spezia");
        ALIAS_PROVINCIA.put("Monza e della Brianza", "Monza-Brianza");
        ALIAS_PROVINCIA.put("Pesaro e Urbino", "Pesaro-Urbino");
        ALIAS_PROVINCIA.put("Reggio Calabria", "Reggio-Calabria");
        ALIAS_PROVINCIA.put("Reggio nell'Emilia", "Reggio-Emilia");
        ALIAS_PROVINCIA.put("Vibo Valentia", "Vibo-Valentia");
        ALIAS_PROVINCIA.put("Valle d'Aosta/Vallée d'Aoste", "Aosta");
        ALIAS_PROVINCIA.put("Verbano-Cusio-Ossola", "Verbania");
    }

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

            // Apre il file in lettura impostando esplicitamente la codifica UTF-8 per i caratteri speciali
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;           // Variabile temporanea per memorizzare la riga corrente del file
                boolean isFirstLine = true; // Flag per identificare la prima riga del file
                int count = 0;         // Contatore per tracciare il numero di province salvate con successo

                // Ciclo che legge il file riga per riga
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) { // Se è la prima riga del file, la ignora perche continue i nomi delle colonne
                        isFirstLine = false;
                        continue;
                    }

                    if (line.trim().isEmpty()) continue;

                    // Divide la stringa in base al separatore al ;
                    String[] parts = line.split(";");

                    // Verifica che la riga contenga almeno due campi
                    if (parts.length >= 2) {
                        String sigla = parts[0].trim();
                        String nome = parts[1].trim();


                        if (sigla.length() > 2) {
                            String siglaCorretta = correggiSigla(nome, sigla);
                            log.warn("Sigla non valida '" + sigla + "' per la provincia " + nome
                                    + ": corretta in '" + siglaCorretta + "'");
                            sigla = siglaCorretta;
                        }

                        ProvinciaDTO dto = new ProvinciaDTO(nome, sigla);
                        try {
                            provinciaService.save(dto);
                            count++;
                        } catch (Exception e) {
                            log.warn("Provincia " + nome + " non salvata: " + e.getMessage());
                        }
                    }
                }

                try {
                    provinciaService.findByNome("Sud Sardegna");
                } catch (Exception e) {
                    try {
                        provinciaService.save(new ProvinciaDTO("Sud Sardegna", "SU"));
                        count++;
                        log.info("Provincia 'Sud Sardegna' non presente nel CSV: aggiunta manualmente.");
                    } catch (Exception saveEx) {
                        log.warn("Impossibile creare manualmente la provincia 'Sud Sardegna': " + saveEx.getMessage());
                    }
                }

                log.info("Importate " + count + " province.");
            }
        } catch (Exception e) {
            log.error("Errore durante l'importazione delle Province: " + e.getMessage());
        }
    }


    private String correggiSigla(String nomeProvincia, String siglaLetta) {
        if ("Roma".equalsIgnoreCase(nomeProvincia)) {
            return "RM";
        }
        return siglaLetta.substring(0, 2).toUpperCase();
    }

    private void importaComuni() {
        try {
            ClassPathResource resource = new ClassPathResource("csv/comuni-italiani.csv");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean isFirstLine = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(";");

                    if (parts.length >= 4) {
                        String nomeComune = parts[2].trim();
                        String nomeProvincia = parts[3].trim();

                        String nomeProvinciaDaCercare = ALIAS_PROVINCIA.getOrDefault(nomeProvincia, nomeProvincia);

                        try {
                            Provincia provincia = provinciaService.findByNome(nomeProvinciaDaCercare);
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