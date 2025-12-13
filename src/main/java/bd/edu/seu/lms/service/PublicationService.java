package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Publication;
import bd.edu.seu.lms.repository.PublicationRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PublicationService {
    private final PublicationRepo publicationRepo;

    public PublicationService(PublicationRepo publicationRepo) {
        this.publicationRepo = publicationRepo;
    }

    public Publication savePublication(Publication publication) {
        return publicationRepo.save(publication);
    }

    public Publication updatePublication(int id, Publication publication) {
        return publicationRepo.findById(id).map(existing -> {
            existing.setName(publication.getName());
            existing.setAddress(publication.getAddress());
            return publicationRepo.save(existing);
        }).orElse(null);
    }

    public void deletePublication(int id) {
        if (publicationRepo.existsById(id)) {
            publicationRepo.deleteById(id);
        }
    }

    public ArrayList<Publication> getAllPublications() {
        return new ArrayList<>(publicationRepo.findAll());
    }

    public Publication getPublicationById(int id) {
        return publicationRepo.findById(id).orElse(null);
    }

    public ArrayList<Publication> searchPublications(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(publicationRepo.findAll());
        }
        String lowerKeyword = keyword.toLowerCase();
        return publicationRepo.findAll().stream().filter(p -> {
            String name = p.getName() != null ? p.getName().toLowerCase() : "";
            String address = p.getAddress() != null ? p.getAddress().toLowerCase() : "";
            return name.contains(lowerKeyword) || address.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
