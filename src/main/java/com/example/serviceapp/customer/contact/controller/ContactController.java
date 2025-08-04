package com.example.serviceapp.customer.contact.controller;

import com.example.serviceapp.common.constants.CONTACT_STATUS;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.customer.contact.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contact/add")
    public String doAdd(Customer customer) {
        customer.setStatus(CONTACT_STATUS.PENDING.getStatus());
        contactService.addCustomer(customer);
        return "redirect:/home?success=true";
    }
}
