package com.javalabappointment.javalabappointment.service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.javalabappointment.javalabappointment.entity.SystemUserEntity;
import com.javalabappointment.javalabappointment.persist.SystemUser;
import com.javalabappointment.javalabappointment.repository.SystemUserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemUserService {
    private SystemUserRepository systemUserRepository;

    @Autowired
    public SystemUserService(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    /*---------------------------------- STORE ----------------------------------*/
    @Transactional
    public SystemUserEntity store(SystemUser systemUser) throws ParseException {
        SystemUserEntity systemUserEntity=new SystemUserEntity();

        if(systemUser.getName() == null || systemUser.getName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Name !");
        }

        if(systemUser.getPhoneNumber() == null || systemUser.getPhoneNumber().isEmpty())
        {
            throw new IllegalStateException("Please Enter Mobile Number !");
        }

        if(systemUser.getAddress() == null || systemUser.getAddress().isEmpty())
        {
            throw new IllegalStateException("Please Enter Address !");
        }

        if(systemUser.getEmail() == null || systemUser.getEmail().isEmpty())
        {
            throw new IllegalStateException("Please Enter E-mail !");
        }

        if(systemUser.getRole() == null || systemUser.getRole().isEmpty())
        {
            throw new IllegalStateException("Please Enter Role !");
        }

        if(systemUser.getUsername() == null || systemUser.getUsername().isEmpty())
        {
            throw new IllegalStateException("Please Enter Username !");
        }

        if(systemUser.getPassword() == null || systemUser.getPassword().isEmpty())
        {
            throw new IllegalStateException("Please Enter Password !");
        }

        String hashedPassword = BCrypt.hashpw(systemUser.getPassword(), BCrypt.gensalt());
        
        systemUserEntity.setName(systemUser.getName());
        systemUserEntity.setPhoneNumber(systemUser.getPhoneNumber());
        systemUserEntity.setAddress(systemUser.getAddress());
        systemUserEntity.setEmail(systemUser.getEmail());
        systemUserEntity.setRole(systemUser.getRole());
        systemUserEntity.setUsername(systemUser.getUsername());
        systemUserEntity.setPassword(hashedPassword);

        return systemUserRepository.save(systemUserEntity);
    }

    /*------------------------------- UPDATE -------------------------------------*/
    @Transactional
    public SystemUserEntity update(SystemUser systemUser) throws ParseException {
        SystemUserEntity systemUserEntity=systemUserRepository.findById(systemUser.getId()).orElseThrow(()->new IllegalStateException(
                "User With Id"+systemUser.getId()+"Doesn't Exist !"
        ));

        if(systemUser.getName() == null || systemUser.getName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Name !");
        }

        if(systemUser.getPhoneNumber() == null || systemUser.getPhoneNumber().isEmpty())
        {
            throw new IllegalStateException("Please Enter Mobile Number !");
        }

        if(systemUser.getAddress() == null || systemUser.getAddress().isEmpty())
        {
            throw new IllegalStateException("Please Enter Address !");
        }

        if(systemUser.getEmail() == null || systemUser.getEmail().isEmpty())
        {
            throw new IllegalStateException("Please Enter E-mail !");
        }

        if(systemUser.getRole() == null || systemUser.getRole().isEmpty())
        {
            throw new IllegalStateException("Please Enter Role !");
        }

        if(systemUser.getUsername() == null || systemUser.getUsername().isEmpty())
        {
            throw new IllegalStateException("Please Enter Username !");
        }

        if(systemUser.getPassword() == null || systemUser.getPassword().isEmpty())
        {
            throw new IllegalStateException("Please Enter Password !");
        }

        String hashedPassword = BCrypt.hashpw(systemUser.getPassword(), BCrypt.gensalt());

        systemUserEntity.setName(systemUser.getName());
        systemUserEntity.setPhoneNumber(systemUser.getPhoneNumber());
        systemUserEntity.setAddress(systemUser.getAddress());
        systemUserEntity.setEmail(systemUser.getEmail());
        systemUserEntity.setRole(systemUser.getRole());
        systemUserEntity.setUsername(systemUser.getUsername());
        systemUserEntity.setPassword(hashedPassword);

        return systemUserRepository.save(systemUserEntity);
    }

    /*------------------------- GET ALL RECORDS-------------------------------*/
    public Page<SystemUserEntity>  getAll(Integer pageNo, Integer pageSize, String orderBy,SystemUser systemUser) {
        Pageable pageable = null;
        List<Sort.Order> sorts = new ArrayList<>();
        if (orderBy != null) {
            String[] split = orderBy.split("&");
            for (String s : split) {
                String[] orders = s.split(",");
                sorts.add(new Sort.Order(Sort.Direction.valueOf(orders[1]), orders[0]));
            }
        }
        if (pageNo != null && pageSize != null) {
            if (orderBy != null) {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));
            } else {
                pageable = PageRequest.of(pageNo, pageSize);
            }
        } else {
            if (orderBy != null) {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(sorts));
            }
        }

        String searchLike = null;
        if(systemUser.getSearch() != null){
            searchLike = "%"+systemUser.getSearch()+"%";
        }

        Page<SystemUserEntity> systemUserEntities;

        systemUserEntities=systemUserRepository.findAllUser(pageable,
                systemUser.getId(),
                systemUser.getName(),
                systemUser.getPhoneNumber(),
                systemUser.getAddress(),
                systemUser.getEmail(),
                systemUser.getRole(),
                systemUser.getUsername(),
                systemUser.getPassword(),
                searchLike);

        return systemUserEntities;
    }

    /*-------------------------------- DELETE API----------------------------------- */
    public ResponseEntity<String> delete(Integer id)
    {
        systemUserRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully !");
    }

    /*------------------------------ DOWNLOAD USER LISTS --------------------------------  */
    public void download(Integer pageNo, Integer pageSize, String orderBy, SystemUser systemUser, Integer fileType, String downloadColumn, HttpServletResponse response) throws IOException {
        Page<SystemUserEntity> list = getAll(pageNo, pageSize, orderBy,systemUser);
        String[] requiredColumns;
        if (downloadColumn != null) {
            requiredColumns = downloadColumn.split(",");
        } else {
            requiredColumns = new String[]{"id","name","phoneNumber","address",
                    "email","role","username"};
        }

        List<String> columnNames = new ArrayList<>();

        for (String columns : requiredColumns) {
            if (columns.matches("id")) {
                columnNames.add("User No");
            }
            if (columns.matches("name")) {
                columnNames.add("Name");
            }
            if (columns.matches("phoneNumber")) {
                columnNames.add("Phone Number");
            }
            if (columns.matches("address")) {
                columnNames.add("Address");
            }
            if (columns.matches("email")) {
                columnNames.add("E-mail");
            }
            if (columns.matches("role")) {
                columnNames.add("Role");
            }
            if (columns.matches("username")) {
                columnNames.add("Username");
            }
        }
        if (fileType == 1) {

            response.setHeader("Content-Disposition", "attachment; filename=user.pdf");

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDocument;
            pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4.rotate());

            try {
                float[] colWidths = new float[requiredColumns.length];
                for (int i = 0; i < requiredColumns.length; i++) {
                    colWidths[i] = 10f;
                }

                com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table((UnitValue.createPercentArray(colWidths)));
                table.setFontSize(10);

                //Headed
                for (String col : columnNames) {
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(col).setBold()));
                }

                //Set data
                for (SystemUserEntity emp : list) {
                    for (String col : requiredColumns) {
                        if (col.matches("id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getId() != null ? emp.getId().toString() : "")));
                        }
                        if (col.matches("name")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getName() != null ? emp.getName() : "")));
                        }
                        if (col.matches("phoneNumber")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "")));
                        }
                        if (col.matches("address")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getAddress() != null ? emp.getAddress().toString() : "")));
                        }
                        if (col.matches("email")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getEmail() != null ? emp.getEmail().toString() : "")));
                        }
                        if (col.matches("role")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getRole() != null ? emp.getRole().toString() : "")));
                        }
                        if (col.matches("username")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getUsername() != null ? emp.getUsername().toString() : "")));
                        }
                    }
                }
                document.add(table);
                document.close();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*----------------------------LOGIN ------------------------------------*/
    public SystemUserEntity login(SystemUser systemUser) {
        String username = systemUser.getUsername();
        String password = systemUser.getPassword();

        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be null !");
        }

        List<SystemUserEntity> users = systemUserRepository.findAllByUsername(username);
        if (users.isEmpty()) {
            throw new IllegalStateException("User not found !");
        } else if (users.size() > 1) {
            throw new IllegalStateException("Multiple users found with the same username !");
        }

        SystemUserEntity user = users.get(0);

        String hashedPassword = user.getPassword();

        if (!BCrypt.checkpw(password, hashedPassword)) {
            throw new IllegalStateException("Incorrect password !");
        }

        if (!user.getRole().equals("Admin")) {
            throw new IllegalStateException("User not authorized !");
        }

        return user;
    }

}
