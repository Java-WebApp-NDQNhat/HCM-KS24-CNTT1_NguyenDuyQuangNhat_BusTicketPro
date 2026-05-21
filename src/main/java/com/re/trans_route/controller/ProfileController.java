package com.re.trans_route.controller;

import com.re.trans_route.dto.ChangePasswordDTO;
import com.re.trans_route.dto.ProfileUpdateDTO;
import com.re.trans_route.entity.User;
import com.re.trans_route.service.ProfileService;
import com.re.trans_route.util.AvatarPaths;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @ModelAttribute("profileForm")
    public ProfileUpdateDTO profileForm() {
        return new ProfileUpdateDTO();
    }

    @ModelAttribute("passwordForm")
    public ChangePasswordDTO passwordForm() {
        return new ChangePasswordDTO();
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        populateProfileModel(userDetails, model);
        return "page/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @ModelAttribute("profileForm") ProfileUpdateDTO profileForm,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            populateProfileModel(userDetails, model);
            model.addAttribute("profileForm", profileForm);
            return "page/profile";
        }

        profileService.updateProfile(userDetails.getUsername(), profileForm);
        redirectAttributes.addFlashAttribute("profileSuccess", "Cập nhật hồ sơ thành công!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @Valid @ModelAttribute("passwordForm") ChangePasswordDTO passwordForm,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Mật khẩu xác nhận không khớp");
        }

        if (result.hasErrors()) {
            populateProfileModel(userDetails, model);
            model.addAttribute("passwordForm", passwordForm);
            return "page/profile";
        }

        try {
            profileService.changePassword(
                    userDetails.getUsername(),
                    passwordForm.getCurrentPassword(),
                    passwordForm.getNewPassword());
            redirectAttributes.addFlashAttribute("passwordSuccess", "Đổi mật khẩu thành công!");
            return "redirect:/profile";
        } catch (IllegalArgumentException ex) {
            result.rejectValue("currentPassword", "invalid", ex.getMessage());
            populateProfileModel(userDetails, model);
            model.addAttribute("passwordForm", passwordForm);
            return "page/profile";
        }
    }

    private void populateProfileModel(UserDetails userDetails, Model model) {
        User user = profileService.getByEmail(userDetails.getUsername());
        String roleSlug = user.getRole() != null ? user.getRole().getSlug() : "PASSENGER";

        model.addAttribute("user", user);
        model.addAttribute("profileForm", profileService.toProfileDto(user));
        model.addAttribute("avatarUrl", AvatarPaths.forRoleSlug(roleSlug));
        model.addAttribute("roleLabel", AvatarPaths.roleLabel(roleSlug));
        model.addAttribute("pageKind", pageKindForRole(roleSlug));
    }

    private static String pageKindForRole(String roleSlug) {
        if (roleSlug == null) {
            return "passenger";
        }
        return switch (roleSlug.toUpperCase()) {
            case "ADMIN" -> "admin";
            case "STAFF" -> "staff";
            default -> "passenger";
        };
    }
}
