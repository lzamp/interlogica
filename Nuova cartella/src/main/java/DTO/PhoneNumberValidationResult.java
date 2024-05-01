package DTO;

public class PhoneNumberValidationResult {

    private String phoneNumber;
    private String status;

    public PhoneNumberValidationResult(String phoneNumber, String status) {
        this.phoneNumber = phoneNumber;
        this.status = status;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
