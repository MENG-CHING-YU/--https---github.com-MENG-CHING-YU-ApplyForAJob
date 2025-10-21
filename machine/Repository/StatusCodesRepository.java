package com.project.machine.Repository;

import com.project.machine.Bean.StatusCodesBean;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatusCodesRepository extends JpaRepository<StatusCodesBean, String> {
    List<StatusCodesBean> findByStatusType(String statusType);
}
