package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.TechnicianEntity;
import com.javalabappointment.javalabappointment.persist.Technician;
import com.javalabappointment.javalabappointment.service.TechnicianService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

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
    @GetMapping("/download")
    public void download(@RequestParam(required = false) Integer skip, @RequestParam(required = false) Integer limit,
                         @RequestParam(required = false) String orderBy, Technician technician,
                         @RequestParam Integer fileType, @RequestParam(required = false) String downloadColumn, HttpServletResponse response) throws IOException {
        technicianService.download(skip, limit, orderBy, technician,fileType, downloadColumn, response);
    }

    /*-------------------------------- DELETE API----------------------------------- */
    @DeleteMapping("/delete")
    public ResponseEntity delete (@RequestParam(required = true) Integer id)
    {
        return technicianService.delete(id);
    }

    /*------------------------- STATISTICS ------------------*/
    @GetMapping("/statistics")
    public Page<Map<Object,String>> getStatistics(@RequestParam(required = false) Integer skip,
                                                  @RequestParam(required = false) Integer limit,
                                                  @RequestParam(required = false) String orderBy,
                                                  Technician technician) {
        return technicianService.getStatistics(skip,limit,orderBy,technician);
    }

}
