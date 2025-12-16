package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.BookDto;
import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.BookStatus;
import bd.edu.seu.lms.service.BookService;
import bd.edu.seu.lms.service.PublicationService;
import bd.edu.seu.lms.service.VendorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookController {
    private final BookService bookService;
    private final PublicationService publicationService;
    private final VendorService vendorService;

    public BookController(BookService bookService, PublicationService publicationService, VendorService vendorService) {
        this.bookService = bookService;
        this.publicationService = publicationService;
        this.vendorService = vendorService;
    }

    @GetMapping("/books")
    public String books(HttpSession session, Model model, String search) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        if (search != null && !search.trim().equals("")) {
            model.addAttribute("search", search);
            model.addAttribute("books", bookService.searchBooks(search));
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("publications", publicationService.getAllPublications());
        model.addAttribute("vendors", vendorService.getAllVendors());
        model.addAttribute("bookdto", new BookDto("", "", null, null, "", 0, 0, 0.0, BookStatus.AVAILABLE, ""));
        return "books";
    }

    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute("bookdto") BookDto bookDto, RedirectAttributes redirectAttributes) {
        try {
            if (bookDto.title() == null || bookDto.title().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Title is required");
                return "redirect:/books";
            }
            if (bookDto.author() == null || bookDto.author().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Author is required");
                return "redirect:/books";
            }
            if (bookDto.publicationId() == null || bookDto.publicationId() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Publication is required");
                return "redirect:/books";
            }
            if (bookDto.vendorId() == null || bookDto.vendorId() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Vendor is required");
                return "redirect:/books";
            }
            if (bookDto.availableCopies() > bookDto.totalCopies()) {
                redirectAttributes.addFlashAttribute("error", "Available copies cannot be greater than total copies");
                return "redirect:/books";
            }

            // Fetch Publication and Vendor entities
            var publication = publicationService.getPublicationById(bookDto.publicationId());
            if (publication == null) {
                redirectAttributes.addFlashAttribute("error", "Publication not found");
                return "redirect:/books";
            }
            var vendor = vendorService.getVendorById(bookDto.vendorId());
            if (vendor == null) {
                redirectAttributes.addFlashAttribute("error", "Vendor not found");
                return "redirect:/books";
            }

            Book book = new Book();
            book.setTitle(bookDto.title());
            book.setAuthor(bookDto.author());
            book.setPublication(publication);
            book.setVendor(vendor);
            book.setCategory(bookDto.category());
            book.setTotalCopies(bookDto.totalCopies());
            book.setAvailableCopies(bookDto.availableCopies());
            book.setPricePerCopy(bookDto.pricePerCopy() != null ? bookDto.pricePerCopy() : 0.0);
            book.setStatus(book.getAvailableCopies() > 0 ? BookStatus.AVAILABLE : BookStatus.UNAVAILABLE);
            book.setDescription(bookDto.description() != null ? bookDto.description() : "");
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("success", "Book added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books";
    }

    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute BookDto bookDto, int id, RedirectAttributes redirectAttributes) {
        try {
            if (bookDto.title() == null || bookDto.title().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Title is required");
                return "redirect:/books";
            }
            if (bookDto.author() == null || bookDto.author().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Author is required");
                return "redirect:/books";
            }
            if (bookDto.publicationId() == null || bookDto.publicationId() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Publication is required");
                return "redirect:/books";
            }
            if (bookDto.vendorId() == null || bookDto.vendorId() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Vendor is required");
                return "redirect:/books";
            }
            if (bookDto.availableCopies() > bookDto.totalCopies()) {
                redirectAttributes.addFlashAttribute("error", "Available copies cannot be greater than total copies");
                return "redirect:/books";
            }

            // Fetch Publication and Vendor entities
            var publication = publicationService.getPublicationById(bookDto.publicationId());
            if (publication == null) {
                redirectAttributes.addFlashAttribute("error", "Publication not found");
                return "redirect:/books";
            }
            var vendor = vendorService.getVendorById(bookDto.vendorId());
            if (vendor == null) {
                redirectAttributes.addFlashAttribute("error", "Vendor not found");
                return "redirect:/books";
            }

            Book book = bookService.getBookById(id);
            book.setTitle(bookDto.title());
            book.setAuthor(bookDto.author());
            book.setPublication(publication);
            book.setVendor(vendor);
            book.setCategory(bookDto.category());
            book.setTotalCopies(bookDto.totalCopies());
            book.setAvailableCopies(bookDto.availableCopies());
            book.setPricePerCopy(bookDto.pricePerCopy() != null ? bookDto.pricePerCopy() : 0.0);
            book.setStatus(book.getAvailableCopies() > 0 ? BookStatus.AVAILABLE : BookStatus.UNAVAILABLE);
            book.setDescription(bookDto.description() != null ? bookDto.description() : "");
            bookService.updateBook(book);
            redirectAttributes.addFlashAttribute("success", "Book updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books";
    }

    @PostMapping("/books/delete")
    public String deleteBook(int id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Book deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books";
    }

}
