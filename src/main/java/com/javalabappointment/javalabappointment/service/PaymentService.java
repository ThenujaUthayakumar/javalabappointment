package com.javalabappointment.javalabappointment.service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.PaymentEntity;
import com.javalabappointment.javalabappointment.entity.TestEntity;
import com.javalabappointment.javalabappointment.persist.Payment;
import com.javalabappointment.javalabappointment.repository.AppointmentRepository;
import com.javalabappointment.javalabappointment.repository.PaymentRepository;
import com.javalabappointment.javalabappointment.repository.TestRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private PaymentRepository paymentRepository;
    private AppointmentRepository appointmentRepository;
    private TestRepository testRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AppointmentRepository appointmentRepository,
                          TestRepository testRepository) {
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
        this.testRepository = testRepository;
    }

    /*------------------------------- CREATE PAYMENT ----------------------------*/
    @Transactional
    public PaymentEntity store(Payment payment) throws ParseException {
        PaymentEntity paymentEntity=new PaymentEntity();

        if(payment.getAmount() == null)
        {
            throw new IllegalStateException("Please Enter Payment Amount !");
        }

        String cardNumberString = String.valueOf(payment.getCardNumber());
        if(payment.getCardNumber() == null || cardNumberString.length()!=16)
        {
            throw new IllegalStateException("Please Enter Your Card Number !");
        }

        String cvvString = String.valueOf(payment.getCvv());
        if(payment.getCvv() == null || cvvString.length()!=3)
        {
            throw new IllegalStateException("Please Enter Your Card CVV Number !");
        }

        if(payment.getExpiryDate() == null || payment.getExpiryDate().isEmpty())
        {
            throw new IllegalStateException("Please Enter Your Card Expiry Date !");
        }

        if(payment.getCardHolderName() == null || payment.getCardHolderName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Your Name Of Card !");
        }

        if(payment.getCardHolderPhoneNumber() == null || payment.getCardHolderPhoneNumber().isEmpty())
        {
            throw new IllegalStateException("Please Enter Your OTP Get Mobile Number !");
        }

        AppointmentEntity appointmentEntity=appointmentRepository.findById(payment.getAppointmentId().getId()).orElse(null);
        if (appointmentEntity==null)
        {
            throw new IllegalStateException("Patient Not Found !");
        }

        PaymentEntity existingPayment = paymentRepository.findByAppointmentId(payment.getAppointmentId());
        if (existingPayment != null) {
            throw new IllegalStateException("Payment for this appointment already exists !");
        }

        paymentEntity.setAppointmentId(payment.getAppointmentId());
        paymentEntity.setAmount(payment.getAmount());
        paymentEntity.setCardNumber(payment.getCardNumber());
        paymentEntity.setCvv(payment.getCvv());
        paymentEntity.setExpiryDate(payment.getExpiryDate());
        paymentEntity.setReferenceNumber(this.getReferencenumber());
        paymentEntity.setStatus(1);
        paymentEntity.setCardHolderName(payment.getCardHolderName());
        paymentEntity.setCardHolderPhoneNumber(payment.getCardHolderPhoneNumber());

        return paymentRepository.save(paymentEntity);
    }

    /*------------------------ SET AUTO REFERENCE NUMBER FOR PAYMENT ------------------*/
    public String getReferencenumber(){
        String referenceNumber=paymentRepository.findReferenceNumber();
        if(referenceNumber==null){
            return "INVOICE1";
        }
        else{
            String[] splitString=referenceNumber.split("INVOICE");
            int newReferenceNumber=Integer.valueOf(splitString[1])+1;
            String finalReferenceNumber="INVOICE"+newReferenceNumber;
            return finalReferenceNumber;
        }
    }

    /*------------------------------ GET ALL PAYMENT RECORDS --------------------------------  */
    public Page<PaymentEntity> getAll(Integer pageNo, Integer pageSize, String orderBy, Payment payment) {
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
        if(payment.getSearch() != null){
            searchLike = "%"+payment.getSearch()+"%";
        }

        Page<PaymentEntity> paymentEntities;

        paymentEntities=paymentRepository.findAllPayment(pageable,
                payment.getId(),
                payment.getAmount(),
                payment.getAppointmentId(),
                payment.getReferenceNumber(),
                payment.getStatus(),
                searchLike);

        return paymentEntities;
    }

    /*------------------------------ DOWNLOAD PAYMENT RECORDS --------------------------------  */
    public void download(Integer pageNo, Integer pageSize, String orderBy, Payment payment, Integer fileType, String downloadColumn, HttpServletResponse response) throws IOException {
        Page<PaymentEntity> list = getAll(pageNo, pageSize, orderBy,payment);
        String[] requiredColumns;
        if (downloadColumn != null) {
            requiredColumns = downloadColumn.split(",");
        } else {
            requiredColumns = new String[]{"id","name","phoneNumber","patientName","patientNumber",
            "referenceNo","test","amount","status"};
        }

        List<String> columnNames = new ArrayList<>();

        for (String columns : requiredColumns) {
            if (columns.matches("id")) {
                columnNames.add("Payment No");
            }
            if (columns.matches("name")) {
                columnNames.add("Payer Name");
            }
            if (columns.matches("phoneNumber")) {
                columnNames.add("Payer Mobile Number");
            }
            if (columns.matches("patientName")) {
                columnNames.add("Patient Name");
            }
            if (columns.matches("patientNumber")) {
                columnNames.add("Patient Mobile Number");
            }
            if (columns.matches("referenceNo")) {
                columnNames.add("Invoice No");
            }
            if (columns.matches("test")) {
                columnNames.add("Test");
            }
            if (columns.matches("amount")) {
                columnNames.add("Test Amount");
            }
            if (columns.matches("status")) {
                columnNames.add("Status");
            }
        }
        if (fileType == 1) {

            response.setHeader("Content-Disposition", "attachment; filename=payment.pdf");

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
                for (PaymentEntity emp : list) {
                    AppointmentEntity appointmentEntity=appointmentRepository.findById(emp.getAppointmentId().getId()).orElse(null);
                    TestEntity testEntity=testRepository.findById(appointmentEntity.getTestId().getId()).orElse(null);

                    for (String col : requiredColumns) {
                        if (col.matches("id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getId() != null ? emp.getId().toString() : "")));
                        }
                        if (col.matches("name")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getCardHolderName() != null ? emp.getCardHolderName() : "")));
                        }
                        if (col.matches("phoneNumber")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getCardHolderPhoneNumber() != null ? emp.getCardHolderPhoneNumber() : "")));
                        }
                        if (col.matches("patientName")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(appointmentEntity.getName() != null ? appointmentEntity.getName().toString() : "")));
                        }
                        if (col.matches("patientNumber")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(appointmentEntity.getPhoneNumber() != null ? appointmentEntity.getPhoneNumber().toString() : "")));
                        }
                        if (col.matches("referenceNo")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getReferenceNumber() != null ? emp.getReferenceNumber().toString() : "")));
                        }
                        if (col.matches("test")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(testEntity.getName() != null ? testEntity.getName().toString() : "")));
                        }
                        if (col.matches("amount")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getAmount() != null ? emp.getAmount().toString() : "")));
                        }
                        if (col.matches("status")) {
                            String status="Payment Success";
                            if (emp.getStatus()==1)
                            {
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
}
