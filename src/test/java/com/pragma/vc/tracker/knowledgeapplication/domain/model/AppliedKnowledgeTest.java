package com.pragma.vc.tracker.knowledgeapplication.domain.model;

import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AppliedKnowledgeTest {

    @Test
    void shouldCreateAppliedKnowledgeWithValidData() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        KnowledgeId knowledgeId = KnowledgeId.of(1L);
        LevelId levelId = LevelId.of(1L);
        LocalDate startDate = LocalDate.now();

        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, knowledgeId, levelId, startDate
        );

        assertNotNull(appliedKnowledge);
        assertEquals(projectId, appliedKnowledge.getProjectId());
        assertEquals(pragmaticId, appliedKnowledge.getPragmaticId());
        assertEquals(knowledgeId, appliedKnowledge.getKnowledgeId());
        assertEquals(levelId, appliedKnowledge.getLevelId());
        assertEquals(startDate, appliedKnowledge.getStartDate());
        assertNull(appliedKnowledge.getId());
    }

    @Test
    void shouldCreateAppliedKnowledgeWithNullKnowledgeAndLevel() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        LocalDate startDate = LocalDate.now();

        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, startDate
        );

        assertNotNull(appliedKnowledge);
        assertNull(appliedKnowledge.getKnowledgeId());
        assertNull(appliedKnowledge.getLevelId());
        assertFalse(appliedKnowledge.hasKnowledge());
        assertFalse(appliedKnowledge.hasLevel());
    }

    @Test
    void shouldThrowExceptionWhenProjectIdIsNull() {
        PragmaticId pragmaticId = PragmaticId.of(1L);

        assertThrows(IllegalArgumentException.class, () ->
                AppliedKnowledge.create(null, pragmaticId, null, null, LocalDate.now())
        );
    }

    @Test
    void shouldThrowExceptionWhenPragmaticIdIsNull() {
        ProjectId projectId = ProjectId.of(1L);

        assertThrows(IllegalArgumentException.class, () ->
                AppliedKnowledge.create(projectId, null, null, null, LocalDate.now())
        );
    }

    @Test
    void shouldUpdateKnowledge() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, LocalDate.now()
        );

        KnowledgeId knowledgeId = KnowledgeId.of(5L);
        appliedKnowledge.updateKnowledge(knowledgeId);

        assertEquals(knowledgeId, appliedKnowledge.getKnowledgeId());
        assertTrue(appliedKnowledge.hasKnowledge());
    }

    @Test
    void shouldUpdateLevel() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        LevelId levelId = LevelId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, levelId, LocalDate.now()
        );

        LevelId newLevelId = LevelId.of(3L);
        appliedKnowledge.updateLevel(newLevelId);

        assertEquals(newLevelId, appliedKnowledge.getLevelId());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingLevelToNull() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, LocalDate.now()
        );

        assertThrows(IllegalArgumentException.class, () ->
                appliedKnowledge.updateLevel(null)
        );
    }

    @Test
    void shouldUpdateStartDate() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        LocalDate oldDate = LocalDate.of(2023, 1, 1);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, oldDate
        );

        LocalDate newDate = LocalDate.of(2024, 6, 15);
        appliedKnowledge.updateStartDate(newDate);

        assertEquals(newDate, appliedKnowledge.getStartDate());
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, LocalDate.now()
        );

        assertThrows(IllegalArgumentException.class, () ->
                appliedKnowledge.updateStartDate(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsInFuture() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, LocalDate.now()
        );

        LocalDate futureDate = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                appliedKnowledge.updateStartDate(futureDate)
        );
    }

    @Test
    void shouldBeAbleToDeleteWhenNoKnowledgeOrLevel() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, LocalDate.now()
        );

        assertTrue(appliedKnowledge.canBeDeleted());
    }

    @Test
    void shouldNotBeAbleToDeleteWhenHasKnowledge() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        KnowledgeId knowledgeId = KnowledgeId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, knowledgeId, null, LocalDate.now()
        );

        assertFalse(appliedKnowledge.canBeDeleted());
    }

    @Test
    void shouldNotBeAbleToDeleteWhenHasLevel() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        LevelId levelId = LevelId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, levelId, LocalDate.now()
        );

        assertFalse(appliedKnowledge.canBeDeleted());
    }

    @Test
    void shouldPreventIdChangeOnceSet() {
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(1L);
        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId, pragmaticId, null, null, LocalDate.now()
        );

        AppliedKnowledgeId id1 = AppliedKnowledgeId.of(1L);
        appliedKnowledge.setId(id1);

        assertThrows(IllegalStateException.class, () ->
                appliedKnowledge.setId(AppliedKnowledgeId.of(2L))
        );
    }

    @Test
    void shouldReconstituteAppliedKnowledge() {
        AppliedKnowledgeId id = AppliedKnowledgeId.of(10L);
        ProjectId projectId = ProjectId.of(1L);
        PragmaticId pragmaticId = PragmaticId.of(2L);
        KnowledgeId knowledgeId = KnowledgeId.of(3L);
        LevelId levelId = LevelId.of(4L);
        LocalDate startDate = LocalDate.of(2024, 1, 1);

        AppliedKnowledge appliedKnowledge = AppliedKnowledge.reconstitute(
                id, projectId, pragmaticId, knowledgeId, levelId, startDate
        );

        assertEquals(id, appliedKnowledge.getId());
        assertEquals(projectId, appliedKnowledge.getProjectId());
        assertEquals(pragmaticId, appliedKnowledge.getPragmaticId());
        assertEquals(knowledgeId, appliedKnowledge.getKnowledgeId());
        assertEquals(levelId, appliedKnowledge.getLevelId());
        assertEquals(startDate, appliedKnowledge.getStartDate());
    }
}