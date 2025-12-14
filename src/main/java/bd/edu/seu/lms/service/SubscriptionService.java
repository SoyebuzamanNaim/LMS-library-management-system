package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Subscription;
import bd.edu.seu.lms.model.SubscriptionStatus;
import bd.edu.seu.lms.repository.SubscriptionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepo subscriptionRepo;

    public SubscriptionService(SubscriptionRepo subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    public Subscription saveSubscription(Subscription subscription) {
        if (subscription.getId() != null) {
            throw new IllegalArgumentException("Subscription already exists");
        }
        return subscriptionRepo.save(subscription);
    }

    public Subscription updateSubscription(Subscription subscription) {
        if (subscription.getId() == null) {
            throw new IllegalArgumentException("Subscription does not exist");
        }
        return subscriptionRepo.save(subscription);
    }

    public void deleteSubscription(int id) {
        try {
            subscriptionRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Subscription does not exist");
        }
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepo.findAll();
    }

    public Subscription getSubscriptionById(int id) {
        return subscriptionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription does not exist"));
    }

    // search by student name
    public List<Subscription> searchSubscriptions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return subscriptionRepo.findAll();
        }
        return subscriptionRepo.findByStudent_NameContainingIgnoreCase(keyword);
    }

    public Subscription toggleSubscriptionStatus(int id) {
        Subscription subscription = getSubscriptionById(id);
        subscription.setStatus(subscription.getStatus() == SubscriptionStatus.ACTIVE ? SubscriptionStatus.EXPIRED
                : SubscriptionStatus.ACTIVE);
        return subscriptionRepo.save(subscription);
    }
}
