package com.example.serviceapp.admin.authenticate.controller;

import com.example.serviceapp.admin.authenticate.dto.ChangePasswordDTO;
import com.example.serviceapp.admin.authenticate.dto.ForgotPasswordDTO;
import com.example.serviceapp.admin.authenticate.dto.ResetPasswordDTO;
import com.example.serviceapp.admin.authenticate.repository.UserRepository;
import com.example.serviceapp.admin.authenticate.service.UserService;
import com.example.serviceapp.common.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/authenticate/register";
    }


    @PostMapping("/register")
    public String registerUser(@Validated(User.OnCreate.class) @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        // Kiểm tra lỗi validate
        if (result.hasErrors()) {
            return "admin/authenticate/register";
        }

        // Validate duplicate fields
        String duplicateError = validateDuplicateFields(user.getUsername(), user.getEmail(), user.getPhoneNumber(), null);
        if (duplicateError != null) {
            if (duplicateError.contains("Tên đăng nhập")) {
                model.addAttribute("usernameError", duplicateError);
            } else if (duplicateError.contains("Email")) {
                model.addAttribute("emailError", duplicateError);
            } else if (duplicateError.contains("Số điện thoại")) {
                model.addAttribute("phoneError", duplicateError);
            }
            return "admin/authenticate/register";
        }

        // Kiểm tra xác nhận mật khẩu
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            model.addAttribute("passwordError", "Mật khẩu xác nhận không khớp!");
            return "admin/authenticate/register";
        }

        userService.registerUser(user);
        model.addAttribute("message", "Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt.");
        return "admin/authenticate/login";
    }

    /**
     * Validate duplicate username, email, phoneNumber
     * @param username username to check
     * @param email email to check
     * @param phoneNumber phoneNumber to check
     * @param excludeUserId user ID to exclude from check (null for new user)
     * @return error message if duplicate found, null otherwise
     */
    private String validateDuplicateFields(String username, String email, String phoneNumber, Long excludeUserId) {
        if (username != null) {
            Optional<User> existingUser = userService.findByUsername(username);
            if (existingUser.isPresent() && (excludeUserId == null || !existingUser.get().getUserId().equals(excludeUserId))) {
                return "Tên đăng nhập đã được sử dụng!";
            }
        }

        if (email != null) {
            Optional<User> existingUser = userService.findByEmail(email);
            if (existingUser.isPresent() && (excludeUserId == null || !existingUser.get().getUserId().equals(excludeUserId))) {
                return "Email đã được sử dụng!";
            }
        }

        if (phoneNumber != null) {
            Optional<User> existingUser = userService.findByPhoneNumber(phoneNumber);
            if (existingUser.isPresent() && (excludeUserId == null || !existingUser.get().getUserId().equals(excludeUserId))) {
                return "Số điện thoại đã được sử dụng!";
            }
        }

        return null;
    }


    @GetMapping("/activate")
    public String activate(@RequestParam("code") String code, Model model) {
        boolean activated = userService.activateUser(code);
        model.addAttribute("message", activated ? "Kích hoạt thành công!" : "Mã không hợp lệ hoặc đã được sử dụng.");
        return "admin/authenticate/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/authenticate/login";
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        return "admin/error/access-denied";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/authenticate/forgot-password";
    }
    @PostMapping("/forgot-password")
    public String processForgotPassword(@Validated @ModelAttribute("user") ForgotPasswordDTO user,
                                        BindingResult bindingResult, Model model) {

        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            return "admin/authenticate/forgot-password";
        }
        Optional<User> userOpt = userService.findByEmail(user.getEmail());
        if (userOpt.isPresent()) {
            userService.sendResetPasswordToken(userOpt.get());
            model.addAttribute("message", "Đã gửi email đặt lại mật khẩu, vui lòng kiểm tra.");
        } else {
            model.addAttribute("message", "Không tìm thấy email trong hệ thống.");
        }
        return "admin/authenticate/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<User> userOpt = userService.findByResetToken(token);
        if (userOpt.isEmpty()) {
            model.addAttribute("message", "Liên kết không hợp lệ hoặc đã hết hạn.");
            return "admin/authenticate/login";
        }
        model.addAttribute("token", token);
        model.addAttribute("user", new ResetPasswordDTO());
        return "admin/authenticate/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam("token") String token,
            @Validated @ModelAttribute("user") ResetPasswordDTO user
           ,BindingResult bindingResult,Model model) {

        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("token", token);
            return "admin/authenticate/reset-password";
        }
        // Kiểm tra xác nhận mật khẩu
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.passwordConfirmation", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("user", user);
            model.addAttribute("token", token);
            return "admin/authenticate/reset-password";
        }

        boolean success = userService.resetPassword(token, user.getPassword());

        model.addAttribute("message", success
                ? "Đặt lại mật khẩu thành công!"
                : "Token không hợp lệ hoặc đã hết hạn.");

        return "admin/authenticate/login";
    }

    @GetMapping("/admin/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        return "admin/authenticate/change-password";
    }

    @PostMapping("/admin/change-password")
    public String handleChangePassword(
            @Validated @ModelAttribute("changePasswordDTO") ChangePasswordDTO changePasswordDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            addChangePasswordModelAttributes(model, changePasswordDTO);
            return "admin/authenticate/change-password";
        }

        // Kiểm tra xác nhận mật khẩu mới
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.passwordConfirmation", "Mật khẩu xác nhận không khớp!");
            addChangePasswordModelAttributes(model, changePasswordDTO);
            return "admin/authenticate/change-password";
        }

        // Lấy thông tin user đang đăng nhập
        Optional<User> userOpt = getCurrentAuthenticatedUser();
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Bạn cần đăng nhập để đổi mật khẩu.");
            addChangePasswordModelAttributes(model, changePasswordDTO);
            return "admin/authenticate/change-password";
        }

        User user = userOpt.get();

        // Kiểm tra mật khẩu cũ
        if (!userService.checkPassword(changePasswordDTO.getOldPassword(), user.getPassword())) {
            bindingResult.rejectValue("oldPassword", "error.oldPassword", "Mật khẩu cũ không đúng!");
            addChangePasswordModelAttributes(model, changePasswordDTO);
            return "admin/authenticate/change-password";
        }

        // Kiểm tra mật khẩu mới không được trùng với mật khẩu cũ
        if (userService.checkPassword(changePasswordDTO.getNewPassword(), user.getPassword())) {
            bindingResult.rejectValue("newPassword", "error.newPassword", "Mật khẩu mới phải khác mật khẩu cũ!");
            addChangePasswordModelAttributes(model, changePasswordDTO);
            return "admin/authenticate/change-password";
        }

        // Đổi mật khẩu
        boolean success = userService.changePassword(user.getUserId(), changePasswordDTO.getNewPassword());

        if (success) {
            model.addAttribute("showSuccessModal", true);
            model.addAttribute("successMessage", "Đổi mật khẩu thành công!");
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        } else {
            model.addAttribute("error", "Có lỗi xảy ra khi đổi mật khẩu. Vui lòng thử lại.");
            addChangePasswordModelAttributes(model, changePasswordDTO);
        }

        return "admin/authenticate/change-password";
    }

    /**
     * Get current authenticated user
     */
    private Optional<User> getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            return Optional.empty();
        }
        return userRepository.findByUsernameOrPhoneNumber(authentication.getName());
    }

    /**
     * Add common model attributes for change password form
     */
    private void addChangePasswordModelAttributes(Model model, ChangePasswordDTO changePasswordDTO) {
        model.addAttribute("changePasswordDTO", changePasswordDTO);
    }


}

