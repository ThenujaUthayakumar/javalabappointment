package com.javalabappointment.javalabappointment.service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.TestEntity;
import com.javalabappointment.javalabappointment.persist.Appointment;
import com.javalabappointment.javalabappointment.repository.AppointmentRepository;
import com.javalabappointment.javalabappointment.repository.TestRepository;
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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private TestRepository testRepository;
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,TestRepository testRepository) {
        this.appointmentRepository = appointmentRepository;
        this.testRepository = testRepository;
    }

    /*------------------------- CREATE APPOINTMENT --------------------------------  */
    @Transactional
    public AppointmentEntity store(Appointment appointment) throws ParseException {
        AppointmentEntity appointmentEntity=new AppointmentEntity();

        if(appointment.getName() == null || appointment.getName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Name !");
        }

        if(appointment.getPhoneNumber() == null || appointment.getPhoneNumber().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Mobile Number !");
        }

        if(appointment.getAddress() == null || appointment.getAddress().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Address !");
        }

        if(appointment.getEmail() == null || appointment.getEmail().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient E-mail !");
        }

        if(appointment.getAge() == null || appointment.getAge().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Age !");
        }

        TestEntity testEntity=testRepository.findById(appointment.getTestId().getId()).orElse(null);
        if (testEntity==null)
        {
            throw new IllegalStateException("Test Not Found !");
        }

        if(appointment.getAppointmentDateTime() == null || appointment.getAppointmentDateTime().isEmpty())
        {
            throw new IllegalStateException("Please Enter Appointment Date & Time !");
        }

        if (appointment.getGender()==null || appointment.getGender().isEmpty()){
            throw new IllegalStateException("Please Select the Gender !");
        }
        appointmentEntity.setName(appointment.getName());
        appointmentEntity.setPhoneNumber(appointment.getPhoneNumber());
        appointmentEntity.setAddress(appointment.getAddress());
        appointmentEntity.setEmail(appointment.getEmail());
        appointmentEntity.setAge(appointment.getAge());
        appointmentEntity.setTestId(appointment.getTestId());
        appointmentEntity.setAppointmentDateTime(appointment.getAppointmentDateTime());
        if (appointment.getDoctorName()!=null)
        {
            appointmentEntity.setDoctorName(appointment.getDoctorName());
        }
        appointmentEntity.setReferenceNumber(this.getReferencenumber());
        appointmentEntity.setGender(appointment.getGender());

        return appointmentRepository.save(appointmentEntity);
    }

    /*------------------------ SET AUTO REFERENCE NUMBER FOR APPOINTMENT ------------------*/
    public String getReferencenumber(){
        String referenceNumber=appointmentRepository.findReferenceNumber();
        if(referenceNumber==null){
            return "ARN1";
        }
        else{
            String[] splitString=referenceNumber.split("ARN");
            int newReferenceNumber=Integer.valueOf(splitString[1])+1;
            String finalReferenceNumber="ARN"+newReferenceNumber;
            return finalReferenceNumber;
        }
    }

    /*------------------------------ GET ALL APPOINTMENTS RECORDS --------------------------------  */
    public Page<AppointmentEntity> getAll(Integer pageNo, Integer pageSize, String orderBy, Appointment appointment) {
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
        if(appointment.getSearch() != null){
            searchLike = "%"+appointment.getSearch()+"%";
        }

        Page<AppointmentEntity> appointmentEntities;

        appointmentEntities=appointmentRepository.findAllAppointment(pageable,
                appointment.getId(),
                appointment.getName(),
                appointment.getAddress(),
                appointment.getEmail(),
                appointment.getAge(),
                appointment.getReferenceNumber(),
                appointment.getPhoneNumber(),
                appointment.getAppointmentDateTime(),
                appointment.getTestId(),
                appointment.getDoctorName(),
                searchLike,
                appointment.getGender());

        return appointmentEntities;
    }

    /*------------------------- DOWNLOAD AS A PDF IN ALL RECORDS -------------------*/
    public void download(Integer pageNo, Integer pageSize, String orderBy,Appointment appointment,Integer fileType, String downloadColumn, HttpServletResponse response) throws IOException {
        Page<AppointmentEntity> list = getAll(pageNo, pageSize, orderBy,appointment);
        String[] requiredColumns;
        if (downloadColumn != null) {
            requiredColumns = downloadColumn.split(",");
        } else {
            requiredColumns = new String[]{"id","reference_number","name","address","email","age",
            "phone_number","appointment_date_time","test_id","doctor_name","gender"};
        }

        List<String> columnNames = new ArrayList<>();

        for (String columns : requiredColumns) {
            if (columns.matches("id")) {
                columnNames.add("Appointment No");
            }
            if (columns.matches("reference_number")) {
                columnNames.add("Reference Number");
            }
            if (columns.matches("name")) {
                columnNames.add("Patient Name");
            }
            if (columns.matches("address")) {
                columnNames.add("Patient Address");
            }
            if (columns.matches("email")) {
                columnNames.add("Patient E-mail");
            }
            if (columns.matches("age")) {
                columnNames.add("Patient Age");
            }
            if (columns.matches("phone_number")) {
                columnNames.add("Patient Mobile Number");
            }
            if (columns.matches("appointment_date_time")) {
                columnNames.add("Appointment Date & Time");
            }
            if (columns.matches("test_id")) {
                columnNames.add("Test Name");
            }
            if (columns.matches("doctor_name")) {
                columnNames.add("Doctor Name");
            }
            if (columns.matches("gender")) {
                columnNames.add("Gender");
            }
        }
        if (fileType == 1) {

            response.setHeader("Content-Disposition", "attachment; filename=appointment.pdf");

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
                for (AppointmentEntity emp : list) {
                    for (String col : requiredColumns) {
                        if (col.matches("id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getId() != null ? emp.getId().toString() : "N/A")));
                        }
                        if (col.matches("reference_number")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getReferenceNumber() != null ? emp.getReferenceNumber().toString() : "N/A")));
                        }
                        if (col.matches("name")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getName() != null ? emp.getName() : "N/A")));
                        }
                        if (col.matches("address")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getAddress() != null ? emp.getAddress() :"N/A")));
                        }
                        if (col.matches("email")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getEmail() != null ? emp.getEmail().toString() : "N/A")));
                        }
                        if (col.matches("age")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getAge() != null ? emp.getAge().toString() : "N/A")));
                        }
                        if (col.matches("phone_number")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getPhoneNumber() != null ? emp.getPhoneNumber().toString() : "N/A")));
                        }
                        if (col.matches("appointment_date_time")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getAppointmentDateTime() != null ? emp.getAppointmentDateTime().toString() : "N/A")));
                        }
                        TestEntity testEntity=testRepository.findById(Integer.valueOf(emp.getTestId().getId())).orElse(null);
                        if (col.matches("test_id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(testEntity.getName() != null ? testEntity.getName().toString() : "N/A")));
                        }
                        if (col.matches("doctor_name")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getDoctorName() != null ? emp.getDoctorName().toString() : "N/A")));
                        }
                        if (col.matches("gender")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getGender() != null ? emp.getGender().toString() : "N/A")));
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
        appointmentRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully !");
    }
}
