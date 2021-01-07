package fudan.database.project.repository;

import fudan.database.project.domain.Bed;
import fudan.database.project.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {
    Bed findByBedNumberAndRoomNumberAndWardNumber(int bedNumber, int roomNumber, int wardNumber);

    List<Bed> findAllByWardNumber(int wardNumber);

    List<Bed> findAllByWardNumberAndStatus(int wardNumber, int status);

    List<Bed> findAllByStatus(int status);
}
