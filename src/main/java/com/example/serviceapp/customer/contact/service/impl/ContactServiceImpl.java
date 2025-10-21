package com.example.serviceapp.customer.contact.service.impl;

import com.example.serviceapp.admin.contact.repository.ADContactRepository;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.contact.service.ContactService;
import com.example.serviceapp.customer.service.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Override
    public boolean isPhoneNumberExists(String phoneNumber) {
        return contactRepository.isPhoneNumberExists(phoneNumber);
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
            String note = customer.getContext() != null && !customer.getContext().isBlank()
                    ? customer.getContext()
                    : "(Không có)";

            // ✅ Email công ty
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(companyEmail);
            message.setSubject("📩 Có khách hàng mới đăng ký dịch vụ GSTS");
            message.setText(String.format("""
            Xin chào đội ngũ GSTS,

            Có một khách hàng mới vừa gửi thông tin đăng ký dịch vụ.

            Thông tin chi tiết:
            - Họ tên: %s
            - Số điện thoại: %s
            - Dịch vụ quan tâm: %s
            - Ghi chú: %s

            Vui lòng liên hệ sớm để xác nhận và tư vấn cho khách hàng.

            Trân trọng,
            Hệ thống website GSTS.
            """, customerName, phoneNumber, serviceName, note)
            );

            mailSender.send(message);
            System.out.println("✅ Đã gửi thông báo tới công ty: " + companyEmail);
        } catch (Exception e) {
            System.err.println("❌ Gửi email tới công ty thất bại: " + e.getMessage());
        }
    }

}
