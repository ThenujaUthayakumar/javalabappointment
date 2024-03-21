package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.LabReportEntity;
import com.javalabappointment.javalabappointment.persist.LabReport;
import com.javalabappointment.javalabappointment.repository.LabReportRepository;
import com.javalabappointment.javalabappointment.service.LabReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class LabReportController {
    private final LabReportService labReportService;

    private final LabReportRepository labReportRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    /*------------------------- CREATE LAB REPORT --------------------------------*/
    @PostMapping("/store")
    public LabReportEntity store(@ModelAttribute LabReport labReport, @RequestPart(value = "file", required = false) MultipartFile file) throws ParseException {
        return labReportService.store(labReport,file);
    }

    /*------------------------------ GET ALL REPORTS RECORDS --------------------------------  */
    @GetMapping
    public Page<Map<Object,String>> getAll(@RequestParam(required = false) Integer skip,
                                           @RequestParam(required = false) Integer limit,
                                           @RequestParam(required = false) String orderBy,
                                           LabReport labReport) {
        return labReportService.getAll(skip,limit,orderBy,labReport);
    }

    /*---------------------------UPDATE----------------------------------------------*/
    @PutMapping
    public LabReportEntity update(@ModelAttribute LabReport labReport, @RequestPart(value = "file", required = false) MultipartFile file) throws ParseException {
        return labReportService.update(labReport,file);
    }
    /*-------------------------------- DELETE API----------------------------------- */
    @DeleteMapping("/delete")
    public ResponseEntity delete (@RequestParam(required = true) Integer id)
    {
        return labReportService.delete(id);
    }

    /*------------------------- STATISTICS ------------------*/
    @GetMapping("/statistics")
    public Page<Map<Object,String>> getStatistics(@RequestParam(required = false) Integer skip,
                                                  @RequestParam(required = false) Integer limit,
                                                  @RequestParam(required = false) String orderBy,
                                                  LabReport labReport) {
        return labReportService.getStatistics(skip,limit,orderBy,labReport);
    }

    /*------------------------------- LAB REPORT DOWNLOAD ---------------------------------------*/
    @GetMapping("/download")
    public void download(@RequestParam(required = false) Integer skip, @RequestParam(required = false) Integer limit,
                         @RequestParam(required = false) String orderBy, LabReport labReport,
                         @RequestParam Integer fileType, @RequestParam(required = false) String downloadColumn, HttpServletResponse response) throws IOException {
        labReportService.download(skip, limit, orderBy, labReport,fileType, downloadColumn, response);
    }

    /*--------------------------- SHARE REPORT TO EMAIL ------------------------------------------*/
    @GetMapping("/share")
    public ResponseEntity<String> sendEmail(@RequestParam Integer id) {
        labReportService.sendEmailById(id);
        return ResponseEntity.ok("Email sent successfully");
    }
}
