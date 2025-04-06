package com.chedi.artifact.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sim_card_subscriptions")
public class SimCardSubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String simNumber; // Numéro de la carte SIM
    private String subscriptionType; // Type d'abonnement (prépayé, postpayé, etc.)
    private String planName; // Nom du forfait (par exemple, "Forfait 10 Go", "Forfait Illimité")
    private Integer dataLimit; // Limite de données en Mo ou Go
    private Integer callLimit; // Limite d'appels en minutes
    private Integer smsLimit; // Limite de SMS
    private LocalDateTime activationDate; // Date d'activation de l'abonnement
    private LocalDateTime expirationDate; // Date d'expiration de l'abonnement
    private Boolean isActive; // Statut de l'abonnement (actif ou inactif)
    
}
