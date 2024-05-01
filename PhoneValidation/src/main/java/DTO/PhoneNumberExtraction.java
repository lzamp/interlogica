package DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneNumberExtraction {
    private List<String> acceptableNumbers;
    private Map<String, String> correctedNumbers;
    private List<String> incorrectNumbers;

    // Costruttori, getter e setter
    public PhoneNumberExtraction() {
        this.acceptableNumbers = new ArrayList<>();
        this.correctedNumbers = new HashMap<>();
        this.incorrectNumbers = new ArrayList<>();
    }

    // Aggiungi metodi per aggiungere numeri nei rispettivi elenchi
    public void addAcceptableNumber(String number) {
        acceptableNumbers.add(number);
    }

    public void addCorrectedNumber(String original, String corrected) {
        correctedNumbers.put(original, corrected);
    }

    public void addIncorrectNumber(String number) {
        incorrectNumbers.add(number);
    }

    // Getter e Setter
    public List<String> getAcceptableNumbers() {
        return acceptableNumbers;
    }

    public Map<String, String> getCorrectedNumbers() {
        return correctedNumbers;
    }

    public List<String> getIncorrectNumbers() {
        return incorrectNumbers;
    }

    public void setAcceptableNumbers(List<String> acceptableNumbers) {
        this.acceptableNumbers = acceptableNumbers;
    }

    public void setCorrectedNumbers(Map<String, String> correctedNumbers) {
        this.correctedNumbers = correctedNumbers;
    }

    public void setIncorrectNumbers(List<String> incorrectNumbers) {
        this.incorrectNumbers = incorrectNumbers;
    }
}
