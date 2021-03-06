package fudan.database.project.service;

import fudan.database.project.domain.Bed;
import fudan.database.project.repository.BedRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class BedService {
    private BedRepository bedRepository;

    public BedService(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    public int wardOfBed(int bedId) {
        return findById(bedId).getWardNumber();
    }

    public Bed findById(int bedId) {
        return bedRepository.findById(bedId);
    }

    public boolean existBedEmpty(int wardNumber) {
        List<Bed> beds = findAllByWardNumberAndStatus(wardNumber, 0);
        return beds.size() > 0;
    }

    public Bed findByBedNumberAndRoomNumberAndWardNumber(int bedNumber, int roomNumber, int wardNumber) {
        return bedRepository.findByBedNumberAndRoomNumberAndWardNumber(bedNumber, roomNumber, wardNumber);
    }

    public List<Bed> findAllByWardNumber(int wardNumber) {
        return bedRepository.findAllByWardNumber(wardNumber);
    }

    public List<Bed> findAllByWardNumberAndStatus(int wardNumber, int status) {
        return bedRepository.findAllByWardNumberAndStatus(wardNumber, status);
    }

    public List<Bed> findAllByStatus(int status) {
        return bedRepository.findAllByStatus(status);
    }

    public List<Bed> findAllFreeBeds(int wardNumber) {
        return bedRepository.findAllByWardNumberAndStatus(wardNumber, 0);
    }
}
