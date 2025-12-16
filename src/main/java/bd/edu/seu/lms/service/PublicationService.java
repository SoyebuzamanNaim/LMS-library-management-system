package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Publication;
import bd.edu.seu.lms.repository.PublicationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationService {
    private final PublicationRepo publicationRepo;

    public PublicationService(PublicationRepo publicationRepo) {
        this.publicationRepo = publicationRepo;
    }

    public Publication savePublication(Publication publication) {
        if (publicationRepo.existsById(publication.getId())) {
            throw new IllegalArgumentException("Publication already exists");
        }
        return publicationRepo.save(publication);
    }

    public Publication updatePublication(Publication publication) {
        if (publication.getId() == null || !publicationRepo.existsById(publication.getId())) {
            throw new IllegalArgumentException("Publication does not exist");
        }
        return publicationRepo.save(publication);
    }

    public void deletePublication(int id) {

        try {
            publicationRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Publication does not exist");
        }
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
