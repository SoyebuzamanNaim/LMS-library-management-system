package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.model.Subscription;
import bd.edu.seu.lms.model.SubscriptionStatus;
import bd.edu.seu.lms.repository.StudentRepo;
import bd.edu.seu.lms.repository.SubscriptionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepo subscriptionRepo;
    private final StudentRepo studentRepo;

    public SubscriptionService(SubscriptionRepo subscriptionRepo, StudentRepo studentRepo) {
        this.subscriptionRepo = subscriptionRepo;
        this.studentRepo = studentRepo;
    }

    @Transactional
    public Subscription saveSubscription(Subscription subscription) {

        if (subscription.getId() != null && subscriptionRepo.existsById(subscription.getId())) {
            throw new IllegalArgumentException("Subscription already exists");
        }
        return subscriptionRepo.save(subscription);
    }

    @Transactional
    public Subscription updateSubscription(Subscription subscription) {
        if (subscription.getId() == null || !subscriptionRepo.existsById(subscription.getId())) {
            throw new IllegalArgumentException("Subscription does not exist");
        }
        return subscriptionRepo.save(subscription);
    }

    @Transactional
    public void deleteSubscription(int id) {
        if (!subscriptionRepo.existsById(id)) {
            throw new IllegalArgumentException("Subscription does not exist");
        }

        Student student = subscriptionRepo.findByStudent_Id(id);
        student.setSubscription(null);
        studentRepo.save(student);

        subscriptionRepo.deleteById(id);

    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepo.findAll();
    }

    public long countActiveSubscriptions() {
        return subscriptionRepo.countByStatus(SubscriptionStatus.ACTIVE);
    }

    public Subscription getSubscriptionById(int id) {
        return subscriptionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription does not exist"));
    }

    public List<Subscription> searchSubscriptions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return subscriptionRepo.findAll();
        }
        return subscriptionRepo.findByStudentNameContainingIgnoreCase(keyword);
    }

    @Transactional
    public Subscription toggleSubscriptionStatus(int id) {
        Subscription subscription = getSubscriptionById(id);
        if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
        } else {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
        return subscriptionRepo.save(subscription);
    }

}
