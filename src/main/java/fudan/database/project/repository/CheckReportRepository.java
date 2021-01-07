package fudan.database.project.repository;

import fudan.database.project.domain.CheckReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckReportRepository extends JpaRepository<CheckReport, Integer> {

}
