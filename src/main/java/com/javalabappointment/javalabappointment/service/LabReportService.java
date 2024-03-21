package com.javalabappointment.javalabappointment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.LabReportEntity;
import com.javalabappointment.javalabappointment.entity.TechnicianEntity;
import com.javalabappointment.javalabappointment.persist.LabReport;
import com.javalabappointment.javalabappointment.repository.AppointmentRepository;
import com.javalabappointment.javalabappointment.repository.LabReportRepository;
import com.javalabappointment.javalabappointment.repository.TechnicianRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LabReportService {
    private LabReportRepository labReportRepository;
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JavaMailSender emailSender;

    private TechnicianRepository technicianRepository;

    @Autowired
    public LabReportService(LabReportRepository labReportRepository,AppointmentRepository appointmentRepository,TechnicianRepository technicianRepository,
                            JavaMailSender emailSender) {
        this.labReportRepository = labReportRepository;
        this.appointmentRepository = appointmentRepository;
        this.technicianRepository = technicianRepository;
        this.emailSender = emailSender;
    }

    /*------------------------- CREATE LAB REPORT --------------------------------*/
    @Transactional
    public LabReportEntity store(LabReport labReport, MultipartFile file) {
        if (labReport.getStatus() == null) {
            throw new IllegalArgumentException("Please Enter Status !");
        }

        TechnicianEntity technicianEntity=technicianRepository.findById(labReport.getTechnicianId()).orElse(null);
        if (technicianEntity==null)
        {
            throw new IllegalStateException("Technician Not Found !");
        }

        AppointmentEntity appointmentEntity=appointmentRepository.findById(labReport.getAppointmentId().getId()).orElse(null);
        if (appointmentEntity==null)
        {
            throw new IllegalStateException("Patient Not Found !");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please Upload Report PDF !");
        }

        /*--------------------- IMAGE SAVING IN DATABASE --------------------*/
        String uploadDirectory = "src/main/resources/static/report";

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

            LabReportEntity labReportEntity = new LabReportEntity();
            labReportEntity.setAppointmentId(labReport.getAppointmentId());
            labReportEntity.setTechnicianId(labReport.getTechnicianId());
            labReportEntity.setStatus(labReport.getStatus());
            labReportEntity.setReferenceNumber(this.getReferencenumber());
            labReportEntity.setPdf(imagePathJson);

            return labReportRepository.save(labReportEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    /*------------------------ SET AUTO REFERENCE NUMBER FOR REPORT ------------------*/
    public String getReferencenumber(){
        String referenceNumber=labReportRepository.findReferenceNumber();
        if(referenceNumber==null){
            return "REPORT1";
        }
        else{
            String[] splitString=referenceNumber.split("REPORT");
            int newReferenceNumber=Integer.valueOf(splitString[1])+1;
            String finalReferenceNumber="REPORT"+newReferenceNumber;
            return finalReferenceNumber;
        }
    }

    /*------------------------------ GET ALL REPORTS RECORDS --------------------------------  */
    public Page<Map<Object,String>> getAll(Integer pageNo, Integer pageSize, String orderBy, LabReport labReport) {
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
        if(labReport.getSearch() != null){
            searchLike = "%"+labReport.getSearch()+"%";
        }

        Page<Map<Object,String>> labReportEntities;

        labReportEntities=labReportRepository.findAllReport(pageable,
                labReport.getId(),
                labReport.getReferenceNumber(),
                labReport.getTechnicianId(),
                labReport.getAppointmentId(),
                labReport.getPdf(),
                labReport.getStatus(),
                searchLike);

        return labReportEntities;
    }

    /*------------------------------------UPDATE ------------------------------------*/
    @Transactional
    public LabReportEntity update(LabReport labReport, MultipartFile file) {
        LabReportEntity labReportEntity=labReportRepository.findById(labReport.getId()).orElseThrow(()->new IllegalStateException(
                "Report With Id"+labReport.getId()+"Doesn't Exist !"
        ));
        if (labReport.getStatus() == null) {
            throw new IllegalArgumentException("Please Enter Status !");
        }

        TechnicianEntity technicianEntity=technicianRepository.findById(labReport.getTechnicianId()).orElse(null);
        if (technicianEntity==null)
        {
            throw new IllegalStateException("Technician Not Found !");
        }

        AppointmentEntity appointmentEntity=appointmentRepository.findById(labReport.getAppointmentId().getId()).orElse(null);
        if (appointmentEntity==null)
        {
            throw new IllegalStateException("Patient Not Found !");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please Upload Report PDF !");
        }

        /*--------------------- IMAGE SAVING IN DATABASE --------------------*/
        String uploadDirectory = "src/main/resources/static/report";

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

            labReportEntity.setAppointmentId(labReport.getAppointmentId());
            labReportEntity.setTechnicianId(labReport.getTechnicianId());
            labReportEntity.setStatus(labReport.getStatus());
            labReportEntity.setReferenceNumber(this.getReferencenumber());
            labReportEntity.setPdf(imagePathJson);

            return labReportRepository.save(labReportEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    /*-------------------------------- DELETE API----------------------------------- */
    public ResponseEntity<String> delete(Integer id)
    {
        labReportRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully !");
    }

    /*------------------------- STATISTICS ------------------*/
    public Page<Map<Object,String>> getStatistics(Integer pageNo, Integer pageSize, String orderBy, LabReport labReport) {
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
        if(labReport.getSearch() != null){
            searchLike = "%"+labReport.getSearch()+"%";
        }

        Page<Map<Object,String>> labReportEntities;

        labReportEntities=labReportRepository.findStatistics(pageable,
                labReport.getId(),
                labReport.getReferenceNumber(),
                labReport.getTechnicianId(),
                labReport.getAppointmentId(),
                labReport.getPdf(),
                labReport.getStatus(),
                searchLike);

        return labReportEntities;
    }

    /*------------------------------- LAB REPORT DOWNLOAD ---------------------------------------*/
    public void download(Integer pageNo, Integer pageSize, String orderBy, LabReport labReport, Integer fileType, String downloadColumn, HttpServletResponse response) throws IOException {
        Page<Map<Object,String>> list = getAll(pageNo, pageSize, orderBy,labReport);
        String[] requiredColumns;
        if (downloadColumn != null) {
            requiredColumns = downloadColumn.split(",");
        } else {
            requiredColumns = new String[]{"id","labNo","patientName","patientMobile","patientNo",
                    "test","technician","status","file"};
        }

        List<String> columnNames = new ArrayList<>();

        for (String columns : requiredColumns) {
            if (columns.matches("id")) {
                columnNames.add("No");
            }
            if (columns.matches("labNo")) {
                columnNames.add("Lab No");
            }
            if (columns.matches("patientName")) {
                columnNames.add("Patient Name");
            }
            if (columns.matches("patientMobile")) {
                columnNames.add("Patient Mobile Number");
            }
            if (columns.matches("patientNo")) {
                columnNames.add("Patient No");
            }
            if (columns.matches("test")) {
                columnNames.add("Test");
            }
            if (columns.matches("technician")) {
                columnNames.add("Technician Name");
            }
            if (columns.matches("status")) {
                columnNames.add("Status");
            }
            if (columns.matches("file")) {
                columnNames.add("Attachment");
            }
        }
        if (fileType == 1) {

            response.setHeader("Content-Disposition", "attachment; filename=report.pdf");

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
                for (Map<Object,String> emp : list) {
                    for (String col : requiredColumns) {
                        if (col.matches("id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.get("id") != null ? emp.get("id") : "")));
                        }
                        if (col.matches("labNo")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.get("labNo") != null ? emp.get("labNo") : "")));
                        }
                        if (col.matches("patientName")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.get("patientName") != null ? emp.get("patientName") : "")));
                        }
                        if (col.matches("patientMobile")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.get("patientMobile") != null ? emp.get("patientMobile") : "")));
                        }
                        if (col.matches("patientNo")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.get("patientNo") != null ? emp.get("patientNo") : "")));
                        }
                        if (col.matches("test")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.get("test") != null ? emp.get("test") : "")));
                        }
                        if (col.matches("technician")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.get("technician") != null ? emp.get("technician") : "")));
                        }
                        if (col.equals("status")) {
                            String status = "Pending";
                            if (emp.get("status") != null) {
                                int statusCode = Integer.parseInt(emp.get("status").toString());
                                status = (statusCode == 1) ? "Completed" : "Pending";
                            }
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(status)));
                        }
                        if (col.matches("file")) {
                            String status = "Yes";
                            if (emp.get("file") == null) {
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

    /**/
    public void sendEmailById(Integer id) {
        Optional<LabReportEntity> reportOptional = labReportRepository.findById(id);
        if (reportOptional.isPresent()) {
            LabReportEntity report = reportOptional.get();
            try {
                String filePath = extractFilePath(report.getPdf());

                File file = ResourceUtils.getFile("src/main/resources/static/report/" + filePath);
                byte[] pdfData = Files.readAllBytes(file.toPath());

                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(report.getAppointmentId().getEmail());
                helper.setSubject("[ "+report.getAppointmentId().getTestId().getName() + " Report ]");
                helper.setText("Dear " + report.getAppointmentId().getName()+ ",\n\n" +
                        "Your Report attached below If you have any questions or concerns regarding the report, please don't hesitate to reach out to us. We're here to assist you and provide any clarification you may need.\n" +
                        "Thank you for choosing " + "ABC Laboratory" + " for your healthcare needs. We wish you continued health and well-being.\n\n" +
                        "Best regards,\n" +
                        "ABC Laboratory \n" +
                        "+94 0115 333 666");


                helper.addAttachment(report.getAppointmentId().getName()+" Report", new ByteArrayResource(pdfData));

                emailSender.send(message);
            } catch (IOException | MessagingException e) {
                throw new RuntimeException("Failed to send email with lab report attachment", e);
            }
        } else {
            throw new RuntimeException("Lab report not found with ID: " + id);
        }
    }

    private String extractFilePath(String json) {
        Pattern pattern = Pattern.compile("\"filePath\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid JSON string: " + json);
    }
}
