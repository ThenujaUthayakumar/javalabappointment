package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.TechnicianEntity;
import com.javalabappointment.javalabappointment.persist.Technician;
import com.javalabappointment.javalabappointment.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/technician")
public class TechnicianController {
    private final TechnicianService technicianService;

    /*------------------------- CREATE TECHNICIAN --------------------------------*/
    @PostMapping("/store")
    public TechnicianEntity store(@ModelAttribute Technician technician, @RequestPart(value = "file", required = false) MultipartFile file) throws ParseException {
        return technicianService.store(technician,file);
    }

    /*------------------------------------  GET ALL TECHNICIANS --------------------------*/
    @GetMapping
    public Page<TechnicianEntity> getAll(@RequestParam(required = false) Integer skip,
                                      @RequestParam(required = false) Integer limit,
                                      @RequestParam(required = false) String orderBy,
                                      Technician technician) {
        return technicianService.getAll(skip,limit,orderBy,technician);
    }

    /*------------------------------- DOWNLOAD TECHNICIANS RECORDS ----------------------------------*/


}
