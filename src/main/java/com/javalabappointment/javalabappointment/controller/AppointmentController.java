package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.persist.Appointment;
import com.javalabappointment.javalabappointment.service.AppointmentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /*------------------------- CREATE APPOINTMENT --------------------------------  */
    @PostMapping("/store")
    public AppointmentEntity store(@RequestBody Appointment appointment) throws ParseException {
        return appointmentService.store(appointment);
    }

    /*------------------------------ GET ALL APPOINTMENTS RECORDS --------------------------------  */
    @GetMapping
    public Page<AppointmentEntity> getAll(@RequestParam(required = false) Integer skip,
                                       @RequestParam(required = false) Integer limit,
                                       @RequestParam(required = false) String orderBy,
                                       Appointment appointment) {
        return appointmentService.getAll(skip,limit,orderBy,appointment);
    }

    /*------------------------- DOWNLOAD AS A PDF IN ALL RECORDS -------------------*/
    @GetMapping("/download")
    public void download(@RequestParam(required = false) Integer skip, @RequestParam(required = false) Integer limit,
                         @RequestParam(required = false) String orderBy, Appointment search,
                         @RequestParam Integer fileType, @RequestParam(required = false) String downloadColumn, HttpServletResponse response) throws IOException {
        appointmentService.download(skip, limit, orderBy, search,fileType, downloadColumn, response);
    }

    /*-------------------------------- DELETE API----------------------------------- */
    @DeleteMapping("/delete")
    public ResponseEntity delete (@RequestParam(required = true) Integer id)
    {
        return appointmentService.delete(id);
    }

    /*------------------------- STATISTICS ------------------*/
    @GetMapping("/statistics")
    public Page<Map<Object,String>> getStatistics(@RequestParam(required = false) Integer skip,
                                                  @RequestParam(required = false) Integer limit,
                                                  @RequestParam(required = false) String orderBy,
                                                  Appointment appointment) {
        return appointmentService.getStatistics(skip,limit,orderBy,appointment);
    }
}
