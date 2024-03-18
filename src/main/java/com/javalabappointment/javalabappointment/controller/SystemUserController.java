package com.javalabappointment.javalabappointment.controller;
;
import com.javalabappointment.javalabappointment.entity.SystemUserEntity;
import com.javalabappointment.javalabappointment.persist.SystemUser;
import com.javalabappointment.javalabappointment.service.SystemUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class SystemUserController {
    private final SystemUserService systemUserService;

    /*---------------------------------- STORE ----------------------------------*/
    @PostMapping("/store")
    public SystemUserEntity store(@RequestBody SystemUser systemUser) throws ParseException {
        return systemUserService.store(systemUser);
    }

    /*------------------------- UPDATE ---------------------------------------------*/
    @PutMapping
    public ResponseEntity<SystemUserEntity> update(@RequestBody SystemUser systemUser) throws ParseException {
        return ResponseEntity.ok(systemUserService.update(systemUser));
    }

    /*------------------------- GET ALL RECORDS-------------------------------*/
    @GetMapping
    public Page<SystemUserEntity> getAll(@RequestParam(required = false) Integer skip,
                                       @RequestParam(required = false) Integer limit,
                                       @RequestParam(required = false) String orderBy,
                                       SystemUser systemUser) {
        return systemUserService.getAll(skip,limit,orderBy,systemUser);
    }

    /*-------------------------------- DELETE API----------------------------------- */
    @DeleteMapping("/delete")
    public ResponseEntity delete (@RequestParam(required = true) Integer id)
    {
        return systemUserService.delete(id);
    }

    /*------------------------------ DOWNLOAD USER LISTS --------------------------------  */
    @GetMapping("/download")
    public void download(@RequestParam(required = false) Integer skip, @RequestParam(required = false) Integer limit,
                         @RequestParam(required = false) String orderBy, SystemUser search,
                         @RequestParam Integer fileType, @RequestParam(required = false) String downloadColumn, HttpServletResponse response) throws IOException {
        systemUserService.download(skip, limit, orderBy, search,fileType, downloadColumn, response);
    }

    /*-------------------------- LOGIN -------------------------------------*/
    @PostMapping("/login")
    public ResponseEntity<SystemUserEntity> login(@Validated @RequestBody SystemUser systemUser)
    {
        return ResponseEntity.ok(systemUserService.login(systemUser));
    }
}
