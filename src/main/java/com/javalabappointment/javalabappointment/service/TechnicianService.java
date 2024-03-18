package com.javalabappointment.javalabappointment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.javalabappointment.javalabappointment.entity.TechnicianEntity;
import com.javalabappointment.javalabappointment.persist.Technician;
import com.javalabappointment.javalabappointment.repository.TechnicianRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TechnicianService {
    private TechnicianRepository technicianRepository;

    @Autowired
    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    /*------------------------- CREATE TECHNICIAN --------------------------------*/
    @Transactional
    public TechnicianEntity store(Technician technician, MultipartFile file) {
        if (technician.getName() == null || technician.getName().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Name !");
        }

        if (technician.getAddress() == null || technician.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Address !");
        }

        if (technician.getEmail() == null || technician.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician E-mail !");
        }

        if (technician.getPhoneNumber() == null || technician.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Mobile Number !");
        }

        if (technician.getPosition() == null || technician.getPosition().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Position !");
        }

        if (technician.getJoinDate() == null || technician.getJoinDate().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Join Date !");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please Upload Technician Image !");
        }

        /*--------------------- IMAGE SAVING IN DATABASE --------------------*/
        String uploadDirectory = "src/main/resources/static/img/technician";

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);
            String relativeFilePath = Paths.get(fileName).toString();

            Map<String, String> imagePath = new HashMap<>();
            imagePath.put("filePath", relativeFilePath);
            ObjectMapper objectMapper = new ObjectMapper();
            String imagePathJson = objectMapper.writeValueAsString(imagePath);

            TechnicianEntity technicianEntity = new TechnicianEntity();
            technicianEntity.setName(technician.getName());
            technicianEntity.setAddress(technician.getAddress());
            technicianEntity.setEmail(technician.getEmail());
            technicianEntity.setPhoneNumber(technician.getPhoneNumber());
            technicianEntity.setPosition(technician.getPosition());
            technicianEntity.setJoinDate(technician.getJoinDate());
            technicianEntity.setImage(imagePathJson);

            return technicianRepository.save(technicianEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }

    }

    /*------------------------------------  GET ALL TECHNICIANS --------------------------*/
    public Page<TechnicianEntity> getAll(Integer pageNo, Integer pageSize, String orderBy, Technician technician) {
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
        if(technician.getSearch() != null){
            searchLike = "%"+technician.getSearch()+"%";
        }

        Page<TechnicianEntity> technicianEntities;

        technicianEntities=technicianRepository.findAllTechnician(pageable,
                technician.getId(),
                technician.getName(),
                technician.getAddress(),
                technician.getPhoneNumber(),
                searchLike);

        return technicianEntities;
    }

    /*------------------------- UPDATE -------------------------------------*/
    @Transactional
    public TechnicianEntity update(Technician technician, MultipartFile file) {
        TechnicianEntity technicianEntity=technicianRepository.findById(technician.getId()).orElseThrow(()->new IllegalStateException(
                "Technician With Id"+technician.getId()+"Doesn't Exist !"
        ));
        if (technician.getName() == null || technician.getName().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Name !");
        }

        if (technician.getAddress() == null || technician.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Address !");
        }

        if (technician.getEmail() == null || technician.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician E-mail !");
        }

        if (technician.getPhoneNumber() == null || technician.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Mobile Number !");
        }

        if (technician.getPosition() == null || technician.getPosition().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Position !");
        }

        if (technician.getJoinDate() == null || technician.getJoinDate().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Join Date !");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please Upload Technician Image !");
        }

        /*--------------------- IMAGE SAVING IN DATABASE --------------------*/
        String uploadDirectory = "src/main/resources/static/img/technician";

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);
            String relativeFilePath = Paths.get(fileName).toString();

            Map<String, String> imagePath = new HashMap<>();
            imagePath.put("filePath", relativeFilePath);
            ObjectMapper objectMapper = new ObjectMapper();
            String imagePathJson = objectMapper.writeValueAsString(imagePath);

            technicianEntity.setName(technician.getName());
            technicianEntity.setAddress(technician.getAddress());
            technicianEntity.setEmail(technician.getEmail());
            technicianEntity.setPhoneNumber(technician.getPhoneNumber());
            technicianEntity.setPosition(technician.getPosition());
            technicianEntity.setJoinDate(technician.getJoinDate());
            technicianEntity.setImage(imagePathJson);

            return technicianRepository.save(technicianEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }

    }

    /*------------------------------- DOWNLOAD TECHNICIANS RECORDS ----------------------------------*/
    public void download(Integer pageNo, Integer pageSize, String orderBy, Technician technician, Integer fileType, String downloadColumn, HttpServletResponse response) throws IOException {
        Page<TechnicianEntity> list = getAll(pageNo, pageSize, orderBy,technician);
        String[] requiredColumns;
        if (downloadColumn != null) {
            requiredColumns = downloadColumn.split(",");
        } else {
            requiredColumns = new String[]{"id","name","address","phoneNumber","email",
                    "position","joinDate","image"};
        }

        List<String> columnNames = new ArrayList<>();

        for (String columns : requiredColumns) {
            if (columns.matches("id")) {
                columnNames.add("Technician No");
            }
            if (columns.matches("name")) {
                columnNames.add("Technician Name");
            }
            if (columns.matches("address")) {
                columnNames.add("Technician Address");
            }
            if (columns.matches("phoneNumber")) {
                columnNames.add("Technician Mobile Number");
            }
            if (columns.matches("email")) {
                columnNames.add("Technician E-mail");
            }
            if (columns.matches("position")) {
                columnNames.add("Technician Position");
            }
            if (columns.matches("joinDate")) {
                columnNames.add("Technician joinDate");
            }
            if (columns.matches("image")) {
                columnNames.add("Technician image");
            }
        }
        if (fileType == 1) {

            response.setHeader("Content-Disposition", "attachment; filename=technician.pdf");

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDocument;
            pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A3.rotate());

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
                for (TechnicianEntity emp : list) {
                    for (String col : requiredColumns) {
                        if (col.matches("id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getId() != null ? emp.getId().toString() : "")));
                        }
                        if (col.matches("name")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getName() != null ? emp.getName() : "")));
                        }
                        if (col.matches("address")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getAddress() != null ? emp.getAddress().toString() : "")));
                        }
                        if (col.matches("phoneNumber")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "")));
                        }
                        if (col.matches("email")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getEmail() != null ? emp.getEmail().toString() : "")));
                        }
                        if (col.matches("position")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getPosition() != null ? emp.getPosition().toString() : "")));
                        }
                        if (col.matches("joinDate")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getJoinDate() != null ? emp.getJoinDate().toString() : "")));
                        }
                        if (col.matches("image")) {
                            String status = "Yes";
                            if (emp.getImage() == null) {
                                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(status != null ? status.toString() : "")));
                            }
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

    /*-------------------------------- DELETE API----------------------------------- */
    public ResponseEntity<String> delete(Integer id)
    {
        technicianRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully !");
    }

    /*------------------------- STATISTICS ------------------*/
    public Page<Map<Object,String>> getStatistics(Integer pageNo, Integer pageSize, String orderBy, Technician technician) {
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
        if(technician.getSearch() != null){
            searchLike = "%"+technician.getSearch()+"%";
        }

        Page<Map<Object,String>> technicianEntities;

        technicianEntities=technicianRepository.findStatistics(pageable,
                technician.getId(),
                technician.getName(),
                technician.getAddress(),
                technician.getPhoneNumber(),
                searchLike);

        return technicianEntities;
    }
}
