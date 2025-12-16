package com.example.serviceapp.customer.contact.service.impl;

import com.example.serviceapp.admin.contact.repository.ADContactRepository;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.contact.service.ContactService;
import com.example.serviceapp.customer.service.repository.ServiceRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final ADContactRepository contactRepository;
    private final ServiceRepository serviceRepository;
    private final JavaMailSender mailSender;

    @Value("${company.email}")
    private String companyEmail;

    public ContactServiceImpl(ADContactRepository contactRepository, ServiceRepository serviceRepository, JavaMailSender mailSender) {
        this.contactRepository = contactRepository;
        this.serviceRepository = serviceRepository;
        this.mailSender = mailSender;
    }

    @Override
    public void addCustomer(Customer customer) {
        contactRepository.save(customer);
    }

    @Override
    public boolean isServiceIdExists(Long serviceId) {
        return serviceRepository.existsById(serviceId);
    }

    @Async
    @Override
    public void sendNotificationToCompany(Customer customer) {
        try {
            String customerName = customer.getName();
            String phoneNumber = customer.getPhoneNumber();
            String serviceName = serviceRepository.findById(customer.getServiceId())
                    .map(Services::getServiceName)
                    .orElse("Không xác định");
            String note = (customer.getContext() != null && !customer.getContext().isBlank())
                    ? customer.getContext()
                    : "(Không có)";

            // ✅ Nội dung HTML email
            String contentHtml = """
            <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h3>Xin chào đội ngũ GSTS,</h3>
                    <p>Có một khách hàng mới vừa gửi thông tin đăng ký dịch vụ.</p>

                    <table style="border-collapse: collapse; font-size: 14px;">
                        <tr><td><strong>Họ tên:</strong></td><td>%s</td></tr>
                        <tr><td><strong>Số điện thoại:</strong></td><td>%s</td></tr>
                        <tr><td><strong>Dịch vụ quan tâm:</strong></td><td>%s</td></tr>
                        <tr><td><strong>Ghi chú:</strong></td><td>%s</td></tr>
                    </table>

                    <p>Vui lòng liên hệ sớm để xác nhận và tư vấn cho khách hàng.</p>

                    <p>Trân trọng,<br/>Hệ thống website GSTS.</p>
                </body>
            </html>
            """.formatted(customerName, phoneNumber, serviceName, note);

            // ✅ Gửi email qua Spring Boot Mail
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(companyEmail, "GSTS Support");
            helper.setTo(companyEmail);
            helper.setSubject("📩 Có khách hàng mới đăng ký dịch vụ GSTS");
            helper.setText(contentHtml, true);
            
            mailSender.send(message);
            System.out.println("✅ Đã gửi thông báo tới công ty: " + companyEmail);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email tới công ty: " + e.getMessage());
        }
    }
}
