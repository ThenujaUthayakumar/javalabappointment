package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.TestEntity;
import com.javalabappointment.javalabappointment.persist.Test;
import com.javalabappointment.javalabappointment.service.TestService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    /*------------------------- CREATE TEST LISTS --------------------------------  */
    @PostMapping("/store")
    public ResponseEntity<TestEntity> store(@RequestBody Test test) throws ParseException {
        return ResponseEntity.ok(testService.store(test));
    }

    /*------------------------------ GET ALL TEST LISTS --------------------------------  */
    @GetMapping
    public Page<TestEntity> getAllTest(@RequestParam(required = false) Integer skip,
                                       @RequestParam(required = false) Integer limit,
                                       @RequestParam(required = false) String orderBy,
                                       Test test) {
        return testService.getAllTest(skip,limit,orderBy,test);
    }

    /*------------------------------ UPDATE TEST LISTS --------------------------------  */
    @PutMapping
    public ResponseEntity<TestEntity> update(@RequestBody Test test) throws ParseException {
        return ResponseEntity.ok(testService.update(test));
    }

    /*------------------------------ DOWNLOAD TEST LISTS --------------------------------  */
    @GetMapping("/download")
    public void download(@RequestParam(required = false) Integer skip, @RequestParam(required = false) Integer limit,
                            @RequestParam(required = false) String orderBy,Test search,
                            @RequestParam Integer fileType, @RequestParam(required = false) String downloadColumn, HttpServletResponse response) throws IOException {
        testService.download(skip, limit, orderBy, search,fileType, downloadColumn, response);
    }
}
