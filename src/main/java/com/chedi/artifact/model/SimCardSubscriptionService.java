package com.chedi.artifact.model;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SimCardSubscriptionService {
    private final SimCardRepository repository;

    public SimCardSubscriptionService(SimCardRepository repository) {
        this.repository = repository;
    }

    // Créer une nouvelle souscription
    @Transactional
    public SimCardSubscriptionEntity createSimCardSubscription(SimCardSubscriptionEntity subscription) {
        return repository.save(subscription);
    }

    // Lire une souscription par son ID
    public Optional<SimCardSubscriptionEntity> getSimCardSubscriptionById(Long id) {
        return repository.findById(id);
    }

    // Lire toutes les souscriptions
    public List<SimCardSubscriptionEntity> getAllSimCardSubscriptions() {
        return repository.findAll();
    }

    // Mettre à jour une souscription
    @Transactional
    public SimCardSubscriptionEntity updateSimCardSubscription(Long id, SimCardSubscriptionEntity updatedSubscription) {
        Optional<SimCardSubscriptionEntity> existingSubscription = repository.findById(id);
        if (existingSubscription.isPresent()) {
            updatedSubscription.setId(id); // assurez-vous que l'ID reste le même
            return repository.save(updatedSubscription);
        } else {
            throw new RuntimeException("Subscription with id " + id + " not found.");
        }
    }

    // Supprimer une souscription
    @Transactional
    public boolean deleteSimCardSubscription(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Subscription with id " + id + " not found.");
        }
    }
}
