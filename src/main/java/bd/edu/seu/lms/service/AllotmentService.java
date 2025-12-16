
package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Allotment;

import bd.edu.seu.lms.repository.AllotmentRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllotmentService {
    private final AllotmentRepo allotmentRepo;

    public AllotmentService(AllotmentRepo allotmentRepo) {
        this.allotmentRepo = allotmentRepo;
    }

    public Allotment saveAllotment(Allotment allotment) {
        if (allotmentRepo.existsById(allotment.getId())) {
            throw new IllegalArgumentException("Allotment already exists");
        }
        return allotmentRepo.save(allotment);
    }

    public Allotment updateAllotment(Allotment allotment) {
        if (allotment.getId() == null || !allotmentRepo.existsById(allotment.getId())) {
            throw new IllegalArgumentException("Allotment does not exist");
        }
        return allotmentRepo.save(allotment);
    }

    public void deleteAllotment(int id) {
        try {
            allotmentRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Allotment does not exist");
        }
    }

    public List<Allotment> getAllAllotments() {
        return allotmentRepo.findAll();
    }

    public Allotment getAllotmentById(int id) {
        return allotmentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Allotment does not exist"));
    }

    public double calculateFine(LocalDate issueDate) {
        LocalDate plannedReturnDate = issueDate.plusDays(14);

        LocalDate today = LocalDate.now();
        // planned date passed
        if (!today.isAfter(plannedReturnDate)) {
            return 0;
        }

        // 20 takafor each day after the planned return date
        long overdueDays = ChronoUnit.DAYS.between(plannedReturnDate, today);
        // isfai
        return overdueDays * 20.0;
    }

    public List<Allotment> searchAllotments(String keyword) {
        if (keyword == null || keyword.trim().equals("")) {
            return getAllAllotments();
        }
        // isfai
        List<Allotment> studentMatches = allotmentRepo.findByStudent_NameContainingIgnoreCase(keyword);
        List<Allotment> titleMatches = allotmentRepo.findByBook_TitleContainingIgnoreCase(keyword);
        List<Allotment> combined = new ArrayList<>();

        combined.addAll(studentMatches);
        combined.addAll(titleMatches);

        return combined.stream().distinct().collect(Collectors.toList());
    }
}
