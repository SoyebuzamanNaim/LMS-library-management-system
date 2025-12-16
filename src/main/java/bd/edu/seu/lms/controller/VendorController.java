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

        model.addAttribute("search", search);
        model.addAttribute("vendors", vendorService.searchVendors(search));

        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("vendordto", new VendorDto("", "", "", null, ""));
        return "vendors";
    }

    @PostMapping("/vendors/save")
    public String vendors(@ModelAttribute("vendordto") VendorDto vendorDto, RedirectAttributes redirectAttributes) {
        try {

            Vendor vendor = new Vendor();
            vendor.setName(vendorDto.name().trim());
            vendor.setContactPerson(vendorDto.contactPerson().trim());
            vendor.setEmail(vendorDto.email().trim());
            vendor.setPhones(vendorDto.phones());
            vendor.setAddress(vendorDto.address().trim());
            vendorService.saveVendor(vendor);
            redirectAttributes.addFlashAttribute("success", "Vendor added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vendors";
    }

    @PostMapping("/vendors/update")
    public String updateVendor(@ModelAttribute VendorDto vendorDto, RedirectAttributes redirectAttributes) {
        try {

            Vendor vendor = new Vendor();
            vendor.setName(vendorDto.name().trim());
            vendor.setContactPerson(vendorDto.contactPerson().trim());
            vendor.setEmail(vendorDto.email().trim());
            vendor.setPhones(vendorDto.phones());
            vendor.setAddress(vendorDto.address().trim());
            vendorService.updateVendor(vendor);
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
