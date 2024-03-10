package com.javalabappointment.javalabappointment.controller;

import com.javalabappointment.javalabappointment.entity.PaymentEntity;
import com.javalabappointment.javalabappointment.persist.Payment;
import com.javalabappointment.javalabappointment.service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
        PaymentEntity paymentEntity=new PaymentEntity();

        try {
            byte[] billPdf = paymentService.generateBill(payment);

            List<PaymentEntity> appointment = paymentService.findAllPayment();
            Integer appointmentId = payment.getAppointmentId().getId();
            String recipientEmail = paymentService.getAppointmentDetails(appointmentId);

                if (recipientEmail != null) {
                    String subject = "Payment Receipt and Appointment Confirmation";
                    String message = "Dear";

                    for (PaymentEntity payments : appointment) {
                        String patientName = payments.getAppointmentId().getName();
                        String appointmentDateTime = payments.getAppointmentId().getAppointmentDateTime();

                        message += patientName + " - Appointment Date & Time: " + appointmentDateTime + ", ";
                    }

                    message += "\n\n"
                            + "Location: ABC Laboratory, Wijerama Mawatha, Colombo 07\n\n"
                            + "Please ensure that you arrive 30 Minutes before your appointment time to complete any necessary paperwork and prepare for your testing.\n\n"
                            + "We would like to thank you for your payment. Below We're attach the Payment receipt.\n\n"
                            + "If you have any questions or concerns regarding your appointment or payment receipt, please feel free to contact us at +94 0115 333 666.\n\n"
                            + "Thank You For Choosing Us !\n\n"
                            + "Sincerely,\n"
                            + "[ABC Laboratory]\n"
                            + "[+94 0115 333 666]\n"
                            + "<img src='cid:clinicLogo' alt='Clinic Logo'><br>";

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
