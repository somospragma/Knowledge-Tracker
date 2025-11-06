package com.pragma.vc.tracker.peoplemanagement.infrastructure.web;

import com.pragma.vc.tracker.peoplemanagement.application.dto.PragmaticDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for PragmaticController
 * Tests the REST API endpoints with real Spring context and database
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class PragmaticControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllPragmatics_shouldReturnAllPragmatics() {
        // Act
        ResponseEntity<List<PragmaticDTO>> response = restTemplate.exchange(
                "/api/v1/pragmatics",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PragmaticDTO>>() {}
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();

        // Verify at least one pragmatic is returned
        List<PragmaticDTO> pragmatics = response.getBody();
        assertThat(pragmatics.size()).isGreaterThan(0);

        // Verify first pragmatic has required fields
        PragmaticDTO firstPragmatic = pragmatics.get(0);
        assertThat(firstPragmatic.getId()).isNotNull();
        assertThat(firstPragmatic.getEmail()).isNotBlank();
        assertThat(firstPragmatic.getFirstName()).isNotBlank();
        assertThat(firstPragmatic.getLastName()).isNotBlank();
        assertThat(firstPragmatic.getStatus()).isNotNull();
    }

    @Test
    void testGetAllPragmatics_withStatusFilter_shouldReturnFilteredPragmatics() {
        // Act
        ResponseEntity<List<PragmaticDTO>> response = restTemplate.exchange(
                "/api/v1/pragmatics?status=Active",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PragmaticDTO>>() {}
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Verify all returned pragmatics have Active status
        List<PragmaticDTO> pragmatics = response.getBody();
        pragmatics.forEach(pragmatic ->
            assertThat(pragmatic.getStatus()).isEqualTo("Active")
        );
    }

    @Test
    void testGetPragmaticById_shouldReturnPragmatic() {
        // Arrange - Get all pragmatics to find a valid ID
        ResponseEntity<List<PragmaticDTO>> allPragmaticsResponse = restTemplate.exchange(
                "/api/v1/pragmatics",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PragmaticDTO>>() {}
        );

        assertThat(allPragmaticsResponse.getBody()).isNotEmpty();
        Long pragmaticId = allPragmaticsResponse.getBody().get(0).getId();

        // Act
        ResponseEntity<PragmaticDTO> response = restTemplate.getForEntity(
                "/api/v1/pragmatics/" + pragmaticId,
                PragmaticDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(pragmaticId);
    }

    @Test
    void testGetPragmaticById_withInvalidId_shouldReturn404() {
        // Act
        ResponseEntity<PragmaticDTO> response = restTemplate.getForEntity(
                "/api/v1/pragmatics/999999",
                PragmaticDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
