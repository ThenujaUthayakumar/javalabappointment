package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.ContactEntity;
import com.javalabappointment.javalabappointment.persist.Contact;
import com.javalabappointment.javalabappointment.service.ContactService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;

    /*------------------------- CREATE CONTACT --------------------------------  */
    @PostMapping("/store")
    public ContactEntity store(@RequestBody Contact contact) throws ParseException {
        return contactService.store(contact);
    }

    /*-------------------------- GET ALL CONTACTS RECORDS ----------------------*/
    @GetMapping
    public Page<ContactEntity> getAll(@RequestParam(required = false) Integer skip,
                                          @RequestParam(required = false) Integer limit,
                                          @RequestParam(required = false) String orderBy,
                                          Contact contact) {
        return contactService.getAll(skip,limit,orderBy,contact);
    }

    /*----------------------- DOWNLOAD CONTACT DETAILS ----------------------------*/
    @GetMapping("/download")
    public void download(@RequestParam(required = false) Integer skip, @RequestParam(required = false) Integer limit,
                         @RequestParam(required = false) String orderBy, Contact search,
                         @RequestParam Integer fileType, @RequestParam(required = false) String downloadColumn, HttpServletResponse response) throws IOException {
        contactService.download(skip, limit, orderBy, search,fileType, downloadColumn, response);
    }
}
