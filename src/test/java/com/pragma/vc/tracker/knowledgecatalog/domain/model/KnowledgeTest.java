package com.pragma.vc.tracker.knowledgecatalog.domain.model;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class KnowledgeTest {

    @Test
    void shouldCreateKnowledgeWithValidData() {
        CategoryId categoryId = CategoryId.of(1L);
        Knowledge knowledge = Knowledge.create(categoryId, "Spring Boot", "Java framework", 
            ApprovalStatus.PENDING, new HashMap<>());
        
        assertNotNull(knowledge);
        assertEquals("Spring Boot", knowledge.getName());
        assertEquals(ApprovalStatus.PENDING, knowledge.getApprovalStatus());
        assertTrue(knowledge.isPending());
    }

    @Test
    void shouldThrowExceptionWhenCategoryIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
            Knowledge.create(null, "Test", "Desc", ApprovalStatus.PENDING, null)
        );
    }

    @Test
    void shouldApproveKnowledge() {
        CategoryId categoryId = CategoryId.of(1L);
        Knowledge knowledge = Knowledge.create(categoryId, "Test", "Desc", ApprovalStatus.PENDING, null);
        
        knowledge.approve();
        
        assertTrue(knowledge.isApproved());
        assertEquals(ApprovalStatus.APPROVED, knowledge.getApprovalStatus());
    }

    @Test
    void shouldNotModifyApprovedKnowledge() {
        CategoryId categoryId = CategoryId.of(1L);
        Knowledge knowledge = Knowledge.create(categoryId, "Test", "Desc", ApprovalStatus.APPROVED, null);
        
        assertThrows(IllegalStateException.class, () -> knowledge.updateName("New Name"));
    }

    @Test
    void shouldRejectAndResubmitKnowledge() {
        CategoryId categoryId = CategoryId.of(1L);
        Knowledge knowledge = Knowledge.create(categoryId, "Test", "Desc", ApprovalStatus.PENDING, null);
        
        knowledge.reject();
        assertEquals(ApprovalStatus.REJECTED, knowledge.getApprovalStatus());
        
        knowledge.resubmit();
        assertEquals(ApprovalStatus.PENDING, knowledge.getApprovalStatus());
    }

    @Test
    void shouldNotDeleteApprovedKnowledge() {
        CategoryId categoryId = CategoryId.of(1L);
        Knowledge knowledge = Knowledge.create(categoryId, "Test", "Desc", ApprovalStatus.APPROVED, null);
        
        assertFalse(knowledge.canBeDeleted());
    }
}
