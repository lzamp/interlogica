package service.impl;


import DTO.PhoneNumberExtraction;
import DTO.PhoneNumberValidationResult;
import dao.PhoneNumberDAO;
import entity.PhoneNumber;
import entity.DataEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.DataEntryRepository;
import repository.PhoneNumberRepository;
import service.PhoneNumberService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    @Autowired
    private PhoneNumberDAO phoneNumberDAO;

    @Autowired
    private DataEntryRepository dataEntryRepository;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Override
    public PhoneNumberValidationResult validateAndCorrect(String phoneNumber) {
        // Si assume da requisito che il numero corretto ha lunghezza 11 e inizia con 27.
        //Vado a verificare che il numero non sia null o vuoto. In questo caso vado a mettere null nel campo del numero di telefono
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return new PhoneNumberValidationResult("", "INVALID");
        }

        //Rimuove eventuali spazzi
        phoneNumber = phoneNumber.replaceAll("\\s+", "");

        // Rimuovi tutti i caratteri non numerici eccetto il segno + all'inizio se presente
        phoneNumber = phoneNumber.replaceAll("[^\\d]", "");

        // Va a vedere se il numero inizia con 27 e se la lunghezza è 11.
        if (phoneNumber.length() == 11 && phoneNumber.startsWith("27")) {
            return new PhoneNumberValidationResult(phoneNumber, "ACCEPTABLE");
        } else if (phoneNumber.length() == 9 && !phoneNumber.startsWith("27")) {
            // Se il prefisso 27 é assente lo va ad aggiungere.
            phoneNumber = "27" + phoneNumber;
            return new PhoneNumberValidationResult(phoneNumber, "CORRECTED");
        }

        return new PhoneNumberValidationResult("", "INVALID");
    }


    @Override
    @Transactional
    public boolean saveNumbers() throws SQLException {

        List<DataEntry> dataEntries = phoneNumberDAO.extractPhoneNumber();
        boolean ris = false;

        if(!dataEntries.isEmpty()){
            for (DataEntry entry : dataEntries) {
                PhoneNumberValidationResult validationResult = validateAndCorrect(entry.getPhoneNumber());
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setId(entry.getId());
                phoneNumber.setPhoneNumber(validationResult.getPhoneNumber());
                phoneNumber.setStatus(validationResult.getStatus());
                phoneNumber.setDateLoad(Date.valueOf(LocalDate.now()));
                phoneNumberRepository.save(phoneNumber);
                ris = true;

            }
        }
        return ris;
    }

    @Override
    @Transactional
    public boolean saveNumber(String phoneNum) throws SQLException {
        boolean save = phoneNumberDAO.saveNumber(phoneNum);

        List<DataEntry> dataEntries = null;
        if(save == true) {
            dataEntries = phoneNumberDAO.extractPhoneNumberInput(phoneNum);
        }
        boolean ris = false;

        if(!dataEntries.isEmpty()){
            for (DataEntry entry : dataEntries) {
                PhoneNumberValidationResult validationResult = validateAndCorrect(entry.getPhoneNumber());
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setId(entry.getId());
                phoneNumber.setPhoneNumber(validationResult.getPhoneNumber());
                phoneNumber.setStatus(validationResult.getStatus());
                phoneNumber.setDateLoad(Date.valueOf(LocalDate.now()));
                phoneNumberRepository.save(phoneNumber);
                ris = true;

            }
        }
        return ris;
    }

    @Override
    public List<PhoneNumber> extractElaboratedNumber(String phoneNum) throws SQLException {
        return phoneNumberDAO.extractElaboratedNumber(phoneNum);
    }

    @Override
    public PhoneNumberExtraction validatePhoneNumbers() throws SQLException {
        PhoneNumberExtraction result = new PhoneNumberExtraction();
        result.setAcceptableNumbers(phoneNumberDAO.fetchAcceptableNumbers());
        result.setCorrectedNumbers(phoneNumberDAO.fetchCorrectedNumbers());
        result.setIncorrectNumbers(phoneNumberDAO.fetchIncorrectNumbers());
        return result;
    }
}
