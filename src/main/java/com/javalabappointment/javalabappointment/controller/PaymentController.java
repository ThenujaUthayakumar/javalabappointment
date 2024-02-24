package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.PaymentEntity;
import com.javalabappointment.javalabappointment.persist.Payment;
import com.javalabappointment.javalabappointment.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    /*------------------------------- CREATE PAYMENT ----------------------------*/
    @PostMapping("/store")
    public PaymentEntity store(@RequestBody Payment payment) throws ParseException {
        return paymentService.store(payment);
    }

    /*------------------------------ GET ALL PAYMENT RECORDS --------------------------------  */
    @GetMapping
    public Page<PaymentEntity> getAll(@RequestParam(required = false) Integer skip,
                                          @RequestParam(required = false) Integer limit,
                                          @RequestParam(required = false) String orderBy,
                                          Payment payment) {
        return paymentService.getAll(skip,limit,orderBy,payment);
    }

    /*------------------------------ DOWNLOAD PAYMENT RECORDS --------------------------------  */
    @GetMapping("/download")
    public void download(@RequestParam(required = false) Integer skip, @RequestParam(required = false) Integer limit,
                         @RequestParam(required = false) String orderBy, Payment payment,
                         @RequestParam Integer fileType, @RequestParam(required = false) String downloadColumn, HttpServletResponse response) throws IOException {
        paymentService.download(skip, limit, orderBy, payment,fileType, downloadColumn, response);
    }
}
