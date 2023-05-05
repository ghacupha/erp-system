package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 14 (Caleb Series) Server ver 1.1.4-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.Algorithm;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.AlgorithmRepository;
import io.github.erp.repository.search.AlgorithmSearchRepository;
import io.github.erp.service.AlgorithmService;
import io.github.erp.service.criteria.AlgorithmCriteria;
import io.github.erp.service.dto.AlgorithmDTO;
import io.github.erp.service.mapper.AlgorithmMapper;
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
 * Integration tests for the {@link AlgorithmResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlgorithmResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/algorithms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/algorithms";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Mock
    private AlgorithmRepository algorithmRepositoryMock;

    @Autowired
    private AlgorithmMapper algorithmMapper;

    @Mock
    private AlgorithmService algorithmServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AlgorithmSearchRepositoryMockConfiguration
     */
    @Autowired
    private AlgorithmSearchRepository mockAlgorithmSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlgorithmMockMvc;

    private Algorithm algorithm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Algorithm createEntity(EntityManager em) {
        Algorithm algorithm = new Algorithm().name(DEFAULT_NAME);
        return algorithm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Algorithm createUpdatedEntity(EntityManager em) {
        Algorithm algorithm = new Algorithm().name(UPDATED_NAME);
        return algorithm;
    }

    @BeforeEach
    public void initTest() {
        algorithm = createEntity(em);
    }

    @Test
    @Transactional
    void createAlgorithm() throws Exception {
        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();
        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);
        restAlgorithmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isCreated());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate + 1);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(1)).save(testAlgorithm);
    }

    @Test
    @Transactional
    void createAlgorithmWithExistingId() throws Exception {
        // Create the Algorithm with an existing ID
        algorithm.setId(1L);
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlgorithmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(0)).save(algorithm);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = algorithmRepository.findAll().size();
        // set the field null
        algorithm.setName(null);

        // Create the Algorithm, which fails.
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        restAlgorithmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlgorithms() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList
        restAlgorithmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(algorithm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlgorithmsWithEagerRelationshipsIsEnabled() throws Exception {
        when(algorithmServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlgorithmMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(algorithmServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlgorithmsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(algorithmServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlgorithmMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(algorithmServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get the algorithm
        restAlgorithmMockMvc
            .perform(get(ENTITY_API_URL_ID, algorithm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(algorithm.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getAlgorithmsByIdFiltering() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        Long id = algorithm.getId();

        defaultAlgorithmShouldBeFound("id.equals=" + id);
        defaultAlgorithmShouldNotBeFound("id.notEquals=" + id);

        defaultAlgorithmShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlgorithmShouldNotBeFound("id.greaterThan=" + id);

        defaultAlgorithmShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlgorithmShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlgorithmsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where name equals to DEFAULT_NAME
        defaultAlgorithmShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the algorithmList where name equals to UPDATED_NAME
        defaultAlgorithmShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlgorithmsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where name not equals to DEFAULT_NAME
        defaultAlgorithmShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the algorithmList where name not equals to UPDATED_NAME
        defaultAlgorithmShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlgorithmsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAlgorithmShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the algorithmList where name equals to UPDATED_NAME
        defaultAlgorithmShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlgorithmsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where name is not null
        defaultAlgorithmShouldBeFound("name.specified=true");

        // Get all the algorithmList where name is null
        defaultAlgorithmShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlgorithmsByNameContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where name contains DEFAULT_NAME
        defaultAlgorithmShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the algorithmList where name contains UPDATED_NAME
        defaultAlgorithmShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlgorithmsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        // Get all the algorithmList where name does not contain DEFAULT_NAME
        defaultAlgorithmShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the algorithmList where name does not contain UPDATED_NAME
        defaultAlgorithmShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlgorithmsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        algorithm.addPlaceholder(placeholder);
        algorithmRepository.saveAndFlush(algorithm);
        Long placeholderId = placeholder.getId();

        // Get all the algorithmList where placeholder equals to placeholderId
        defaultAlgorithmShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the algorithmList where placeholder equals to (placeholderId + 1)
        defaultAlgorithmShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAlgorithmsByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);
        UniversallyUniqueMapping parameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            parameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(parameters);
            em.flush();
        } else {
            parameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(parameters);
        em.flush();
        algorithm.addParameters(parameters);
        algorithmRepository.saveAndFlush(algorithm);
        Long parametersId = parameters.getId();

        // Get all the algorithmList where parameters equals to parametersId
        defaultAlgorithmShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the algorithmList where parameters equals to (parametersId + 1)
        defaultAlgorithmShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlgorithmShouldBeFound(String filter) throws Exception {
        restAlgorithmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(algorithm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAlgorithmMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlgorithmShouldNotBeFound(String filter) throws Exception {
        restAlgorithmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlgorithmMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlgorithm() throws Exception {
        // Get the algorithm
        restAlgorithmMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Update the algorithm
        Algorithm updatedAlgorithm = algorithmRepository.findById(algorithm.getId()).get();
        // Disconnect from session so that the updates on updatedAlgorithm are not directly saved in db
        em.detach(updatedAlgorithm);
        updatedAlgorithm.name(UPDATED_NAME);
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(updatedAlgorithm);

        restAlgorithmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, algorithmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(algorithmDTO))
            )
            .andExpect(status().isOk());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository).save(testAlgorithm);
    }

    @Test
    @Transactional
    void putNonExistingAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();
        algorithm.setId(count.incrementAndGet());

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, algorithmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(algorithmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(0)).save(algorithm);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();
        algorithm.setId(count.incrementAndGet());

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(algorithmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(0)).save(algorithm);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();
        algorithm.setId(count.incrementAndGet());

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(0)).save(algorithm);
    }

    @Test
    @Transactional
    void partialUpdateAlgorithmWithPatch() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Update the algorithm using partial update
        Algorithm partialUpdatedAlgorithm = new Algorithm();
        partialUpdatedAlgorithm.setId(algorithm.getId());

        partialUpdatedAlgorithm.name(UPDATED_NAME);

        restAlgorithmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlgorithm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlgorithm))
            )
            .andExpect(status().isOk());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAlgorithmWithPatch() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Update the algorithm using partial update
        Algorithm partialUpdatedAlgorithm = new Algorithm();
        partialUpdatedAlgorithm.setId(algorithm.getId());

        partialUpdatedAlgorithm.name(UPDATED_NAME);

        restAlgorithmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlgorithm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlgorithm))
            )
            .andExpect(status().isOk());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();
        algorithm.setId(count.incrementAndGet());

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, algorithmDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(algorithmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(0)).save(algorithm);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();
        algorithm.setId(count.incrementAndGet());

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(algorithmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(0)).save(algorithm);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();
        algorithm.setId(count.incrementAndGet());

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(algorithmDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(0)).save(algorithm);
    }

    @Test
    @Transactional
    void deleteAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);

        int databaseSizeBeforeDelete = algorithmRepository.findAll().size();

        // Delete the algorithm
        restAlgorithmMockMvc
            .perform(delete(ENTITY_API_URL_ID, algorithm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Algorithm in Elasticsearch
        verify(mockAlgorithmSearchRepository, times(1)).deleteById(algorithm.getId());
    }

    @Test
    @Transactional
    void searchAlgorithm() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        algorithmRepository.saveAndFlush(algorithm);
        when(mockAlgorithmSearchRepository.search("id:" + algorithm.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(algorithm), PageRequest.of(0, 1), 1));

        // Search the algorithm
        restAlgorithmMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + algorithm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(algorithm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
