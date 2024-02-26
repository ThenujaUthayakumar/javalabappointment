package com.javalabappointment.javalabappointment.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
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

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /*--------------------- GET APPOINTMENT EMAIL FOR PAYMENT RECEIPT SENT --------------*/
    public String getAppointmentDetails(Integer appointmentId) {
        AppointmentEntity payment= appointmentRepository.findAppointmentWithRecipientEmailById(appointmentId);
        if (payment != null && payment.getEmail() != null) {
            return payment.getEmail();
        }
        return null;
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


    /*------------------- BILL FORMAT FOR PAYMENT ----------------------------------*/
    public byte[] generateBill(Payment payment) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            try {
                PdfFont font = PdfFontFactory.createFont("Helvetica");
                try {
                    Image logo = new Image(ImageDataFactory.create("src/main/resources/images/logo.png"));
                    logo.setWidth(100);
                    document.add(logo);
                } catch (IOException e) {
                    System.err.println("Failed to load logo image: " + e.getMessage());
                }
                addBillHeader(document, font, payment);
                addCompanyDetails(document, font);
               // addCustomerDetails(document, font, payment);
                addItemizedList(document, font, payment);
                //addTotalAmount(document, font, payment);
            } finally {
                document.close();
            }
            return outputStream.toByteArray();
        }

        private void addBillHeader(Document document, PdfFont font, Payment payment) {
            Paragraph header = new Paragraph("Payment Receipt").setFont(font).setFontSize(20);
            header.setTextAlignment(TextAlignment.CENTER);
            document.add(header);

            payment.setReferenceNumber(getReferencenumber()); // Assuming getReferencenumber() returns a value
            Paragraph billNo = new Paragraph("Bill No: " + payment.getReferenceNumber()).setFont(font);
            billNo.setTextAlignment(TextAlignment.RIGHT);

            AppointmentEntity appointment = appointmentRepository.findAppointmentWithRecipientEmailById(payment.getAppointmentId().getId());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());
            Paragraph date = new Paragraph("Bill Date: " + currentDateTime).setFont(font);
            date.setTextAlignment(TextAlignment.RIGHT);

            Paragraph appointmentDate = new Paragraph("Appointment Date: " + appointment.getAppointmentDateTime()).setFont(font);
            appointmentDate.setTextAlignment(TextAlignment.RIGHT);
            document.add(date);
            document.add(appointmentDate);
            document.add(billNo);

            // Add patient name and phone number in the header
            Paragraph customerInfo = new Paragraph()
                    .add(new Text("Patient Name : ").setFont(font))
                    .add(new Text(payment.getCardHolderName()).setFont(font).setBold())
                    .add("\n")
                    .add(new Text("Mobile Number   : ").setFont(font))
                    .add(new Text(payment.getCardHolderPhoneNumber()).setFont(font).setBold())
                    .add("\n")
                    .add(new Text("Appointment Number : ").setFont(font))
                    .add(new Text(appointment.getReferenceNumber()).setFont(font).setBold())
                    .add("\n");
            customerInfo.setTextAlignment(TextAlignment.LEFT);
            document.add(customerInfo);

            document.add(new Paragraph("\n"));
        }

        private void addCompanyDetails(Document document, PdfFont font) {
            Paragraph companyName = new Paragraph("Your Company Name").setFont(font).setBold();
            Paragraph companyContact = new Paragraph("Contact: +1234567890").setFont(font);
            document.add(companyName);
            document.add(companyContact);

            document.add(new Paragraph("\n"));
        }

//        private void addCustomerDetails(Document document, PdfFont font, Payment payment) {
//            Table table = new Table(new float[]{1, 3}).useAllAvailableWidth();
//            table.setBorder(new SolidBorder(new DeviceRgb(169, 169, 169), 1));
//            table.setBackgroundColor(new DeviceRgb(240, 240, 240));
//
//            table.addCell(createCell("Customer Name:", font, true));
//            table.addCell(createCell(payment.getCardHolderName(), font, false));
//            table.addCell(createCell("Email:", font, true));
//            table.addCell(createCell(payment.getCardHolderPhoneNumber(), font, false));
//            // Add more customer details as needed
//
//            document.add(table);
//            document.add(new Paragraph("\n")); // Add empty line
//        }

    private void addItemizedList(Document document, PdfFont font, Payment payment) {
        DecimalFormat df = new DecimalFormat("#.00");

        AppointmentEntity appointment = appointmentRepository.findAppointmentWithRecipientEmailById(payment.getAppointmentId().getId());
        if (appointment != null && appointment.getTestId().getName() != null) {
            Table table = new Table(new float[]{3, 1}).useAllAvailableWidth();
            table.setBorder(new SolidBorder(new DeviceRgb(169, 169, 169), 1));

            table.addCell(createCell("Test Name", font, true).setBackgroundColor(new DeviceRgb(220, 220, 220)));
            table.addCell(createCell("Amount (LKR)", font, true).setBackgroundColor(new DeviceRgb(220, 220, 220)));
            table.addCell(createCell(appointment.getTestId().getName(), font, false));
            table.addCell(createCell(df.format(payment.getAmount()), font, false));

            table.addCell(createCell("Total Amount:", font, false).setBackgroundColor(new DeviceRgb(220, 220, 220)));
            table.addCell(createCell("Rs " + df.format(payment.getAmount()), font, false).setBackgroundColor(new DeviceRgb(220, 220, 220)));

            document.add(table);
            document.add(new Paragraph("\n"));
        }
    }


//        private void addTotalAmount(Document document, PdfFont font, Payment payment) {
//            DecimalFormat df = new DecimalFormat("#.00");
//
//            Paragraph totalAmountParagraph = new Paragraph("Total Amount: Rs " + df.format(payment.getAmount()))
//                    .setFont(font)
//                    .setBold()
//                    .setMarginTop(10); // Adjust top margin for spacing
//            totalAmountParagraph.setTextAlignment(TextAlignment.RIGHT);
//            document.add(totalAmountParagraph);
//        }

        private Cell createCell(String content, PdfFont font, boolean isBold) {
            Cell cell = new Cell().add(new Paragraph(content).setFont(font));
            cell.setBorder(Border.NO_BORDER);
            if (isBold) {
                cell.setFont(font).setBold();
            }
            return cell;
        }
}
