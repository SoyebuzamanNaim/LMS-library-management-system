package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Publication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PublicationService {
    private final ArrayList<Publication> publications = new ArrayList<>();
    private long ind = 1;

    public void savePublication(Publication publication) {
        if (publications.stream().anyMatch(p -> p.getId().equals(publication.getId()))) {
            throw new IllegalArgumentException("Publication already exists");
        }
        if (publications.stream().anyMatch(p -> p.getName().equals(publication.getName()))) {
            throw new IllegalArgumentException("Publication with this name already exists");
        }
        publication.setId(Long.toString(ind++));
        publications.add(publication);
    }

    public void updatePublication(String id, Publication publication) {
        if (!publications.stream().anyMatch(p -> p.getId().equals(id))) {
            throw new IllegalArgumentException("Publication not found");
        }
        publications.stream().filter(p -> p.getId().equals(id)).findFirst()
                .ifPresent(p -> p.setName(publication.getName()));
        publications.stream().filter(p -> p.getId().equals(id)).findFirst()
                .ifPresent(p -> p.setAddress(publication.getAddress()));
    }

    public void deletePublication(String id) {
        if (!publications.stream().anyMatch(p -> p.getId().equals(id))) {
            throw new IllegalArgumentException("Publication not found");
        }
        publications.removeIf(p -> p.getId().equals(id));
    }

    public ArrayList<Publication> getAllPublications() {
        return new ArrayList<>(publications);
    }

    public Publication getPublicationById(String id) {
        return publications.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public ArrayList<Publication> searchPublications(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(publications);
        }
        String lowerKeyword = keyword.toLowerCase();
        return publications.stream().filter(p -> {
            String name = p.getName() != null ? p.getName().toLowerCase() : "";
            String address = p.getAddress() != null ? p.getAddress().toLowerCase() : "";
            return name.contains(lowerKeyword) || address.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    // Initialize with dummy data
    public PublicationService() {
        initializeDummyData();
    }

    private void initializeDummyData() {
        Publication p1 = new Publication();
        p1.setId(Long.toString(ind++));
        p1.setName("Dhaka University Press");
        p1.setAddress("Dhaka, Bangladesh");
        publications.add(p1);

        Publication p2 = new Publication();
        p2.setId(Long.toString(ind++));
        p2.setName("Oxford University Press");
        p2.setAddress("Oxford, UK");
        publications.add(p2);

        Publication p3 = new Publication();
        p3.setId(Long.toString(ind++));
        p3.setName("Southeast University Press");
        p3.setAddress("Dhaka, Bangladesh");
        publications.add(p3);

        Publication p4 = new Publication();
        p4.setId(Long.toString(ind++));
        p4.setName("Rokomari");
        p4.setAddress("Dhaka, Bangladesh");
        publications.add(p4);

        Publication p5 = new Publication();
        p5.setId(Long.toString(ind++));
        p5.setName("Prothom Alo");
        p5.setAddress("Dhaka, Bangladesh");
        publications.add(p5);
    }
}
