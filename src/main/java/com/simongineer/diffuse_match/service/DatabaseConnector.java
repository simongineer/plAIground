package com.simongineer.diffuse_match.service;

import jakarta.persistence.*;

public class DatabaseConnector {
    private final EntityManagerFactory emf;

    public DatabaseConnector() {
        emf = Persistence.createEntityManagerFactory("main");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}