package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "datiCSV")
public class DataEntry {
    @Id
    String id;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "date_load")
    Date dateLoad;

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

    public Date getDateLoad() {
        return dateLoad;
    }

    public void setDateLoad(Date dateLoad) {
        this.dateLoad = dateLoad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataEntry)) return false;
        DataEntry dataEntry = (DataEntry) o;
        return getId().equals(dataEntry.getId()) && Objects.equals(getPhoneNumber(), dataEntry.getPhoneNumber()) && Objects.equals(getDateLoad(), dataEntry.getDateLoad());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPhoneNumber(), getDateLoad());
    }
}
