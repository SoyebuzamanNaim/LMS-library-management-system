package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.model.Subscription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final ArrayList<Subscription> subscriptions = new ArrayList<>();
    private long ind = 1;
    private final StudentService studentService;

    public SubscriptionService(StudentService studentService) {
        this.studentService = studentService;
        initializeDummyData();
    }

    public void saveSubscription(Subscription subscription) {
        if (subscription.getStudentId() == null || subscription.getStudentId().trim().equals("")) {
            throw new IllegalArgumentException("Student ID is required");
        }
        if (studentService.getStudentById(subscription.getStudentId()) == null) {
            throw new IllegalArgumentException("Student not found");
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
        if (subscription.getId() != null && subscriptions.stream()
                .anyMatch(s -> s.getId().equals(subscription.getId()))) {
            throw new IllegalArgumentException("Subscription with this id already exists");
        }
        // Check if the student already has an  subscription
        if (subscriptions.stream()
                .anyMatch(s -> s.getStudentId().equals(subscription.getStudentId()))) {
            throw new IllegalArgumentException("This student already has an  subscription");
        }

        subscription.setId(Long.toString(ind++));
        // Auto-calculate end date as start date + 30 days
        if (subscription.getEndDate() == null) {
            subscription.setEndDate(subscription.getStartDate().plusDays(30));
        }
        subscription.setStatus(calculateStatus(subscription.getEndDate()));
        subscriptions.add(subscription);
    }

    public void updateSubscription(String id, Subscription subscription) {
        // Check if the student already has an subscription


        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("Subscription ID is required");
        }
        if (subscriptions.stream().anyMatch(s -> s.getStudentId().equals(subscription.getStudentId()))) {
            throw new IllegalArgumentException("This student already has an subscription");
        }
        if (!subscriptions.stream().anyMatch(s -> s.getId().equals(id))) {
            throw new IllegalArgumentException("Subscription not found");
        }
        if (subscription.getStudentId() == null || subscription.getStudentId().trim().equals("")) {
            throw new IllegalArgumentException("Student is required");
        }
        if (studentService.getStudentById(subscription.getStudentId()) == null) {
            throw new IllegalArgumentException("Student not found");
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

        subscriptions.stream().filter(s -> s.getId().equals(id)).findFirst().ifPresent(s -> {
            s.setStudentId(subscription.getStudentId());
            s.setType(subscription.getType());
            s.setStartDate(subscription.getStartDate());
            // Auto-calculate end date as start date + 30 days
            s.setEndDate(subscription.getStartDate().plusDays(30));
            s.setStatus(calculateStatus(s.getEndDate()));
        });
    }

    public void deleteSubscription(String id) {
        if (!subscriptions.stream().anyMatch(s -> s.getId().equals(id))) {
            throw new IllegalArgumentException("Subscription not found");
        }
        subscriptions.removeIf(s -> s.getId().equals(id));
    }

    public ArrayList<Subscription> getAllSubscriptions() {
        return new ArrayList<>(subscriptions);
    }

    public Subscription getSubscriptionById(String id) {
        return subscriptions.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public ArrayList<Subscription> searchSubscriptions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(subscriptions);
        }
        String lowerKeyword = keyword.toLowerCase();
        return subscriptions.stream().filter(s -> {
            String studentId = s.getStudentId() != null ? s.getStudentId().toLowerCase() : "";
            String studentName = "";
            var student = studentService.getStudentById(s.getStudentId());
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

    private void initializeDummyData() {
        // Wait a bit to ensure StudentService is initialized
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return;
        }


        for (int i = 0; i < 4; i++) {
            Subscription subscription = new Subscription();
            subscription.setId(Long.toString(ind++));
            subscription.setStudentId(students.get(i).getId());
            subscription.setType(i == 0 ? "Premium" : (i == 1 ? "Standard" : "Basic"));
            subscription.setStartDate(LocalDate.now().minusDays(i * 5)); // Different start dates
            subscription.setEndDate(subscription.getStartDate().plusDays(30));
            subscription.setStatus(calculateStatus(subscription.getEndDate()));
            subscriptions.add(subscription);
        }
    }

}
