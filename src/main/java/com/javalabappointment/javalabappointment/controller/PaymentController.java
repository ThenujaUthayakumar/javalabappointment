package com.javalabappointment.javalabappointment.controller;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.PaymentEntity;
import com.javalabappointment.javalabappointment.persist.Appointment;
import com.javalabappointment.javalabappointment.persist.Payment;
import com.javalabappointment.javalabappointment.service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    private JavaMailSender emailSender;

    /*------------------------------- CREATE PAYMENT WITH MAIL SEND BILL ----------------------------*/
    @PostMapping("/store")
    public PaymentEntity store(@RequestBody Payment payment) throws ParseException {
        PaymentEntity savedPayment = paymentService.store(payment);

        try {
            byte[] billPdf = paymentService.generateBill(payment);

            //PaymentEntity appointment = paymentService.getAppointmentDetails(payment.getAppointmentId().getId());
            Integer appointmentId = payment.getAppointmentId().getId();
            String recipientEmail = paymentService.getAppointmentDetails(appointmentId);

                if (recipientEmail != null) {
                    String subject = "Payment Receipt";
                    String message = "Thank you for your payment. Please find attached your payment receipt.";
                    sendEmailWithPDF(recipientEmail, subject, message, billPdf);
                }
                else
                {
                    throw new RuntimeException("Failed to send email with bill attached");
                }
        }catch (IOException | MessagingException e) {
            e.printStackTrace(); // Handle exception appropriately
            e.printStackTrace(); // Handle exception appropriately
            throw new RuntimeException("Failed to send email with bill attached");
        }

        return savedPayment;
    }

//    private void sendBillEmail(Payment payment, byte[] billPdf) throws MessagingException {
//        PaymentEntity appointment = paymentService.getAppointmentDetails(payment.getAppointmentId());
//        String recipientEmail = appointment.getAppointmentId().getEmail();
//        String subject = "Payment Receipt";
//        String message = "Thank you for your payment. Please find attached your payment receipt.";
//        sendEmailWithPDF(recipientEmail, subject, message, billPdf);
//    }


    /*-------------------------- SET EMAIL TO SUBJECTS IN THIS CODE WITH ATTACHMENT ------------------------*/
    private void sendEmailWithPDF(String to, String subject, String text, byte[] pdfBytes) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.addAttachment("payment_receipt.pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        }
    }
//
//    /*----------------- SET RECORDS FOR PAYMENT RECEIPT PDF GENERATE --------------------*/
//    private byte[] generatePDF(Payment payment) throws IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        // Generate PDF using a library like iText or Apache PDFBox
//        // Here you should write code to generate a PDF containing payment details
//        // For simplicity, I'm assuming a dummy PDF generation
//        outputStream.write("Payment Receipt\n\n".getBytes());
//        outputStream.write(("Amount: " + payment.getAmount() + "\n").getBytes());
//        outputStream.write(("Date: " + payment.getCreatedAt() + "\n").getBytes());
//        // Add more payment details as needed
//        byte[] pdfBytes = outputStream.toByteArray();
//        outputStream.close();
//        return pdfBytes;
//    }

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
