package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.Publication;
import bd.edu.seu.lms.repository.BookRepo;
import bd.edu.seu.lms.repository.PublicationRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PublicationService {
    private final PublicationRepo publicationRepo;
    private final BookRepo bookRepo;

    public PublicationService(PublicationRepo publicationRepo, BookRepo bookRepo) {
        this.publicationRepo = publicationRepo;
        this.bookRepo = bookRepo;
    }

    @Transactional
    public Publication savePublication(Publication publication) {
        if (publication.getId() != null && publicationRepo.existsById(publication.getId())) {
            throw new IllegalArgumentException("Publication already exists");
        }
        return publicationRepo.save(publication);
    }

    @Transactional
    public Publication updatePublication(Publication publication) {
        if (publication.getId() == null || !publicationRepo.existsById(publication.getId())) {
            throw new IllegalArgumentException("Publication does not exist");
        }
        return publicationRepo.save(publication);
    }

    @Transactional
    public void deletePublication(int id) {
        if (!publicationRepo.existsById(id)) {
            throw new IllegalArgumentException("Publication does not exist");
        }

        List<Book> books = bookRepo.findByPublication_Id(id);

        if (!books.isEmpty()) {
            String middle="";
            for(Book b:books){middle+=(b.getTitle()+", ");}
            String s = "Cannot delete publication " +
                    middle
                    + ".";
            throw new IllegalArgumentException(s);
        }

       
            publicationRepo.deleteById(id);
        
    }

    public List<Publication> getAllPublications() {
        return publicationRepo.findAll();
    }

    public Publication getPublicationById(int id) {
        return publicationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication does not exist"));
    }

    public List<Publication> searchPublications(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return publicationRepo.findAll();
        }
        return publicationRepo.findByNameContainingIgnoreCase(keyword);
    }

}
