package com.example.serviceapp.admin.contact.controller;

import com.example.serviceapp.common.constants.CONTACT_STATUS;
import com.example.serviceapp.admin.contact.service.ADContactService;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class ADContactController {

    public final ADContactService contactService;
    public ADContactController(ADContactService contactService) {
        this.contactService = contactService;
    }


    @PostMapping("/contact/add")
    public String doAdd(Customer customer) {
        customer.setStatus(CONTACT_STATUS.PENDING.getStatus());
        contactService.addCustomer(customer);
        return "redirect:/admin/contact/list";
    }


    @GetMapping("/contact/list")
    public String listCustomer(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false, defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Customer> customerPage;

        if (keyword != null && !keyword.isEmpty()) {
            customerPage = contactService.searchCustomer(keyword, pageable);
        } else {
            customerPage = contactService.findAll(pageable);
        }
        List<Services> services = contactService.getAllServices();
        Map<Long, String> serviceMap = services.stream()
                .collect(Collectors.toMap(Services::getServiceId, Services::getServiceName));

        model.addAttribute("serviceMap", serviceMap);
        model.addAttribute("services", services);
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("customerNew", new Customer());

        return "admin/contact/contact";
    }

    @GetMapping("/contact/edit/{id}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id, Model model) {
        Customer customer = contactService.findById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/contact/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        Customer existing = contactService.findById(id);
        if (existing != null) {
            existing.setName(updatedCustomer.getName());
            existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
            existing.setServiceId(updatedCustomer.getServiceId());
            existing.setContext(updatedCustomer.getContext());
            existing.setStatus(updatedCustomer.getStatus());
            contactService.save(existing);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/contact/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        Customer customer = contactService.findById(id);
        if (customer != null) {
            contactService.delete(customer);
        }
        return "redirect:/admin/contact/list";


    }
}

