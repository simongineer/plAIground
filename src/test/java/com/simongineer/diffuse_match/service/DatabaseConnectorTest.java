package com.simongineer.diffuse_match.service;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import com.simongineer.diffuse_match.beans.Prompt;
import com.simongineer.diffuse_match.beans.StableDiffusionPrompt;

public class DatabaseConnectorTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private DatabaseConnector databaseConnector;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(databaseConnector.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void testCreate() {
        StableDiffusionPrompt entity = new StableDiffusionPrompt();
        databaseConnector.getEntityManager().persist(entity);

        Mockito.verify(entityManager, Mockito.times(1)).persist(entity);
    }

    @Test
    public void testRead() {
        databaseConnector.getEntityManager().find(Prompt.class, 1);

        Mockito.verify(entityManager, Mockito.times(1)).find(Prompt.class, 1);
    }

    @Test
    public void testUpdate() {
        StableDiffusionPrompt entity = new StableDiffusionPrompt();
        databaseConnector.getEntityManager().merge(entity);

        Mockito.verify(entityManager, Mockito.times(1)).merge(entity);
    }

    @Test
    public void testDelete() {
        StableDiffusionPrompt entity = new StableDiffusionPrompt();
        databaseConnector.getEntityManager().remove(entity);

        Mockito.verify(entityManager, Mockito.times(1)).remove(entity);
    }
}