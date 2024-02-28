package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.EmploymentTerms;
import io.github.erp.repository.EmploymentTermsRepository;
import io.github.erp.repository.search.EmploymentTermsSearchRepository;
import io.github.erp.service.criteria.EmploymentTermsCriteria;
import io.github.erp.service.dto.EmploymentTermsDTO;
import io.github.erp.service.mapper.EmploymentTermsMapper;
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
 * Integration tests for the {@link EmploymentTermsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmploymentTermsResourceIT {

    private static final String DEFAULT_EMPLOYMENT_TERMS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYMENT_TERMS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYMENT_TERMS_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYMENT_TERMS_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employment-terms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/employment-terms";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentTermsRepository employmentTermsRepository;

    @Autowired
    private EmploymentTermsMapper employmentTermsMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.EmploymentTermsSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmploymentTermsSearchRepository mockEmploymentTermsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentTermsMockMvc;

    private EmploymentTerms employmentTerms;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentTerms createEntity(EntityManager em) {
        EmploymentTerms employmentTerms = new EmploymentTerms()
            .employmentTermsCode(DEFAULT_EMPLOYMENT_TERMS_CODE)
            .employmentTermsStatus(DEFAULT_EMPLOYMENT_TERMS_STATUS);
        return employmentTerms;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentTerms createUpdatedEntity(EntityManager em) {
        EmploymentTerms employmentTerms = new EmploymentTerms()
            .employmentTermsCode(UPDATED_EMPLOYMENT_TERMS_CODE)
            .employmentTermsStatus(UPDATED_EMPLOYMENT_TERMS_STATUS);
        return employmentTerms;
    }

    @BeforeEach
    public void initTest() {
        employmentTerms = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploymentTerms() throws Exception {
        int databaseSizeBeforeCreate = employmentTermsRepository.findAll().size();
        // Create the EmploymentTerms
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);
        restEmploymentTermsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeCreate + 1);
        EmploymentTerms testEmploymentTerms = employmentTermsList.get(employmentTermsList.size() - 1);
        assertThat(testEmploymentTerms.getEmploymentTermsCode()).isEqualTo(DEFAULT_EMPLOYMENT_TERMS_CODE);
        assertThat(testEmploymentTerms.getEmploymentTermsStatus()).isEqualTo(DEFAULT_EMPLOYMENT_TERMS_STATUS);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(1)).save(testEmploymentTerms);
    }

    @Test
    @Transactional
    void createEmploymentTermsWithExistingId() throws Exception {
        // Create the EmploymentTerms with an existing ID
        employmentTerms.setId(1L);
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        int databaseSizeBeforeCreate = employmentTermsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentTermsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeCreate);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(0)).save(employmentTerms);
    }

    @Test
    @Transactional
    void checkEmploymentTermsCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employmentTermsRepository.findAll().size();
        // set the field null
        employmentTerms.setEmploymentTermsCode(null);

        // Create the EmploymentTerms, which fails.
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        restEmploymentTermsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmploymentTermsStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = employmentTermsRepository.findAll().size();
        // set the field null
        employmentTerms.setEmploymentTermsStatus(null);

        // Create the EmploymentTerms, which fails.
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        restEmploymentTermsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmploymentTerms() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList
        restEmploymentTermsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentTerms.getId().intValue())))
            .andExpect(jsonPath("$.[*].employmentTermsCode").value(hasItem(DEFAULT_EMPLOYMENT_TERMS_CODE)))
            .andExpect(jsonPath("$.[*].employmentTermsStatus").value(hasItem(DEFAULT_EMPLOYMENT_TERMS_STATUS)));
    }

    @Test
    @Transactional
    void getEmploymentTerms() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get the employmentTerms
        restEmploymentTermsMockMvc
            .perform(get(ENTITY_API_URL_ID, employmentTerms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employmentTerms.getId().intValue()))
            .andExpect(jsonPath("$.employmentTermsCode").value(DEFAULT_EMPLOYMENT_TERMS_CODE))
            .andExpect(jsonPath("$.employmentTermsStatus").value(DEFAULT_EMPLOYMENT_TERMS_STATUS));
    }

    @Test
    @Transactional
    void getEmploymentTermsByIdFiltering() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        Long id = employmentTerms.getId();

        defaultEmploymentTermsShouldBeFound("id.equals=" + id);
        defaultEmploymentTermsShouldNotBeFound("id.notEquals=" + id);

        defaultEmploymentTermsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmploymentTermsShouldNotBeFound("id.greaterThan=" + id);

        defaultEmploymentTermsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmploymentTermsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsCode equals to DEFAULT_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldBeFound("employmentTermsCode.equals=" + DEFAULT_EMPLOYMENT_TERMS_CODE);

        // Get all the employmentTermsList where employmentTermsCode equals to UPDATED_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldNotBeFound("employmentTermsCode.equals=" + UPDATED_EMPLOYMENT_TERMS_CODE);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsCode not equals to DEFAULT_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldNotBeFound("employmentTermsCode.notEquals=" + DEFAULT_EMPLOYMENT_TERMS_CODE);

        // Get all the employmentTermsList where employmentTermsCode not equals to UPDATED_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldBeFound("employmentTermsCode.notEquals=" + UPDATED_EMPLOYMENT_TERMS_CODE);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsCode in DEFAULT_EMPLOYMENT_TERMS_CODE or UPDATED_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldBeFound(
            "employmentTermsCode.in=" + DEFAULT_EMPLOYMENT_TERMS_CODE + "," + UPDATED_EMPLOYMENT_TERMS_CODE
        );

        // Get all the employmentTermsList where employmentTermsCode equals to UPDATED_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldNotBeFound("employmentTermsCode.in=" + UPDATED_EMPLOYMENT_TERMS_CODE);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsCode is not null
        defaultEmploymentTermsShouldBeFound("employmentTermsCode.specified=true");

        // Get all the employmentTermsList where employmentTermsCode is null
        defaultEmploymentTermsShouldNotBeFound("employmentTermsCode.specified=false");
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsCodeContainsSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsCode contains DEFAULT_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldBeFound("employmentTermsCode.contains=" + DEFAULT_EMPLOYMENT_TERMS_CODE);

        // Get all the employmentTermsList where employmentTermsCode contains UPDATED_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldNotBeFound("employmentTermsCode.contains=" + UPDATED_EMPLOYMENT_TERMS_CODE);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsCode does not contain DEFAULT_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldNotBeFound("employmentTermsCode.doesNotContain=" + DEFAULT_EMPLOYMENT_TERMS_CODE);

        // Get all the employmentTermsList where employmentTermsCode does not contain UPDATED_EMPLOYMENT_TERMS_CODE
        defaultEmploymentTermsShouldBeFound("employmentTermsCode.doesNotContain=" + UPDATED_EMPLOYMENT_TERMS_CODE);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsStatus equals to DEFAULT_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldBeFound("employmentTermsStatus.equals=" + DEFAULT_EMPLOYMENT_TERMS_STATUS);

        // Get all the employmentTermsList where employmentTermsStatus equals to UPDATED_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldNotBeFound("employmentTermsStatus.equals=" + UPDATED_EMPLOYMENT_TERMS_STATUS);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsStatus not equals to DEFAULT_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldNotBeFound("employmentTermsStatus.notEquals=" + DEFAULT_EMPLOYMENT_TERMS_STATUS);

        // Get all the employmentTermsList where employmentTermsStatus not equals to UPDATED_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldBeFound("employmentTermsStatus.notEquals=" + UPDATED_EMPLOYMENT_TERMS_STATUS);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsStatusIsInShouldWork() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsStatus in DEFAULT_EMPLOYMENT_TERMS_STATUS or UPDATED_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldBeFound(
            "employmentTermsStatus.in=" + DEFAULT_EMPLOYMENT_TERMS_STATUS + "," + UPDATED_EMPLOYMENT_TERMS_STATUS
        );

        // Get all the employmentTermsList where employmentTermsStatus equals to UPDATED_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldNotBeFound("employmentTermsStatus.in=" + UPDATED_EMPLOYMENT_TERMS_STATUS);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsStatus is not null
        defaultEmploymentTermsShouldBeFound("employmentTermsStatus.specified=true");

        // Get all the employmentTermsList where employmentTermsStatus is null
        defaultEmploymentTermsShouldNotBeFound("employmentTermsStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsStatusContainsSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsStatus contains DEFAULT_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldBeFound("employmentTermsStatus.contains=" + DEFAULT_EMPLOYMENT_TERMS_STATUS);

        // Get all the employmentTermsList where employmentTermsStatus contains UPDATED_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldNotBeFound("employmentTermsStatus.contains=" + UPDATED_EMPLOYMENT_TERMS_STATUS);
    }

    @Test
    @Transactional
    void getAllEmploymentTermsByEmploymentTermsStatusNotContainsSomething() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        // Get all the employmentTermsList where employmentTermsStatus does not contain DEFAULT_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldNotBeFound("employmentTermsStatus.doesNotContain=" + DEFAULT_EMPLOYMENT_TERMS_STATUS);

        // Get all the employmentTermsList where employmentTermsStatus does not contain UPDATED_EMPLOYMENT_TERMS_STATUS
        defaultEmploymentTermsShouldBeFound("employmentTermsStatus.doesNotContain=" + UPDATED_EMPLOYMENT_TERMS_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmploymentTermsShouldBeFound(String filter) throws Exception {
        restEmploymentTermsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentTerms.getId().intValue())))
            .andExpect(jsonPath("$.[*].employmentTermsCode").value(hasItem(DEFAULT_EMPLOYMENT_TERMS_CODE)))
            .andExpect(jsonPath("$.[*].employmentTermsStatus").value(hasItem(DEFAULT_EMPLOYMENT_TERMS_STATUS)));

        // Check, that the count call also returns 1
        restEmploymentTermsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmploymentTermsShouldNotBeFound(String filter) throws Exception {
        restEmploymentTermsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmploymentTermsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmploymentTerms() throws Exception {
        // Get the employmentTerms
        restEmploymentTermsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmploymentTerms() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();

        // Update the employmentTerms
        EmploymentTerms updatedEmploymentTerms = employmentTermsRepository.findById(employmentTerms.getId()).get();
        // Disconnect from session so that the updates on updatedEmploymentTerms are not directly saved in db
        em.detach(updatedEmploymentTerms);
        updatedEmploymentTerms.employmentTermsCode(UPDATED_EMPLOYMENT_TERMS_CODE).employmentTermsStatus(UPDATED_EMPLOYMENT_TERMS_STATUS);
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(updatedEmploymentTerms);

        restEmploymentTermsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentTermsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);
        EmploymentTerms testEmploymentTerms = employmentTermsList.get(employmentTermsList.size() - 1);
        assertThat(testEmploymentTerms.getEmploymentTermsCode()).isEqualTo(UPDATED_EMPLOYMENT_TERMS_CODE);
        assertThat(testEmploymentTerms.getEmploymentTermsStatus()).isEqualTo(UPDATED_EMPLOYMENT_TERMS_STATUS);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository).save(testEmploymentTerms);
    }

    @Test
    @Transactional
    void putNonExistingEmploymentTerms() throws Exception {
        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();
        employmentTerms.setId(count.incrementAndGet());

        // Create the EmploymentTerms
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentTermsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentTermsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(0)).save(employmentTerms);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploymentTerms() throws Exception {
        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();
        employmentTerms.setId(count.incrementAndGet());

        // Create the EmploymentTerms
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTermsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(0)).save(employmentTerms);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploymentTerms() throws Exception {
        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();
        employmentTerms.setId(count.incrementAndGet());

        // Create the EmploymentTerms
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTermsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(0)).save(employmentTerms);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentTermsWithPatch() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();

        // Update the employmentTerms using partial update
        EmploymentTerms partialUpdatedEmploymentTerms = new EmploymentTerms();
        partialUpdatedEmploymentTerms.setId(employmentTerms.getId());

        partialUpdatedEmploymentTerms.employmentTermsCode(UPDATED_EMPLOYMENT_TERMS_CODE);

        restEmploymentTermsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentTerms.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentTerms))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);
        EmploymentTerms testEmploymentTerms = employmentTermsList.get(employmentTermsList.size() - 1);
        assertThat(testEmploymentTerms.getEmploymentTermsCode()).isEqualTo(UPDATED_EMPLOYMENT_TERMS_CODE);
        assertThat(testEmploymentTerms.getEmploymentTermsStatus()).isEqualTo(DEFAULT_EMPLOYMENT_TERMS_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentTermsWithPatch() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();

        // Update the employmentTerms using partial update
        EmploymentTerms partialUpdatedEmploymentTerms = new EmploymentTerms();
        partialUpdatedEmploymentTerms.setId(employmentTerms.getId());

        partialUpdatedEmploymentTerms
            .employmentTermsCode(UPDATED_EMPLOYMENT_TERMS_CODE)
            .employmentTermsStatus(UPDATED_EMPLOYMENT_TERMS_STATUS);

        restEmploymentTermsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentTerms.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentTerms))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);
        EmploymentTerms testEmploymentTerms = employmentTermsList.get(employmentTermsList.size() - 1);
        assertThat(testEmploymentTerms.getEmploymentTermsCode()).isEqualTo(UPDATED_EMPLOYMENT_TERMS_CODE);
        assertThat(testEmploymentTerms.getEmploymentTermsStatus()).isEqualTo(UPDATED_EMPLOYMENT_TERMS_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingEmploymentTerms() throws Exception {
        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();
        employmentTerms.setId(count.incrementAndGet());

        // Create the EmploymentTerms
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentTermsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentTermsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(0)).save(employmentTerms);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploymentTerms() throws Exception {
        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();
        employmentTerms.setId(count.incrementAndGet());

        // Create the EmploymentTerms
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTermsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(0)).save(employmentTerms);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploymentTerms() throws Exception {
        int databaseSizeBeforeUpdate = employmentTermsRepository.findAll().size();
        employmentTerms.setId(count.incrementAndGet());

        // Create the EmploymentTerms
        EmploymentTermsDTO employmentTermsDTO = employmentTermsMapper.toDto(employmentTerms);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTermsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentTermsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentTerms in the database
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(0)).save(employmentTerms);
    }

    @Test
    @Transactional
    void deleteEmploymentTerms() throws Exception {
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);

        int databaseSizeBeforeDelete = employmentTermsRepository.findAll().size();

        // Delete the employmentTerms
        restEmploymentTermsMockMvc
            .perform(delete(ENTITY_API_URL_ID, employmentTerms.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmploymentTerms> employmentTermsList = employmentTermsRepository.findAll();
        assertThat(employmentTermsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the EmploymentTerms in Elasticsearch
        verify(mockEmploymentTermsSearchRepository, times(1)).deleteById(employmentTerms.getId());
    }

    @Test
    @Transactional
    void searchEmploymentTerms() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        employmentTermsRepository.saveAndFlush(employmentTerms);
        when(mockEmploymentTermsSearchRepository.search("id:" + employmentTerms.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(employmentTerms), PageRequest.of(0, 1), 1));

        // Search the employmentTerms
        restEmploymentTermsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + employmentTerms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentTerms.getId().intValue())))
            .andExpect(jsonPath("$.[*].employmentTermsCode").value(hasItem(DEFAULT_EMPLOYMENT_TERMS_CODE)))
            .andExpect(jsonPath("$.[*].employmentTermsStatus").value(hasItem(DEFAULT_EMPLOYMENT_TERMS_STATUS)));
    }
}
