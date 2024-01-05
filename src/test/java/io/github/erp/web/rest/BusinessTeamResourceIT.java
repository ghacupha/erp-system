package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.BusinessTeam;
import io.github.erp.domain.User;
import io.github.erp.repository.BusinessTeamRepository;
import io.github.erp.repository.search.BusinessTeamSearchRepository;
import io.github.erp.service.criteria.BusinessTeamCriteria;
import io.github.erp.service.dto.BusinessTeamDTO;
import io.github.erp.service.mapper.BusinessTeamMapper;
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
 * Integration tests for the {@link BusinessTeamResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BusinessTeamResourceIT {

    private static final String DEFAULT_BUSINESS_TEAM = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_TEAM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/business-teams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/business-teams";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessTeamRepository businessTeamRepository;

    @Autowired
    private BusinessTeamMapper businessTeamMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.BusinessTeamSearchRepositoryMockConfiguration
     */
    @Autowired
    private BusinessTeamSearchRepository mockBusinessTeamSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessTeamMockMvc;

    private BusinessTeam businessTeam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessTeam createEntity(EntityManager em) {
        BusinessTeam businessTeam = new BusinessTeam().businessTeam(DEFAULT_BUSINESS_TEAM);
        return businessTeam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessTeam createUpdatedEntity(EntityManager em) {
        BusinessTeam businessTeam = new BusinessTeam().businessTeam(UPDATED_BUSINESS_TEAM);
        return businessTeam;
    }

    @BeforeEach
    public void initTest() {
        businessTeam = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessTeam() throws Exception {
        int databaseSizeBeforeCreate = businessTeamRepository.findAll().size();
        // Create the BusinessTeam
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);
        restBusinessTeamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessTeam testBusinessTeam = businessTeamList.get(businessTeamList.size() - 1);
        assertThat(testBusinessTeam.getBusinessTeam()).isEqualTo(DEFAULT_BUSINESS_TEAM);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(1)).save(testBusinessTeam);
    }

    @Test
    @Transactional
    void createBusinessTeamWithExistingId() throws Exception {
        // Create the BusinessTeam with an existing ID
        businessTeam.setId(1L);
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        int databaseSizeBeforeCreate = businessTeamRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessTeamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeCreate);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(0)).save(businessTeam);
    }

    @Test
    @Transactional
    void checkBusinessTeamIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessTeamRepository.findAll().size();
        // set the field null
        businessTeam.setBusinessTeam(null);

        // Create the BusinessTeam, which fails.
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        restBusinessTeamMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessTeams() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get all the businessTeamList
        restBusinessTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessTeam").value(hasItem(DEFAULT_BUSINESS_TEAM)));
    }

    @Test
    @Transactional
    void getBusinessTeam() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get the businessTeam
        restBusinessTeamMockMvc
            .perform(get(ENTITY_API_URL_ID, businessTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessTeam.getId().intValue()))
            .andExpect(jsonPath("$.businessTeam").value(DEFAULT_BUSINESS_TEAM));
    }

    @Test
    @Transactional
    void getBusinessTeamsByIdFiltering() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        Long id = businessTeam.getId();

        defaultBusinessTeamShouldBeFound("id.equals=" + id);
        defaultBusinessTeamShouldNotBeFound("id.notEquals=" + id);

        defaultBusinessTeamShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusinessTeamShouldNotBeFound("id.greaterThan=" + id);

        defaultBusinessTeamShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusinessTeamShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusinessTeamsByBusinessTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get all the businessTeamList where businessTeam equals to DEFAULT_BUSINESS_TEAM
        defaultBusinessTeamShouldBeFound("businessTeam.equals=" + DEFAULT_BUSINESS_TEAM);

        // Get all the businessTeamList where businessTeam equals to UPDATED_BUSINESS_TEAM
        defaultBusinessTeamShouldNotBeFound("businessTeam.equals=" + UPDATED_BUSINESS_TEAM);
    }

    @Test
    @Transactional
    void getAllBusinessTeamsByBusinessTeamIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get all the businessTeamList where businessTeam not equals to DEFAULT_BUSINESS_TEAM
        defaultBusinessTeamShouldNotBeFound("businessTeam.notEquals=" + DEFAULT_BUSINESS_TEAM);

        // Get all the businessTeamList where businessTeam not equals to UPDATED_BUSINESS_TEAM
        defaultBusinessTeamShouldBeFound("businessTeam.notEquals=" + UPDATED_BUSINESS_TEAM);
    }

    @Test
    @Transactional
    void getAllBusinessTeamsByBusinessTeamIsInShouldWork() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get all the businessTeamList where businessTeam in DEFAULT_BUSINESS_TEAM or UPDATED_BUSINESS_TEAM
        defaultBusinessTeamShouldBeFound("businessTeam.in=" + DEFAULT_BUSINESS_TEAM + "," + UPDATED_BUSINESS_TEAM);

        // Get all the businessTeamList where businessTeam equals to UPDATED_BUSINESS_TEAM
        defaultBusinessTeamShouldNotBeFound("businessTeam.in=" + UPDATED_BUSINESS_TEAM);
    }

    @Test
    @Transactional
    void getAllBusinessTeamsByBusinessTeamIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get all the businessTeamList where businessTeam is not null
        defaultBusinessTeamShouldBeFound("businessTeam.specified=true");

        // Get all the businessTeamList where businessTeam is null
        defaultBusinessTeamShouldNotBeFound("businessTeam.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTeamsByBusinessTeamContainsSomething() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get all the businessTeamList where businessTeam contains DEFAULT_BUSINESS_TEAM
        defaultBusinessTeamShouldBeFound("businessTeam.contains=" + DEFAULT_BUSINESS_TEAM);

        // Get all the businessTeamList where businessTeam contains UPDATED_BUSINESS_TEAM
        defaultBusinessTeamShouldNotBeFound("businessTeam.contains=" + UPDATED_BUSINESS_TEAM);
    }

    @Test
    @Transactional
    void getAllBusinessTeamsByBusinessTeamNotContainsSomething() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        // Get all the businessTeamList where businessTeam does not contain DEFAULT_BUSINESS_TEAM
        defaultBusinessTeamShouldNotBeFound("businessTeam.doesNotContain=" + DEFAULT_BUSINESS_TEAM);

        // Get all the businessTeamList where businessTeam does not contain UPDATED_BUSINESS_TEAM
        defaultBusinessTeamShouldBeFound("businessTeam.doesNotContain=" + UPDATED_BUSINESS_TEAM);
    }

    @Test
    @Transactional
    void getAllBusinessTeamsByTeamMembersIsEqualToSomething() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);
        User teamMembers;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            teamMembers = UserResourceIT.createEntity(em);
            em.persist(teamMembers);
            em.flush();
        } else {
            teamMembers = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(teamMembers);
        em.flush();
        businessTeam.setTeamMembers(teamMembers);
        businessTeamRepository.saveAndFlush(businessTeam);
        Long teamMembersId = teamMembers.getId();

        // Get all the businessTeamList where teamMembers equals to teamMembersId
        defaultBusinessTeamShouldBeFound("teamMembersId.equals=" + teamMembersId);

        // Get all the businessTeamList where teamMembers equals to (teamMembersId + 1)
        defaultBusinessTeamShouldNotBeFound("teamMembersId.equals=" + (teamMembersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusinessTeamShouldBeFound(String filter) throws Exception {
        restBusinessTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessTeam").value(hasItem(DEFAULT_BUSINESS_TEAM)));

        // Check, that the count call also returns 1
        restBusinessTeamMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusinessTeamShouldNotBeFound(String filter) throws Exception {
        restBusinessTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusinessTeamMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusinessTeam() throws Exception {
        // Get the businessTeam
        restBusinessTeamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusinessTeam() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();

        // Update the businessTeam
        BusinessTeam updatedBusinessTeam = businessTeamRepository.findById(businessTeam.getId()).get();
        // Disconnect from session so that the updates on updatedBusinessTeam are not directly saved in db
        em.detach(updatedBusinessTeam);
        updatedBusinessTeam.businessTeam(UPDATED_BUSINESS_TEAM);
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(updatedBusinessTeam);

        restBusinessTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTeamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);
        BusinessTeam testBusinessTeam = businessTeamList.get(businessTeamList.size() - 1);
        assertThat(testBusinessTeam.getBusinessTeam()).isEqualTo(UPDATED_BUSINESS_TEAM);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository).save(testBusinessTeam);
    }

    @Test
    @Transactional
    void putNonExistingBusinessTeam() throws Exception {
        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();
        businessTeam.setId(count.incrementAndGet());

        // Create the BusinessTeam
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTeamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(0)).save(businessTeam);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessTeam() throws Exception {
        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();
        businessTeam.setId(count.incrementAndGet());

        // Create the BusinessTeam
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(0)).save(businessTeam);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessTeam() throws Exception {
        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();
        businessTeam.setId(count.incrementAndGet());

        // Create the BusinessTeam
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTeamMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(0)).save(businessTeam);
    }

    @Test
    @Transactional
    void partialUpdateBusinessTeamWithPatch() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();

        // Update the businessTeam using partial update
        BusinessTeam partialUpdatedBusinessTeam = new BusinessTeam();
        partialUpdatedBusinessTeam.setId(businessTeam.getId());

        partialUpdatedBusinessTeam.businessTeam(UPDATED_BUSINESS_TEAM);

        restBusinessTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessTeam))
            )
            .andExpect(status().isOk());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);
        BusinessTeam testBusinessTeam = businessTeamList.get(businessTeamList.size() - 1);
        assertThat(testBusinessTeam.getBusinessTeam()).isEqualTo(UPDATED_BUSINESS_TEAM);
    }

    @Test
    @Transactional
    void fullUpdateBusinessTeamWithPatch() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();

        // Update the businessTeam using partial update
        BusinessTeam partialUpdatedBusinessTeam = new BusinessTeam();
        partialUpdatedBusinessTeam.setId(businessTeam.getId());

        partialUpdatedBusinessTeam.businessTeam(UPDATED_BUSINESS_TEAM);

        restBusinessTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessTeam))
            )
            .andExpect(status().isOk());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);
        BusinessTeam testBusinessTeam = businessTeamList.get(businessTeamList.size() - 1);
        assertThat(testBusinessTeam.getBusinessTeam()).isEqualTo(UPDATED_BUSINESS_TEAM);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessTeam() throws Exception {
        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();
        businessTeam.setId(count.incrementAndGet());

        // Create the BusinessTeam
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessTeamDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(0)).save(businessTeam);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessTeam() throws Exception {
        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();
        businessTeam.setId(count.incrementAndGet());

        // Create the BusinessTeam
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(0)).save(businessTeam);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessTeam() throws Exception {
        int databaseSizeBeforeUpdate = businessTeamRepository.findAll().size();
        businessTeam.setId(count.incrementAndGet());

        // Create the BusinessTeam
        BusinessTeamDTO businessTeamDTO = businessTeamMapper.toDto(businessTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTeamMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessTeamDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessTeam in the database
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(0)).save(businessTeam);
    }

    @Test
    @Transactional
    void deleteBusinessTeam() throws Exception {
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);

        int databaseSizeBeforeDelete = businessTeamRepository.findAll().size();

        // Delete the businessTeam
        restBusinessTeamMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessTeam.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessTeam> businessTeamList = businessTeamRepository.findAll();
        assertThat(businessTeamList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BusinessTeam in Elasticsearch
        verify(mockBusinessTeamSearchRepository, times(1)).deleteById(businessTeam.getId());
    }

    @Test
    @Transactional
    void searchBusinessTeam() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        businessTeamRepository.saveAndFlush(businessTeam);
        when(mockBusinessTeamSearchRepository.search("id:" + businessTeam.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(businessTeam), PageRequest.of(0, 1), 1));

        // Search the businessTeam
        restBusinessTeamMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + businessTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessTeam").value(hasItem(DEFAULT_BUSINESS_TEAM)));
    }
}
