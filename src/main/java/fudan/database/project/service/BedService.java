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
}
