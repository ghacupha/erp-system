package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.SecurityClearance;
import io.github.erp.repository.SecurityClearanceRepository;
import io.github.erp.repository.search.SecurityClearanceSearchRepository;
import io.github.erp.service.SecurityClearanceService;
import io.github.erp.service.dto.SecurityClearanceDTO;
import io.github.erp.service.mapper.SecurityClearanceMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SecurityClearanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SecurityClearanceResourceIT {

    private static final String DEFAULT_CLEARANCE_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_CLEARANCE_LEVEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/security-clearances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/security-clearances";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecurityClearanceRepository securityClearanceRepository;

    @Mock
    private SecurityClearanceRepository securityClearanceRepositoryMock;

    @Autowired
    private SecurityClearanceMapper securityClearanceMapper;

    @Mock
    private SecurityClearanceService securityClearanceServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SecurityClearanceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SecurityClearanceSearchRepository mockSecurityClearanceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityClearanceMockMvc;

    private SecurityClearance securityClearance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClearance createEntity(EntityManager em) {
        SecurityClearance securityClearance = new SecurityClearance().clearanceLevel(DEFAULT_CLEARANCE_LEVEL);
        return securityClearance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClearance createUpdatedEntity(EntityManager em) {
        SecurityClearance securityClearance = new SecurityClearance().clearanceLevel(UPDATED_CLEARANCE_LEVEL);
        return securityClearance;
    }

    @BeforeEach
    public void initTest() {
        securityClearance = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityClearance() throws Exception {
        int databaseSizeBeforeCreate = securityClearanceRepository.findAll().size();
        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);
        restSecurityClearanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(DEFAULT_CLEARANCE_LEVEL);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(1)).save(testSecurityClearance);
    }

    @Test
    @Transactional
    void createSecurityClearanceWithExistingId() throws Exception {
        // Create the SecurityClearance with an existing ID
        securityClearance.setId(1L);
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        int databaseSizeBeforeCreate = securityClearanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityClearanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeCreate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void checkClearanceLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityClearanceRepository.findAll().size();
        // set the field null
        securityClearance.setClearanceLevel(null);

        // Create the SecurityClearance, which fails.
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        restSecurityClearanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSecurityClearances() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityClearancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(securityClearanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(securityClearanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityClearancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(securityClearanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(securityClearanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSecurityClearance() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get the securityClearance
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL_ID, securityClearance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityClearance.getId().intValue()))
            .andExpect(jsonPath("$.clearanceLevel").value(DEFAULT_CLEARANCE_LEVEL));
    }

    @Test
    @Transactional
    void getNonExistingSecurityClearance() throws Exception {
        // Get the securityClearance
        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecurityClearance() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();

        // Update the securityClearance
        SecurityClearance updatedSecurityClearance = securityClearanceRepository.findById(securityClearance.getId()).get();
        // Disconnect from session so that the updates on updatedSecurityClearance are not directly saved in db
        em.detach(updatedSecurityClearance);
        updatedSecurityClearance.clearanceLevel(UPDATED_CLEARANCE_LEVEL);
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(updatedSecurityClearance);

        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(UPDATED_CLEARANCE_LEVEL);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository).save(testSecurityClearance);
    }

    @Test
    @Transactional
    void putNonExistingSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void partialUpdateSecurityClearanceWithPatch() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();

        // Update the securityClearance using partial update
        SecurityClearance partialUpdatedSecurityClearance = new SecurityClearance();
        partialUpdatedSecurityClearance.setId(securityClearance.getId());

        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityClearance))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(DEFAULT_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateSecurityClearanceWithPatch() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();

        // Update the securityClearance using partial update
        SecurityClearance partialUpdatedSecurityClearance = new SecurityClearance();
        partialUpdatedSecurityClearance.setId(securityClearance.getId());

        partialUpdatedSecurityClearance.clearanceLevel(UPDATED_CLEARANCE_LEVEL);

        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityClearance))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(UPDATED_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void deleteSecurityClearance() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeDelete = securityClearanceRepository.findAll().size();

        // Delete the securityClearance
        restSecurityClearanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityClearance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(1)).deleteById(securityClearance.getId());
    }

    @Test
    @Transactional
    void searchSecurityClearance() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);
        when(mockSecurityClearanceSearchRepository.search("id:" + securityClearance.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(securityClearance), PageRequest.of(0, 1), 1));

        // Search the securityClearance
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + securityClearance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)));
    }
}
