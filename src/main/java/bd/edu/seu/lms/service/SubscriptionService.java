package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Subscription;
import bd.edu.seu.lms.repository.SubscriptionRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final SubscriptionRepo subscriptionRepo;
    private final StudentService studentService;

    public SubscriptionService(SubscriptionRepo subscriptionRepo, StudentService studentService) {
        this.subscriptionRepo = subscriptionRepo;
        this.studentService = studentService;
    }

    public Subscription saveSubscription(Subscription subscription) {
        // Validate foreign key relationship
        if (subscription.getStudent() == null) {
            throw new IllegalArgumentException("Student is required");
        }

        if (subscription.getType() == null || subscription.getType().trim().equals("")) {
            throw new IllegalArgumentException("Subscription type is required");
        }
        if (!subscription.getType().equals("Premium") && !subscription.getType().equals("Standard")
                && !subscription.getType().equals("Basic")) {
            throw new IllegalArgumentException("Subscription type must be Premium, Standard, or Basic");
        }
        if (subscription.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        // Check if the student already has a subscription
        if (subscriptionRepo.findAll().stream()
                .anyMatch(s -> s.getStudent() != null && subscription.getStudent() != null
                        && s.getStudent().getId() == subscription.getStudent().getId())) {
            throw new IllegalArgumentException("This student already has a subscription");
        }

        // Auto-calculate end date as start date + 1 year
        if (subscription.getEndDate() == null) {
            subscription.setEndDate(subscription.getStartDate().plusYears(1));
        }
        subscription.setStatus(calculateStatus(subscription.getEndDate()));
        return subscriptionRepo.save(subscription);
    }

    public Subscription updateSubscription(int id, Subscription subscription) {
        // Validate foreign key relationship
        if (subscription.getStudent() == null) {
            throw new IllegalArgumentException("Student is required");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("Subscription ID is required");
        }
        if (subscriptionRepo.findAll().stream()
                .anyMatch(s -> s.getStudent() != null && subscription.getStudent() != null
                        && s.getStudent().getId() == subscription.getStudent().getId() && s.getId() != id)) {
            throw new IllegalArgumentException("This student already has a subscription");
        }
        if (!subscriptionRepo.existsById(id)) {
            throw new IllegalArgumentException("Subscription not found");
        }

        if (subscription.getType() == null || subscription.getType().trim().equals("")) {
            throw new IllegalArgumentException("Subscription type is required");
        }
        if (!subscription.getType().equals("Premium") && !subscription.getType().equals("Standard")
                && !subscription.getType().equals("Basic")) {
            throw new IllegalArgumentException("Subscription type must be Premium, Standard, or Basic");
        }
        if (subscription.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        return subscriptionRepo.findById(id).map(existing -> {
            existing.setStudent(subscription.getStudent());
            existing.setType(subscription.getType());
            existing.setStartDate(subscription.getStartDate());
            // Auto-calculate end date as start date + 1 year
            existing.setEndDate(subscription.getStartDate().plusYears(1));
            existing.setStatus(calculateStatus(existing.getEndDate()));
            return subscriptionRepo.save(existing);
        }).orElse(null);
    }

    public void deleteSubscription(int id) {
        if (!subscriptionRepo.existsById(id)) {
            throw new IllegalArgumentException("Subscription not found");
        }
        subscriptionRepo.deleteById(id);
    }

    public ArrayList<Subscription> getAllSubscriptions() {
        return new ArrayList<>(subscriptionRepo.findAll());
    }

    public Subscription getSubscriptionById(int id) {
        return subscriptionRepo.findById(id).orElse(null);
    }

    public ArrayList<Subscription> searchSubscriptions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(subscriptionRepo.findAll());
        }
        String lowerKeyword = keyword.toLowerCase();
        return subscriptionRepo.findAll().stream().filter(s -> {
            String studentId = s.getStudent() != null ? Integer.toString(s.getStudent().getId()) : "";
            String studentName = "";
            var student = s.getStudent();
            if (student != null && student.getName() != null) {
                studentName = student.getName().toLowerCase();
            }
            String type = s.getType() != null ? s.getType().toLowerCase() : "";
            String status = s.getStatus() != null ? s.getStatus().toLowerCase() : "";
            return studentName.contains(lowerKeyword) || type.contains(lowerKeyword)
                    || status.contains(lowerKeyword) || studentId.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private String calculateStatus(LocalDate endDate) {
        if (endDate == null) {
            return "Active";
        }
        return endDate.isBefore(LocalDate.now()) ? "Expired" : "Active";
    }

    // Dummy data seeding removed
}
