package com.sathvik.services;

import com.sathvik.entities.Player;
import com.sathvik.repositories.PlayerRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;


@Service
public class UpdateNFLPlayersService {

    @Autowired
    private PlayerRepository playerRepository;

    public void importPlayersFromCsv(InputStream inputStream) throws Exception {
        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            try (CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {
                for (CSVRecord record : csvParser) {
                    if (Objects.equals(record.get("id"), "")) {
                        Long externalId = Long.parseLong(record.get("id_1"));
                        String fullName = record.get("fullName_1");  // match CSV header
                        String position = record.get("position_1");

                        // Look up player by externalId
                        helperForUpdating(externalId, fullName, position);
                        continue;
                    }
                    Long externalId = Long.parseLong(record.get("id"));
                    String fullName = record.get("fullName");  // match CSV header
                    String position = record.get("position");

                    // Look up player by externalId
                    helperForUpdating(externalId, fullName, position);
                }
            }
        }
    }

    private void helperForUpdating(Long externalId, String fullName, String position) {
        Optional<Player> existingPlayerOpt = playerRepository.findById(externalId);

        if (existingPlayerOpt.isPresent()) {
            Player existingPlayer = existingPlayerOpt.get();
            // Update fields
            existingPlayer.setFullName(fullName);
            existingPlayer.setPosition(position);
            // Save updated player
            playerRepository.save(existingPlayer);
            System.out.println("Somethings working?");
        } else {
            // Create new player
            Player newPlayer = Player.builder()
                    .id(externalId)
                    .fullName(fullName)
                    .position(position)
                    .build();
            playerRepository.save(newPlayer);
        }
    }

    @Scheduled(cron = "0 0 8 ? * TUE")
    public void updatePlayersFromPythonScript() {
        try {
            String venvPython = "/Users/sathvik/Desktop/fantasy-project-frontend-backend/fantasy-project/fantasy-backend/venv/bin/python";
            ProcessBuilder pb = new ProcessBuilder(venvPython, "/Users/sathvik/Desktop/fantasy-project-frontend-backend/fantasy-project/fantasy-backend/scripts/Players_Script.py");
            pb.inheritIO(); // for logs to be shown in console
            Process process = pb.start();
            //process.waitFor();

            int exitCode = process.waitFor();

            String csvFilePath = "/Users/sathvik/Desktop/fantasy-project-frontend-backend/fantasy-project/fantasy-backend/scripts/Players.csv";

            if (exitCode == 0) {
                // Python script finished successfully, now read CSV and import
                try (InputStream csvInputStream = new FileInputStream(csvFilePath)) {
                    importPlayersFromCsv(csvInputStream);
                }
            } else {
                System.err.println("Python script exited with code " + exitCode);
            }


            // importPlayersFromCsv();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
