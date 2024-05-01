package service;

import DTO.PhoneNumberExtraction;
import DTO.PhoneNumberValidationResult;
import entity.PhoneNumber;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public interface PhoneNumberService {
    PhoneNumberValidationResult validateAndCorrect(String phoneNumber);
    boolean saveNumbers() throws SQLException;
    public boolean saveNumber(String phoneNumber) throws SQLException;
    List<PhoneNumber> extractElaboratedNumber(String phoneNum)  throws SQLException;
    public PhoneNumberExtraction validatePhoneNumbers() throws SQLException;
}
