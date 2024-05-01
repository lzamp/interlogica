package controller;

import DTO.PhoneNumberExtraction;
import entity.DataEntry;


import entity.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import repository.DataEntryRepository;
import service.PhoneNumberService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@RestController
//@RequestMapping("/api")
public class PhoneController {
    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private DataEntryRepository dataEntryRepo;

    //Chiamata post che prende in input un file csv con una lista di numeri che salva a DB
    @PostMapping("/upload_csv")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.ok("File not uploaded");
        } else {

            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean isFirstLine = true;  // Flag per identificare la prima riga

                while ((line = fileReader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false; // Imposta il flag a false dopo la prima riga
                        continue; // Salta la prima riga (header)
                    }
                    String[] data = line.split(","); // Assumendo che il separatore sia una virgola
                    DataEntry record = new DataEntry();
                    record.setId(data[0]);
                    record.setPhoneNumber(data[1]);
                    record.setDateLoad(Date.valueOf(LocalDate.now()));
                    dataEntryRepo.save(record);
                }
                boolean save = phoneNumberService.saveNumbers();
                return ResponseEntity.ok("File uploaded successfully!");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
            }
        }
    }


    @GetMapping(value = "/controllAndSave/{phoneNum}", produces = "application/json")
    public ResponseEntity<List<PhoneNumber>> controllAndsavePhoneNumber(@PathVariable String phoneNum) {
        try {
            boolean save = phoneNumberService.saveNumber(phoneNum);
            if (save) {
                List<PhoneNumber> phoneNumber = phoneNumberService.extractElaboratedNumber(phoneNum);
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    return ResponseEntity.ok(phoneNumber);
                } else {
                    // Se la lista è vuota, ritorna uno status 204 No Content
                    return ResponseEntity.noContent().build();
                }
            } else {
                // Se il salvataggio fallisce e non ci sono numeri da ritornare
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
        } catch (SQLException e) {
            // Gestione specifica per SQLException
            return ResponseEntity.internalServerError().body(Collections.emptyList());
        } catch (Exception e) {
            // Gestione di altre eccezioni non specificate
            return ResponseEntity.internalServerError().body(Collections.emptyList());
        }
    }

    @GetMapping(value = "/validate", produces = "application/json")
    public ResponseEntity<PhoneNumberExtraction> extractAll() {
        try {
            PhoneNumberExtraction phoneNumberExtraction = phoneNumberService.validatePhoneNumbers();
            if (phoneNumberExtraction != null) {
                return ResponseEntity.ok(phoneNumberExtraction);
            } else {
                // Se la lista è vuota, ritorna uno status 204 No Content
                return ResponseEntity.noContent().build();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Endpoint is working");
    }

}

