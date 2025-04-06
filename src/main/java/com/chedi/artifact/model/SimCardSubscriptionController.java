package com.chedi.artifact.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sim-card-subscriptions")
public class SimCardSubscriptionController {

private final SimCardSubscriptionService simCardSubscriptionService;

public SimCardSubscriptionController(SimCardSubscriptionService simCardSubscriptionService) {
    this.simCardSubscriptionService = simCardSubscriptionService;
}

// Créer une nouvelle souscription
@PostMapping("/add")
public ResponseEntity<SimCardSubscriptionEntity> createSimCardSubscription(@RequestBody SimCardSubscriptionEntity subscription) {
    SimCardSubscriptionEntity createdSubscription = simCardSubscriptionService.createSimCardSubscription(subscription);
    return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
}

// Lire une souscription par son ID
@GetMapping("/{id}")
public ResponseEntity<SimCardSubscriptionEntity> getSimCardSubscriptionById(@PathVariable Long id) {
    Optional<SimCardSubscriptionEntity> subscription = simCardSubscriptionService.getSimCardSubscriptionById(id);
    return subscription.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
}

// Lire toutes les souscriptions
@GetMapping("/getall")
public ResponseEntity<List<SimCardSubscriptionEntity>> getAllSimCardSubscriptions() {
    List<SimCardSubscriptionEntity> subscriptions = simCardSubscriptionService.getAllSimCardSubscriptions();
    return new ResponseEntity<>(subscriptions, HttpStatus.OK);
}

// Mettre à jour une souscription
@PutMapping("/{id}")
public ResponseEntity<SimCardSubscriptionEntity> updateSimCardSubscription(@PathVariable Long id, @RequestBody SimCardSubscriptionEntity updatedSubscription) {
    SimCardSubscriptionEntity subscription = simCardSubscriptionService.updateSimCardSubscription(id, updatedSubscription);
    return subscription != null ? new ResponseEntity<>(subscription, HttpStatus.OK)
            : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
}

// Supprimer une souscription
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteSimCardSubscription(@PathVariable Long id) {
    boolean isDeleted = simCardSubscriptionService.deleteSimCardSubscription(id);
    return isDeleted ? ResponseEntity.noContent().build()
            : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
}
}
