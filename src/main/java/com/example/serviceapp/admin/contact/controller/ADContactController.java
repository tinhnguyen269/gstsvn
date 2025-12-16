package com.example.serviceapp.admin.contact.controller;

import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.admin.service.service.ADServiceService;
import com.example.serviceapp.common.constants.CONTACT_STATUS;
import com.example.serviceapp.admin.contact.service.ADContactService;
import com.example.serviceapp.common.entity.Customer;
import com.example.serviceapp.common.entity.Services;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class ADContactController {

    public final ADContactService contactService;
    public final ADServiceService serviceService;
    private final UserRepository userRepository;

    public ADContactController(ADContactService contactService, ADServiceService serviceService, UserRepository userRepository) {
        this.contactService = contactService;
        this.serviceService = serviceService;
        this.userRepository = userRepository;
    }

    @PostMapping("/contact/add")
    @ResponseBody
    public ResponseEntity<?> addCustomer(@ModelAttribute @Validated Customer customer,
                                         BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        if (!serviceService.isServiceIdExists(customer.getServiceId())) {
            errors.put("serviceId", "Dịch vụ không tồn tại.");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // Lấy thông tin user đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            userRepository.findByUsernameOrPhoneNumber(username).ifPresent(user -> {
                customer.setCreateBy(user.getUserId());
            });
        }

        customer.setStatus(CONTACT_STATUS.PENDING);
        contactService.addCustomer(customer);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Thêm khách hàng thành công");
        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id,
            @ModelAttribute @Valid Customer updatedCustomer,
            BindingResult result) {

        Map<String, String> errors = new HashMap<>();

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }

        if (!serviceService.isServiceIdExists(updatedCustomer.getServiceId())) {
            errors.put("serviceId", "Dịch vụ không tồn tại.");
        }

        if (!EnumSet.allOf(CONTACT_STATUS.class).contains(updatedCustomer.getStatus())) {
            errors.put("status", "Trạng thái không tồn tại.");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Customer existing = contactService.findById(id);
        Map<String, String> response = new HashMap<>();

        if (existing != null) {
            // Lấy thông tin user đăng nhập
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                userRepository.findByUsernameOrPhoneNumber(username).ifPresent(user -> {
                    existing.setUpdateBy(user.getUserId());
                });
            }

            existing.setName(updatedCustomer.getName());
            existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
            existing.setServiceId(updatedCustomer.getServiceId());
            existing.setContext(updatedCustomer.getContext());
            existing.setStatus(updatedCustomer.getStatus());
            contactService.save(existing);
            response.put("message", "Sửa khách hàng thành công");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Khách hàng không tồn tại");
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/contact/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        Customer customer = contactService.findById(id);
        if (customer != null) {
            contactService.delete(customer);
            return ResponseEntity.ok(Map.of("message", "Xóa khách hàng thành công!"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Không tìm thấy khách hàng!"));
    }
    @PostMapping("/contact/delSelected")
    @ResponseBody
    public ResponseEntity<?> deleteListContacts(@RequestParam("ids") List<Long> ids) {
        contactService.softDeleteContacts(ids);
        return ResponseEntity.ok(Map.of("message", "Xóa " + ids.size() + " khách hàng thành công!"));
    }
}

