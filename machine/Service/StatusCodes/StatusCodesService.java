package com.project.machine.Service.StatusCodes;

import com.project.machine.Bean.StatusCodesBean;
import com.project.machine.Repository.StatusCodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusCodesService {

    @Autowired
    private StatusCodesRepository statusCodesRepository;

    public List<StatusCodesBean> getStatusCodesByType(String type) {
        return statusCodesRepository.findByStatusType(type);
    }
}
