package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.persist.Appointment;
import com.javalabappointment.javalabappointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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

}
