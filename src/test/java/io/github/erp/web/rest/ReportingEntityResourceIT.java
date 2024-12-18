package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.ReportingEntity;
import io.github.erp.repository.ReportingEntityRepository;
import io.github.erp.repository.search.ReportingEntitySearchRepository;
import io.github.erp.service.criteria.ReportingEntityCriteria;
import io.github.erp.service.dto.ReportingEntityDTO;
import io.github.erp.service.mapper.ReportingEntityMapper;
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
 * Integration tests for the {@link ReportingEntityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportingEntityResourceIT {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reporting-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/reporting-entities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportingEntityRepository reportingEntityRepository;

    @Autowired
    private ReportingEntityMapper reportingEntityMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReportingEntitySearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportingEntitySearchRepository mockReportingEntitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingEntityMockMvc;

    private ReportingEntity reportingEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingEntity createEntity(EntityManager em) {
        ReportingEntity reportingEntity = new ReportingEntity().entityName(DEFAULT_ENTITY_NAME);
        return reportingEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingEntity createUpdatedEntity(EntityManager em) {
        ReportingEntity reportingEntity = new ReportingEntity().entityName(UPDATED_ENTITY_NAME);
        return reportingEntity;
    }

    @BeforeEach
    public void initTest() {
        reportingEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createReportingEntity() throws Exception {
        int databaseSizeBeforeCreate = reportingEntityRepository.findAll().size();
        // Create the ReportingEntity
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);
        restReportingEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeCreate + 1);
        ReportingEntity testReportingEntity = reportingEntityList.get(reportingEntityList.size() - 1);
        assertThat(testReportingEntity.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(1)).save(testReportingEntity);
    }

    @Test
    @Transactional
    void createReportingEntityWithExistingId() throws Exception {
        // Create the ReportingEntity with an existing ID
        reportingEntity.setId(1L);
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        int databaseSizeBeforeCreate = reportingEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(0)).save(reportingEntity);
    }

    @Test
    @Transactional
    void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportingEntityRepository.findAll().size();
        // set the field null
        reportingEntity.setEntityName(null);

        // Create the ReportingEntity, which fails.
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        restReportingEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportingEntities() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get all the reportingEntityList
        restReportingEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)));
    }

    @Test
    @Transactional
    void getReportingEntity() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get the reportingEntity
        restReportingEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingEntity.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME));
    }

    @Test
    @Transactional
    void getReportingEntitiesByIdFiltering() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        Long id = reportingEntity.getId();

        defaultReportingEntityShouldBeFound("id.equals=" + id);
        defaultReportingEntityShouldNotBeFound("id.notEquals=" + id);

        defaultReportingEntityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportingEntityShouldNotBeFound("id.greaterThan=" + id);

        defaultReportingEntityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportingEntityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingEntitiesByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get all the reportingEntityList where entityName equals to DEFAULT_ENTITY_NAME
        defaultReportingEntityShouldBeFound("entityName.equals=" + DEFAULT_ENTITY_NAME);

        // Get all the reportingEntityList where entityName equals to UPDATED_ENTITY_NAME
        defaultReportingEntityShouldNotBeFound("entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllReportingEntitiesByEntityNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get all the reportingEntityList where entityName not equals to DEFAULT_ENTITY_NAME
        defaultReportingEntityShouldNotBeFound("entityName.notEquals=" + DEFAULT_ENTITY_NAME);

        // Get all the reportingEntityList where entityName not equals to UPDATED_ENTITY_NAME
        defaultReportingEntityShouldBeFound("entityName.notEquals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllReportingEntitiesByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get all the reportingEntityList where entityName in DEFAULT_ENTITY_NAME or UPDATED_ENTITY_NAME
        defaultReportingEntityShouldBeFound("entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME);

        // Get all the reportingEntityList where entityName equals to UPDATED_ENTITY_NAME
        defaultReportingEntityShouldNotBeFound("entityName.in=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllReportingEntitiesByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get all the reportingEntityList where entityName is not null
        defaultReportingEntityShouldBeFound("entityName.specified=true");

        // Get all the reportingEntityList where entityName is null
        defaultReportingEntityShouldNotBeFound("entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingEntitiesByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get all the reportingEntityList where entityName contains DEFAULT_ENTITY_NAME
        defaultReportingEntityShouldBeFound("entityName.contains=" + DEFAULT_ENTITY_NAME);

        // Get all the reportingEntityList where entityName contains UPDATED_ENTITY_NAME
        defaultReportingEntityShouldNotBeFound("entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllReportingEntitiesByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        // Get all the reportingEntityList where entityName does not contain DEFAULT_ENTITY_NAME
        defaultReportingEntityShouldNotBeFound("entityName.doesNotContain=" + DEFAULT_ENTITY_NAME);

        // Get all the reportingEntityList where entityName does not contain UPDATED_ENTITY_NAME
        defaultReportingEntityShouldBeFound("entityName.doesNotContain=" + UPDATED_ENTITY_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingEntityShouldBeFound(String filter) throws Exception {
        restReportingEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)));

        // Check, that the count call also returns 1
        restReportingEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingEntityShouldNotBeFound(String filter) throws Exception {
        restReportingEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportingEntity() throws Exception {
        // Get the reportingEntity
        restReportingEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportingEntity() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();

        // Update the reportingEntity
        ReportingEntity updatedReportingEntity = reportingEntityRepository.findById(reportingEntity.getId()).get();
        // Disconnect from session so that the updates on updatedReportingEntity are not directly saved in db
        em.detach(updatedReportingEntity);
        updatedReportingEntity.entityName(UPDATED_ENTITY_NAME);
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(updatedReportingEntity);

        restReportingEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);
        ReportingEntity testReportingEntity = reportingEntityList.get(reportingEntityList.size() - 1);
        assertThat(testReportingEntity.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository).save(testReportingEntity);
    }

    @Test
    @Transactional
    void putNonExistingReportingEntity() throws Exception {
        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();
        reportingEntity.setId(count.incrementAndGet());

        // Create the ReportingEntity
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(0)).save(reportingEntity);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingEntity() throws Exception {
        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();
        reportingEntity.setId(count.incrementAndGet());

        // Create the ReportingEntity
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(0)).save(reportingEntity);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingEntity() throws Exception {
        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();
        reportingEntity.setId(count.incrementAndGet());

        // Create the ReportingEntity
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingEntityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(0)).save(reportingEntity);
    }

    @Test
    @Transactional
    void partialUpdateReportingEntityWithPatch() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();

        // Update the reportingEntity using partial update
        ReportingEntity partialUpdatedReportingEntity = new ReportingEntity();
        partialUpdatedReportingEntity.setId(reportingEntity.getId());

        restReportingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportingEntity))
            )
            .andExpect(status().isOk());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);
        ReportingEntity testReportingEntity = reportingEntityList.get(reportingEntityList.size() - 1);
        assertThat(testReportingEntity.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateReportingEntityWithPatch() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();

        // Update the reportingEntity using partial update
        ReportingEntity partialUpdatedReportingEntity = new ReportingEntity();
        partialUpdatedReportingEntity.setId(reportingEntity.getId());

        partialUpdatedReportingEntity.entityName(UPDATED_ENTITY_NAME);

        restReportingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportingEntity))
            )
            .andExpect(status().isOk());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);
        ReportingEntity testReportingEntity = reportingEntityList.get(reportingEntityList.size() - 1);
        assertThat(testReportingEntity.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingReportingEntity() throws Exception {
        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();
        reportingEntity.setId(count.incrementAndGet());

        // Create the ReportingEntity
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(0)).save(reportingEntity);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingEntity() throws Exception {
        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();
        reportingEntity.setId(count.incrementAndGet());

        // Create the ReportingEntity
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(0)).save(reportingEntity);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingEntity() throws Exception {
        int databaseSizeBeforeUpdate = reportingEntityRepository.findAll().size();
        reportingEntity.setId(count.incrementAndGet());

        // Create the ReportingEntity
        ReportingEntityDTO reportingEntityDTO = reportingEntityMapper.toDto(reportingEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportingEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingEntity in the database
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(0)).save(reportingEntity);
    }

    @Test
    @Transactional
    void deleteReportingEntity() throws Exception {
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);

        int databaseSizeBeforeDelete = reportingEntityRepository.findAll().size();

        // Delete the reportingEntity
        restReportingEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportingEntity> reportingEntityList = reportingEntityRepository.findAll();
        assertThat(reportingEntityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReportingEntity in Elasticsearch
        verify(mockReportingEntitySearchRepository, times(1)).deleteById(reportingEntity.getId());
    }

    @Test
    @Transactional
    void searchReportingEntity() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reportingEntityRepository.saveAndFlush(reportingEntity);
        when(mockReportingEntitySearchRepository.search("id:" + reportingEntity.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reportingEntity), PageRequest.of(0, 1), 1));

        // Search the reportingEntity
        restReportingEntityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportingEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)));
    }
}
