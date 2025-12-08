package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.VendorDto;
import bd.edu.seu.lms.model.Vendor;
import bd.edu.seu.lms.service.VendorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VendorController {
    private VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/vendors")
    public String vendors(HttpSession session, Model model, String search) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        if (search != null && !search.trim().equals("")) {
            model.addAttribute("search", search);
            model.addAttribute("vendors", vendorService.searchVendors(search));
        } else {
            model.addAttribute("vendors", vendorService.getAllVendors());
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("vendordto", new VendorDto("", "", "", "", ""));
        return "vendors";
    }

    @PostMapping("/vendors/save")
    public String vendors(@ModelAttribute("vendordto") VendorDto vendorDto, RedirectAttributes redirectAttributes) {
        try {
            if (vendorDto.name() == null || vendorDto.name().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Name is required");
                return "redirect:/vendors";
            }
            if (vendorDto.contactPerson() == null || vendorDto.contactPerson().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Contact Person is required");
                return "redirect:/vendors";
            }
            if (vendorDto.email() == null || vendorDto.email().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email is required");
                return "redirect:/vendors";
            }
            if (vendorDto.phone() == null || vendorDto.phone().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Phone is required");
                return "redirect:/vendors";
            }
            Vendor vendor = new Vendor();
            vendor.setName(vendorDto.name());
            vendor.setContactPerson(vendorDto.contactPerson());
            vendor.setEmail(vendorDto.email());
            vendor.setPhone(vendorDto.phone());
            vendor.setAddress(vendorDto.address() != null ? vendorDto.address() : "N/A");
            vendorService.saveVendor(vendor);
            redirectAttributes.addFlashAttribute("success", "Vendor added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vendors";
    }

    @PostMapping("/vendors/update")
    public String updateVendor(@ModelAttribute VendorDto vendorDto, int id, RedirectAttributes redirectAttributes) {
        try {
            if (vendorDto.name() == null || vendorDto.name().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Name is required");
                return "redirect:/vendors";
            }
            if (vendorDto.contactPerson() == null || vendorDto.contactPerson().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Contact Person is required");
                return "redirect:/vendors";
            }
            if (vendorDto.email() == null || vendorDto.email().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email is required");
                return "redirect:/vendors";
            }
            if (vendorDto.phone() == null || vendorDto.phone().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Phone is required");
                return "redirect:/vendors";
            }
            Vendor vendor = new Vendor();
            vendor.setName(vendorDto.name());
            vendor.setContactPerson(vendorDto.contactPerson());
            vendor.setEmail(vendorDto.email());
            vendor.setPhone(vendorDto.phone());
            vendor.setAddress(vendorDto.address() != null ? vendorDto.address() : "N/A");
            if (vendorService.updateVendor(id, vendor) == null) {
                redirectAttributes.addFlashAttribute("error", "Vendor not found");
                return "redirect:/vendors";
            }
            redirectAttributes.addFlashAttribute("success", "Vendor updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vendors";
    }

    @PostMapping("/vendors/delete")
    public String deleteVendor(int id, RedirectAttributes redirectAttributes) {
        try {
            vendorService.deleteVendor(id);
            redirectAttributes.addFlashAttribute("success", "Vendor deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vendors";
    }
}
