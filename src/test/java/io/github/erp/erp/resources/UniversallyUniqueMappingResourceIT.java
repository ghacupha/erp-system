package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 17 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UniversallyUniqueMappingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UniversallyUniqueMappingResourceIT {

    private static final String DEFAULT_UNIVERSAL_KEY = "AAAAAAAAAA";
    private static final String UPDATED_UNIVERSAL_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_MAPPED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_MAPPED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/universally-unique-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/universally-unique-mappings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UniversallyUniqueMappingRepository universallyUniqueMappingRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.UniversallyUniqueMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private UniversallyUniqueMappingSearchRepository mockUniversallyUniqueMappingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniversallyUniqueMappingMockMvc;

    private UniversallyUniqueMapping universallyUniqueMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniversallyUniqueMapping createEntity(EntityManager em) {
        UniversallyUniqueMapping universallyUniqueMapping = new UniversallyUniqueMapping()
            .universalKey(DEFAULT_UNIVERSAL_KEY)
            .mappedValue(DEFAULT_MAPPED_VALUE);
        return universallyUniqueMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniversallyUniqueMapping createUpdatedEntity(EntityManager em) {
        UniversallyUniqueMapping universallyUniqueMapping = new UniversallyUniqueMapping()
            .universalKey(UPDATED_UNIVERSAL_KEY)
            .mappedValue(UPDATED_MAPPED_VALUE);
        return universallyUniqueMapping;
    }

    @BeforeEach
    public void initTest() {
        universallyUniqueMapping = createEntity(em);
    }

    @Test
    @Transactional
    void createUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeCreate = universallyUniqueMappingRepository.findAll().size();
        // Create the UniversallyUniqueMapping
        restUniversallyUniqueMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isCreated());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeCreate + 1);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(DEFAULT_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(DEFAULT_MAPPED_VALUE);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(1)).save(testUniversallyUniqueMapping);
    }

    @Test
    @Transactional
    void createUniversallyUniqueMappingWithExistingId() throws Exception {
        // Create the UniversallyUniqueMapping with an existing ID
        universallyUniqueMapping.setId(1L);

        int databaseSizeBeforeCreate = universallyUniqueMappingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniversallyUniqueMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void checkUniversalKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = universallyUniqueMappingRepository.findAll().size();
        // set the field null
        universallyUniqueMapping.setUniversalKey(null);

        // Create the UniversallyUniqueMapping, which fails.

        restUniversallyUniqueMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isBadRequest());

        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappings() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(universallyUniqueMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].universalKey").value(hasItem(DEFAULT_UNIVERSAL_KEY)))
            .andExpect(jsonPath("$.[*].mappedValue").value(hasItem(DEFAULT_MAPPED_VALUE)));
    }

    @Test
    @Transactional
    void getUniversallyUniqueMapping() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, universallyUniqueMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(universallyUniqueMapping.getId().intValue()))
            .andExpect(jsonPath("$.universalKey").value(DEFAULT_UNIVERSAL_KEY))
            .andExpect(jsonPath("$.mappedValue").value(DEFAULT_MAPPED_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingUniversallyUniqueMapping() throws Exception {
        // Get the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUniversallyUniqueMapping() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();

        // Update the universallyUniqueMapping
        UniversallyUniqueMapping updatedUniversallyUniqueMapping = universallyUniqueMappingRepository
            .findById(universallyUniqueMapping.getId())
            .get();
        // Disconnect from session so that the updates on updatedUniversallyUniqueMapping are not directly saved in db
        em.detach(updatedUniversallyUniqueMapping);
        updatedUniversallyUniqueMapping.universalKey(UPDATED_UNIVERSAL_KEY).mappedValue(UPDATED_MAPPED_VALUE);

        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUniversallyUniqueMapping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUniversallyUniqueMapping))
            )
            .andExpect(status().isOk());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(UPDATED_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(UPDATED_MAPPED_VALUE);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository).save(testUniversallyUniqueMapping);
    }

    @Test
    @Transactional
    void putNonExistingUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, universallyUniqueMapping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void putWithIdMismatchUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void partialUpdateUniversallyUniqueMappingWithPatch() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();

        // Update the universallyUniqueMapping using partial update
        UniversallyUniqueMapping partialUpdatedUniversallyUniqueMapping = new UniversallyUniqueMapping();
        partialUpdatedUniversallyUniqueMapping.setId(universallyUniqueMapping.getId());

        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversallyUniqueMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversallyUniqueMapping))
            )
            .andExpect(status().isOk());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(DEFAULT_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(DEFAULT_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateUniversallyUniqueMappingWithPatch() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();

        // Update the universallyUniqueMapping using partial update
        UniversallyUniqueMapping partialUpdatedUniversallyUniqueMapping = new UniversallyUniqueMapping();
        partialUpdatedUniversallyUniqueMapping.setId(universallyUniqueMapping.getId());

        partialUpdatedUniversallyUniqueMapping.universalKey(UPDATED_UNIVERSAL_KEY).mappedValue(UPDATED_MAPPED_VALUE);

        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversallyUniqueMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversallyUniqueMapping))
            )
            .andExpect(status().isOk());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(UPDATED_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(UPDATED_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, universallyUniqueMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMapping))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void deleteUniversallyUniqueMapping() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeDelete = universallyUniqueMappingRepository.findAll().size();

        // Delete the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, universallyUniqueMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(1)).deleteById(universallyUniqueMapping.getId());
    }

    @Test
    @Transactional
    void searchUniversallyUniqueMapping() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);
        when(mockUniversallyUniqueMappingSearchRepository.search("id:" + universallyUniqueMapping.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(universallyUniqueMapping), PageRequest.of(0, 1), 1));

        // Search the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + universallyUniqueMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(universallyUniqueMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].universalKey").value(hasItem(DEFAULT_UNIVERSAL_KEY)))
            .andExpect(jsonPath("$.[*].mappedValue").value(hasItem(DEFAULT_MAPPED_VALUE)));
    }
}
