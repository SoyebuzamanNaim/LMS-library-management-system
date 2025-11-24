package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.PublicationDto;
import bd.edu.seu.lms.model.Publication;
import bd.edu.seu.lms.service.PublicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicationController {
    private PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping("/publications")
    public String publications(HttpSession session, Model model, String search) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        if (search != null && !search.trim().equals("")) {
            model.addAttribute("search", search);
            model.addAttribute("publications", publicationService.searchPublications(search));
        } else {
            model.addAttribute("publications", publicationService.getAllPublications());
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("publicationdto", new PublicationDto("", ""));
        return "publications";
    }

    @PostMapping("/publications/save")
    public String savePublication(@ModelAttribute("publicationdto") PublicationDto publicationDto,
            RedirectAttributes redirectAttributes) {
        try {
            Publication publication = new Publication();
            if (publicationDto.name() == null || publicationDto.name().trim().equals("")) {
                throw new IllegalArgumentException("Name is required");
            }

            publication.setName(publicationDto.name());
            publication.setAddress(publicationDto.address() != null ? publicationDto.address() : "N/A");
            publicationService.savePublication(publication);
            redirectAttributes.addFlashAttribute("success", "Publication added successfully");
            return "redirect:/publications";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publications";
        }
    }

    @PostMapping("/publications/update")
    public String updatePublication(@ModelAttribute PublicationDto publicationDto, String id,
            RedirectAttributes redirectAttributes) {
        try {
            if (id == null || id.trim().equals("")) {
                throw new IllegalArgumentException("Publication ID is required");
            }
            if (publicationDto.name() == null || publicationDto.name().trim().equals("")) {
                throw new IllegalArgumentException("Name is required");
            }

            Publication publication = new Publication();
            publication.setName(publicationDto.name());
            publication.setAddress(publicationDto.address() != null ? publicationDto.address() : "N/A");
            publicationService.updatePublication(id, publication);
            redirectAttributes.addFlashAttribute("success", "Publication updated successfully");
            return "redirect:/publications";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publications";
        }
    }

    @PostMapping("/publications/delete")
    public String deletePublication(String id, RedirectAttributes redirectAttributes) {
        try {
            publicationService.deletePublication(id);
            redirectAttributes.addFlashAttribute("success", "Publication deleted successfully");
            return "redirect:/publications";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/publications";
        }
    }
}
