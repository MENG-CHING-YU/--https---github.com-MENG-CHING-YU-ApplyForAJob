package com.project.machine.controller;

import com.project.machine.Bean.StatusCodesBean;
import com.project.machine.Service.StatusCodes.StatusCodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-codes")
public class StatusCodesCotroller {

    @Autowired
    private StatusCodesService statusCodesService;

    @GetMapping("/{type}")
    public List<StatusCodesBean> getStatusCodes(@PathVariable String type) {
        return statusCodesService.getStatusCodesByType(type);
    }
}
