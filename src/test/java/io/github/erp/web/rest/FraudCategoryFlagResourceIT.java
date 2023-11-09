package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.FraudCategoryFlag;
import io.github.erp.domain.enumeration.FlagCodes;
import io.github.erp.repository.FraudCategoryFlagRepository;
import io.github.erp.repository.search.FraudCategoryFlagSearchRepository;
import io.github.erp.service.dto.FraudCategoryFlagDTO;
import io.github.erp.service.mapper.FraudCategoryFlagMapper;
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
 * Integration tests for the {@link FraudCategoryFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FraudCategoryFlagResourceIT {

    private static final FlagCodes DEFAULT_FRAUD_CATEGORY_FLAG = FlagCodes.Y;
    private static final FlagCodes UPDATED_FRAUD_CATEGORY_FLAG = FlagCodes.N;

    private static final String DEFAULT_FRAUD_CATEGORY_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_FRAUD_CATEGORY_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fraud-category-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fraud-category-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FraudCategoryFlagRepository fraudCategoryFlagRepository;

    @Autowired
    private FraudCategoryFlagMapper fraudCategoryFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FraudCategoryFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private FraudCategoryFlagSearchRepository mockFraudCategoryFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraudCategoryFlagMockMvc;

    private FraudCategoryFlag fraudCategoryFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraudCategoryFlag createEntity(EntityManager em) {
        FraudCategoryFlag fraudCategoryFlag = new FraudCategoryFlag()
            .fraudCategoryFlag(DEFAULT_FRAUD_CATEGORY_FLAG)
            .fraudCategoryTypeDetails(DEFAULT_FRAUD_CATEGORY_TYPE_DETAILS);
        return fraudCategoryFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraudCategoryFlag createUpdatedEntity(EntityManager em) {
        FraudCategoryFlag fraudCategoryFlag = new FraudCategoryFlag()
            .fraudCategoryFlag(UPDATED_FRAUD_CATEGORY_FLAG)
            .fraudCategoryTypeDetails(UPDATED_FRAUD_CATEGORY_TYPE_DETAILS);
        return fraudCategoryFlag;
    }

    @BeforeEach
    public void initTest() {
        fraudCategoryFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createFraudCategoryFlag() throws Exception {
        int databaseSizeBeforeCreate = fraudCategoryFlagRepository.findAll().size();
        // Create the FraudCategoryFlag
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);
        restFraudCategoryFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeCreate + 1);
        FraudCategoryFlag testFraudCategoryFlag = fraudCategoryFlagList.get(fraudCategoryFlagList.size() - 1);
        assertThat(testFraudCategoryFlag.getFraudCategoryFlag()).isEqualTo(DEFAULT_FRAUD_CATEGORY_FLAG);
        assertThat(testFraudCategoryFlag.getFraudCategoryTypeDetails()).isEqualTo(DEFAULT_FRAUD_CATEGORY_TYPE_DETAILS);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(1)).save(testFraudCategoryFlag);
    }

    @Test
    @Transactional
    void createFraudCategoryFlagWithExistingId() throws Exception {
        // Create the FraudCategoryFlag with an existing ID
        fraudCategoryFlag.setId(1L);
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        int databaseSizeBeforeCreate = fraudCategoryFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraudCategoryFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(0)).save(fraudCategoryFlag);
    }

    @Test
    @Transactional
    void checkFraudCategoryFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = fraudCategoryFlagRepository.findAll().size();
        // set the field null
        fraudCategoryFlag.setFraudCategoryFlag(null);

        // Create the FraudCategoryFlag, which fails.
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        restFraudCategoryFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFraudCategoryFlags() throws Exception {
        // Initialize the database
        fraudCategoryFlagRepository.saveAndFlush(fraudCategoryFlag);

        // Get all the fraudCategoryFlagList
        restFraudCategoryFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fraudCategoryFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].fraudCategoryFlag").value(hasItem(DEFAULT_FRAUD_CATEGORY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].fraudCategoryTypeDetails").value(hasItem(DEFAULT_FRAUD_CATEGORY_TYPE_DETAILS)));
    }

    @Test
    @Transactional
    void getFraudCategoryFlag() throws Exception {
        // Initialize the database
        fraudCategoryFlagRepository.saveAndFlush(fraudCategoryFlag);

        // Get the fraudCategoryFlag
        restFraudCategoryFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, fraudCategoryFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fraudCategoryFlag.getId().intValue()))
            .andExpect(jsonPath("$.fraudCategoryFlag").value(DEFAULT_FRAUD_CATEGORY_FLAG.toString()))
            .andExpect(jsonPath("$.fraudCategoryTypeDetails").value(DEFAULT_FRAUD_CATEGORY_TYPE_DETAILS));
    }

    @Test
    @Transactional
    void getNonExistingFraudCategoryFlag() throws Exception {
        // Get the fraudCategoryFlag
        restFraudCategoryFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFraudCategoryFlag() throws Exception {
        // Initialize the database
        fraudCategoryFlagRepository.saveAndFlush(fraudCategoryFlag);

        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();

        // Update the fraudCategoryFlag
        FraudCategoryFlag updatedFraudCategoryFlag = fraudCategoryFlagRepository.findById(fraudCategoryFlag.getId()).get();
        // Disconnect from session so that the updates on updatedFraudCategoryFlag are not directly saved in db
        em.detach(updatedFraudCategoryFlag);
        updatedFraudCategoryFlag
            .fraudCategoryFlag(UPDATED_FRAUD_CATEGORY_FLAG)
            .fraudCategoryTypeDetails(UPDATED_FRAUD_CATEGORY_TYPE_DETAILS);
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(updatedFraudCategoryFlag);

        restFraudCategoryFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraudCategoryFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);
        FraudCategoryFlag testFraudCategoryFlag = fraudCategoryFlagList.get(fraudCategoryFlagList.size() - 1);
        assertThat(testFraudCategoryFlag.getFraudCategoryFlag()).isEqualTo(UPDATED_FRAUD_CATEGORY_FLAG);
        assertThat(testFraudCategoryFlag.getFraudCategoryTypeDetails()).isEqualTo(UPDATED_FRAUD_CATEGORY_TYPE_DETAILS);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository).save(testFraudCategoryFlag);
    }

    @Test
    @Transactional
    void putNonExistingFraudCategoryFlag() throws Exception {
        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();
        fraudCategoryFlag.setId(count.incrementAndGet());

        // Create the FraudCategoryFlag
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraudCategoryFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraudCategoryFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(0)).save(fraudCategoryFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchFraudCategoryFlag() throws Exception {
        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();
        fraudCategoryFlag.setId(count.incrementAndGet());

        // Create the FraudCategoryFlag
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudCategoryFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(0)).save(fraudCategoryFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFraudCategoryFlag() throws Exception {
        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();
        fraudCategoryFlag.setId(count.incrementAndGet());

        // Create the FraudCategoryFlag
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudCategoryFlagMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(0)).save(fraudCategoryFlag);
    }

    @Test
    @Transactional
    void partialUpdateFraudCategoryFlagWithPatch() throws Exception {
        // Initialize the database
        fraudCategoryFlagRepository.saveAndFlush(fraudCategoryFlag);

        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();

        // Update the fraudCategoryFlag using partial update
        FraudCategoryFlag partialUpdatedFraudCategoryFlag = new FraudCategoryFlag();
        partialUpdatedFraudCategoryFlag.setId(fraudCategoryFlag.getId());

        partialUpdatedFraudCategoryFlag
            .fraudCategoryFlag(UPDATED_FRAUD_CATEGORY_FLAG)
            .fraudCategoryTypeDetails(UPDATED_FRAUD_CATEGORY_TYPE_DETAILS);

        restFraudCategoryFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraudCategoryFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFraudCategoryFlag))
            )
            .andExpect(status().isOk());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);
        FraudCategoryFlag testFraudCategoryFlag = fraudCategoryFlagList.get(fraudCategoryFlagList.size() - 1);
        assertThat(testFraudCategoryFlag.getFraudCategoryFlag()).isEqualTo(UPDATED_FRAUD_CATEGORY_FLAG);
        assertThat(testFraudCategoryFlag.getFraudCategoryTypeDetails()).isEqualTo(UPDATED_FRAUD_CATEGORY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateFraudCategoryFlagWithPatch() throws Exception {
        // Initialize the database
        fraudCategoryFlagRepository.saveAndFlush(fraudCategoryFlag);

        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();

        // Update the fraudCategoryFlag using partial update
        FraudCategoryFlag partialUpdatedFraudCategoryFlag = new FraudCategoryFlag();
        partialUpdatedFraudCategoryFlag.setId(fraudCategoryFlag.getId());

        partialUpdatedFraudCategoryFlag
            .fraudCategoryFlag(UPDATED_FRAUD_CATEGORY_FLAG)
            .fraudCategoryTypeDetails(UPDATED_FRAUD_CATEGORY_TYPE_DETAILS);

        restFraudCategoryFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraudCategoryFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFraudCategoryFlag))
            )
            .andExpect(status().isOk());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);
        FraudCategoryFlag testFraudCategoryFlag = fraudCategoryFlagList.get(fraudCategoryFlagList.size() - 1);
        assertThat(testFraudCategoryFlag.getFraudCategoryFlag()).isEqualTo(UPDATED_FRAUD_CATEGORY_FLAG);
        assertThat(testFraudCategoryFlag.getFraudCategoryTypeDetails()).isEqualTo(UPDATED_FRAUD_CATEGORY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingFraudCategoryFlag() throws Exception {
        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();
        fraudCategoryFlag.setId(count.incrementAndGet());

        // Create the FraudCategoryFlag
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraudCategoryFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fraudCategoryFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(0)).save(fraudCategoryFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFraudCategoryFlag() throws Exception {
        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();
        fraudCategoryFlag.setId(count.incrementAndGet());

        // Create the FraudCategoryFlag
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudCategoryFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(0)).save(fraudCategoryFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFraudCategoryFlag() throws Exception {
        int databaseSizeBeforeUpdate = fraudCategoryFlagRepository.findAll().size();
        fraudCategoryFlag.setId(count.incrementAndGet());

        // Create the FraudCategoryFlag
        FraudCategoryFlagDTO fraudCategoryFlagDTO = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudCategoryFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraudCategoryFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraudCategoryFlag in the database
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(0)).save(fraudCategoryFlag);
    }

    @Test
    @Transactional
    void deleteFraudCategoryFlag() throws Exception {
        // Initialize the database
        fraudCategoryFlagRepository.saveAndFlush(fraudCategoryFlag);

        int databaseSizeBeforeDelete = fraudCategoryFlagRepository.findAll().size();

        // Delete the fraudCategoryFlag
        restFraudCategoryFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, fraudCategoryFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FraudCategoryFlag> fraudCategoryFlagList = fraudCategoryFlagRepository.findAll();
        assertThat(fraudCategoryFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FraudCategoryFlag in Elasticsearch
        verify(mockFraudCategoryFlagSearchRepository, times(1)).deleteById(fraudCategoryFlag.getId());
    }

    @Test
    @Transactional
    void searchFraudCategoryFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fraudCategoryFlagRepository.saveAndFlush(fraudCategoryFlag);
        when(mockFraudCategoryFlagSearchRepository.search("id:" + fraudCategoryFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fraudCategoryFlag), PageRequest.of(0, 1), 1));

        // Search the fraudCategoryFlag
        restFraudCategoryFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fraudCategoryFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fraudCategoryFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].fraudCategoryFlag").value(hasItem(DEFAULT_FRAUD_CATEGORY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].fraudCategoryTypeDetails").value(hasItem(DEFAULT_FRAUD_CATEGORY_TYPE_DETAILS)));
    }
}
