package entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity

@Table(name = "phone_numbers")

public class PhoneNumber {

    @Id
    private String id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "date_load")
    private Date dateLoad;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getDateLoad() {
        return dateLoad;
    }

    public void setDateLoad(Date dateLoad) {
        this.dateLoad = dateLoad;
    }
}
