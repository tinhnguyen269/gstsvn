package com.example.serviceapp.customer.contact.controller;

import com.example.serviceapp.common.constants.CONTACT_STATUS;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.customer.contact.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/lien-he")
public class ContactController {

    private final ContactService contactService;


    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/them-moi")
    @ResponseBody
    public Map<String, Object> doAdd(@Validated Customer customer,
                                     BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();

        // Bước 1: Nếu có lỗi binding hoặc lỗi validate annotation -> trả về luôn
        if (bindingResult.hasFieldErrors("serviceId")) {
            bindingResult.rejectValue("serviceId", "error.customer", "Dịch vụ không hợp lệ.");
        }
        if (bindingResult.hasErrors()) {
            return buildErrorResponse(bindingResult);
        }

        // Bước 2: Kiểm tra logic nghiệp vụ
        if (!contactService.isServiceIdExists(customer.getServiceId())) {
            bindingResult.rejectValue("serviceId", "error.customer", "Dịch vụ không tồn tại.");
        }
        if (contactService.isPhoneNumberExists(customer.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "error.customer", "Số điện thoại đã được sử dụng.");
        }

        // Bước 3: Nếu lỗi logic -> trả về luôn
        if (bindingResult.hasErrors()) {
            return buildErrorResponse(bindingResult);
        }

        // Bước 4: Lưu dữ liệu
        customer.setStatus(CONTACT_STATUS.PENDING);
        contactService.addCustomer(customer);
        contactService.sendNotificationToCompany(customer);

        response.put("status", "success");
        response.put("message", "Đăng kí thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.");
        return response;
    }

    private Map<String, Object> buildErrorResponse(BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        response.put("status", "error");
        response.put("errors", errors);
        return response;
    }

}
