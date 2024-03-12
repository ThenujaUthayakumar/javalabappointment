package com.javalabappointment.javalabappointment.service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.javalabappointment.javalabappointment.entity.ContactEntity;
import com.javalabappointment.javalabappointment.persist.Contact;
import com.javalabappointment.javalabappointment.repository.ContactRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContactService {
    private ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /*------------------------- CREATE CONTACT --------------------------------  */
    @Transactional
    public ContactEntity store(Contact contact) throws ParseException {
        ContactEntity contactEntity=new ContactEntity();

        if(contact.getName() == null || contact.getName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Your Name !");
        }

        if(contact.getPhoneNumber() == null || contact.getPhoneNumber().isEmpty())
        {
            throw new IllegalStateException("Please Enter Your Mobile Number !");
        }

        if(contact.getAddress() == null || contact.getAddress().isEmpty())
        {
            throw new IllegalStateException("Please Enter Your Address !");
        }

        if(contact.getMessage() == null || contact.getMessage().isEmpty())
        {
            throw new IllegalStateException("Please Enter Your Message !");
        }

        contactEntity.setName(contact.getName());
        contactEntity.setPhoneNumber(contact.getPhoneNumber());
        contactEntity.setAddress(contact.getAddress());
        contactEntity.setMessage(contact.getMessage());

        return contactRepository.save(contactEntity);
    }

    /*-------------------------- GET ALL CONTACTS RECORDS ----------------------*/
    public Page<ContactEntity> getAll(Integer pageNo, Integer pageSize, String orderBy, Contact contact) {
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
        if(contact.getSearch() != null){
            searchLike = "%"+contact.getSearch()+"%";
        }

        Page<ContactEntity> contactEntities;

        contactEntities=contactRepository.findAllContact(pageable,
                contact.getId(),
                contact.getName(),
                contact.getAddress(),
                contact.getPhoneNumber(),
                contact.getMessage(),
                searchLike);

        return contactEntities;
    }

    /*----------------------- DOWNLOAD CONTACT DETAILS ----------------------------*/
    public void download(Integer pageNo, Integer pageSize, String orderBy, Contact contact, Integer fileType, String downloadColumn, HttpServletResponse response) throws IOException {
        Page<ContactEntity> list = getAll(pageNo, pageSize, orderBy,contact);
        String[] requiredColumns;
        if (downloadColumn != null) {
            requiredColumns = downloadColumn.split(",");
        } else {
            requiredColumns = new String[]{"id","name","address","phoneNumber","message","created_at"};
        }

        List<String> columnNames = new ArrayList<>();

        for (String columns : requiredColumns) {
            if (columns.matches("id")) {
                columnNames.add("Contact ID");
            }
            if (columns.matches("name")) {
                columnNames.add("Contactor Name");
            }
            if (columns.matches("address")) {
                columnNames.add("Contactor Address");
            }
            if (columns.matches("phoneNumber")) {
                columnNames.add("Contactor Mobile Number");
            }
            if (columns.matches("message")) {
                columnNames.add("Contactor Message");
            }
            if (columns.matches("created_at")) {
                columnNames.add("Contact Date");
            }
        }
        if (fileType == 1) {

            response.setHeader("Content-Disposition", "attachment; filename=contact.pdf");

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
                for (ContactEntity emp : list) {
                    for (String col : requiredColumns) {
                        if (col.matches("id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getId() != null ? emp.getId().toString() : "N/A")));
                        }
                        if (col.matches("name")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getName() != null ? emp.getName() : "N/A")));
                        }
                        if (col.matches("address")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getAddress() != null ? emp.getAddress() :"N/A")));
                        }
                        if (col.matches("phoneNumber")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getPhoneNumber() != null ? emp.getPhoneNumber().toString() : "N/A")));
                        }
                        if (col.matches("message")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getMessage() != null ? emp.getMessage().toString() : "N/A")));
                        }
                        if (col.matches("created_at")) {
                            LocalDateTime createdAtDateTime = emp.getCreatedAt();
                            String formattedCreatedAt = "N/A";

                            if (createdAtDateTime != null) {
                                try {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    formattedCreatedAt = createdAtDateTime.format(formatter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(formattedCreatedAt)));
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
        contactRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully !");
    }


    public Page<Map<Object,String>> getStatistics(Integer pageNo, Integer pageSize, String orderBy, Contact contact) {
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
        if(contact.getSearch() != null){
            searchLike = "%"+contact.getSearch()+"%";
        }

        Page<Map<Object,String>> contactEntities;

        contactEntities=contactRepository.findStatistics(pageable,
                contact.getId(),
                contact.getName(),
                contact.getAddress(),
                contact.getPhoneNumber(),
                contact.getMessage(),
                searchLike);

        return contactEntities;
    }
}
