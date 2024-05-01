package repository;

import entity.DataEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataEntryRepository extends JpaRepository<DataEntry, Long>{
}
