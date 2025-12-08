package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.BookDto;
import bd.edu.seu.lms.model.Book;
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
    private BookService bookService;
    private PublicationService publicationService;
    private VendorService vendorService;

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
        model.addAttribute("bookdto", new BookDto("", "", "", "", "", 0, 0, 0.0, "Available", ""));
        return "books";
    }

    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute("bookdto") BookDto bookDto, RedirectAttributes redirectAttributes) {
        try {
            if (bookDto.title() == null || bookDto.title().trim().equals("")) {
                throw new IllegalArgumentException("Title is required");
            }
            if (bookDto.author() == null || bookDto.author().trim().equals("")) {
                throw new IllegalArgumentException("Author is required");
            }
            if (bookDto.category() == null || bookDto.category().trim().equals("")) {
                throw new IllegalArgumentException("Category is required");
            }
            if (bookDto.totalCopies() == null || bookDto.totalCopies() < 0) {
                throw new IllegalArgumentException("Total copies must be a positive number");
            }
            if (bookDto.availableCopies() == null || bookDto.availableCopies() < 0) {
                throw new IllegalArgumentException("Available copies must be a positive number");
            }
            if (bookDto.pricePerCopy() == null || bookDto.pricePerCopy() < 0) {
                throw new IllegalArgumentException("Price per copy must be a positive number");
            }
            if (bookDto.publicationId() == null || bookDto.publicationId().trim().equals("")) {
                throw new IllegalArgumentException("Publication is required");
            }
            if (bookDto.vendorId() == null || bookDto.vendorId().trim().equals("")) {
                throw new IllegalArgumentException("Vendor is required");
            }

            Book book = new Book();
            book.setTitle(bookDto.title());
            book.setAuthor(bookDto.author());
            book.setPublicationId(bookDto.publicationId());
            book.setVendorId(bookDto.vendorId());
            book.setCategory(bookDto.category());
            book.setTotalCopies(bookDto.totalCopies() != null ? bookDto.totalCopies() : 0);
            book.setAvailableCopies(
                    bookDto.availableCopies() != null ? bookDto.availableCopies() : bookDto.totalCopies());
            // Validate that available copies cannot exceed total copies
            if (book.getAvailableCopies() != null && book.getTotalCopies() != null
                    && book.getAvailableCopies() > book.getTotalCopies()) {
                throw new IllegalArgumentException("Available copies cannot be greater than total copies");
            }
            book.setPricePerCopy(bookDto.pricePerCopy() != null ? bookDto.pricePerCopy() : 0.0);
            // Set status based on available copies
            book.setStatus(
                    book.getAvailableCopies() != null && book.getAvailableCopies() > 0 ? "Available" : "Unavailable");
            book.setDescription(bookDto.description() != null ? bookDto.description() : "");
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("success", "BookRepo added successfully");
            return "redirect:/books";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books";
        }
    }

    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute BookDto bookDto, String id, RedirectAttributes redirectAttributes) {
        try {
            if (id == null || id.trim().equals("")) {
                throw new IllegalArgumentException("BookRepo ID is required");
            }
            if (bookDto.title() == null || bookDto.title().trim().equals("")) {
                throw new IllegalArgumentException("Title is required");
            }
            if (bookDto.author() == null || bookDto.author().trim().equals("")) {
                throw new IllegalArgumentException("Author is required");
            }
            if (bookDto.category() == null || bookDto.category().trim().equals("")) {
                throw new IllegalArgumentException("Category is required");
            }
            if (bookDto.totalCopies() == null || bookDto.totalCopies() < 0) {
                throw new IllegalArgumentException("Total copies must be a positive number");
            }
            if (bookDto.availableCopies() == null || bookDto.availableCopies() < 0) {
                throw new IllegalArgumentException("Available copies must be a positive number");
            }
            if (bookDto.pricePerCopy() == null || bookDto.pricePerCopy() < 0) {
                throw new IllegalArgumentException("Price per copy must be a positive number");
            }
            if (bookDto.publicationId() == null || bookDto.publicationId().trim().equals("")) {
                throw new IllegalArgumentException("Publication is required");
            }
            if (bookDto.vendorId() == null || bookDto.vendorId().trim().equals("")) {
                throw new IllegalArgumentException("Vendor is required");
            }

            Book book = new Book();
            book.setTitle(bookDto.title());
            book.setAuthor(bookDto.author());
            book.setPublicationId(bookDto.publicationId());
            book.setVendorId(bookDto.vendorId());
            book.setCategory(bookDto.category());
            book.setTotalCopies(bookDto.totalCopies() != null ? bookDto.totalCopies() : 0);
            book.setAvailableCopies(bookDto.availableCopies() != null ? bookDto.availableCopies() : 0);
            // Validate that available copies cannot exceed total copies
            if (book.getAvailableCopies() != null && book.getTotalCopies() != null
                    && book.getAvailableCopies() > book.getTotalCopies()) {
                throw new IllegalArgumentException("Available copies cannot be greater than total copies");
            }
            book.setPricePerCopy(bookDto.pricePerCopy() != null ? bookDto.pricePerCopy() : 0.0);
            // Set status based on available copies
            book.setStatus(
                    book.getAvailableCopies() != null && book.getAvailableCopies() > 0 ? "Available" : "Unavailable");
            book.setDescription(bookDto.description() != null ? bookDto.description() : "");
            bookService.updateBook(id, book);
            redirectAttributes.addFlashAttribute("success", "BookRepo updated successfully");
            return "redirect:/books";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books";
        }
    }

    @PostMapping("/books/delete")
    public String deleteBook(String id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", "BookRepo deleted successfully");
            return "redirect:/books";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books";
        }
    }
}
