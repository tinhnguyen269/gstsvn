package com.example.serviceapp.customer.contact.service.impl;

import com.example.serviceapp.admin.contact.repository.ADContactRepository;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.contact.service.ContactService;
import com.example.serviceapp.customer.service.repository.ServiceRepository;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final ADContactRepository contactRepository;
    private final ServiceRepository serviceRepository;

    @Value("${company.email}")
    private String companyEmail;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public ContactServiceImpl(ADContactRepository contactRepository, ServiceRepository serviceRepository) {
        this.contactRepository = contactRepository;
        this.serviceRepository = serviceRepository;
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

            // ✅ Gửi email qua SendGrid API
            Email from = new Email(companyEmail, "GSTS Support");
            Email to = new Email(companyEmail);
            Content content = new Content("text/html", contentHtml);
            Mail mail = new Mail(from, "📩 Có khách hàng mới đăng ký dịch vụ GSTS", to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ Đã gửi thông báo tới công ty: " + companyEmail);
            } else {
                System.err.println("⚠️ Gửi email thất bại, mã phản hồi: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email tới công ty: " + e.getMessage());
        }
    }
}
