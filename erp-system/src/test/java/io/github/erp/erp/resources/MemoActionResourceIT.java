package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.MemoAction;
import io.github.erp.repository.MemoActionRepository;
import io.github.erp.repository.search.MemoActionSearchRepository;
import io.github.erp.web.rest.MemoActionResource;
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
 * Integration tests for the {@link MemoActionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PAYMENTS_USER", "FIXED_ASSETS_USER"})
class MemoActionResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments/memo-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payments/_search/memo-actions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemoActionRepository memoActionRepository;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MemoActionSearchRepositoryMockConfiguration
     */
    @Autowired
    private MemoActionSearchRepository mockMemoActionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemoActionMockMvc;

    private MemoAction memoAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemoAction createEntity(EntityManager em) {
        MemoAction memoAction = new MemoAction().action(DEFAULT_ACTION);
        return memoAction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemoAction createUpdatedEntity(EntityManager em) {
        MemoAction memoAction = new MemoAction().action(UPDATED_ACTION);
        return memoAction;
    }

    @BeforeEach
    public void initTest() {
        memoAction = createEntity(em);
    }

    @Test
    @Transactional
    void createMemoAction() throws Exception {
        int databaseSizeBeforeCreate = memoActionRepository.findAll().size();
        // Create the MemoAction
        restMemoActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memoAction)))
            .andExpect(status().isCreated());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeCreate + 1);
        MemoAction testMemoAction = memoActionList.get(memoActionList.size() - 1);
        assertThat(testMemoAction.getAction()).isEqualTo(DEFAULT_ACTION);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository, times(1)).save(testMemoAction);
    }

    @Test
    @Transactional
    void createMemoActionWithExistingId() throws Exception {
        // Create the MemoAction with an existing ID
        memoAction.setId(1L);

        int databaseSizeBeforeCreate = memoActionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemoActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memoAction)))
            .andExpect(status().isBadRequest());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeCreate);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository, times(0)).save(memoAction);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = memoActionRepository.findAll().size();
        // set the field null
        memoAction.setAction(null);

        // Create the MemoAction, which fails.

        restMemoActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memoAction)))
            .andExpect(status().isBadRequest());

        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemoActions() throws Exception {
        // Initialize the database
        memoActionRepository.saveAndFlush(memoAction);

        // Get all the memoActionList
        restMemoActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memoAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)));
    }

    @Test
    @Transactional
    void getMemoAction() throws Exception {
        // Initialize the database
        memoActionRepository.saveAndFlush(memoAction);

        // Get the memoAction
        restMemoActionMockMvc
            .perform(get(ENTITY_API_URL_ID, memoAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memoAction.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION));
    }

    @Test
    @Transactional
    void getNonExistingMemoAction() throws Exception {
        // Get the memoAction
        restMemoActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMemoAction() throws Exception {
        // Initialize the database
        memoActionRepository.saveAndFlush(memoAction);

        int databaseSizeBeforeUpdate = memoActionRepository.findAll().size();

        // Update the memoAction
        MemoAction updatedMemoAction = memoActionRepository.findById(memoAction.getId()).get();
        // Disconnect from session so that the updates on updatedMemoAction are not directly saved in db
        em.detach(updatedMemoAction);
        updatedMemoAction.action(UPDATED_ACTION);

        restMemoActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMemoAction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMemoAction))
            )
            .andExpect(status().isOk());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeUpdate);
        MemoAction testMemoAction = memoActionList.get(memoActionList.size() - 1);
        assertThat(testMemoAction.getAction()).isEqualTo(UPDATED_ACTION);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository).save(testMemoAction);
    }

    @Test
    @Transactional
    void putNonExistingMemoAction() throws Exception {
        int databaseSizeBeforeUpdate = memoActionRepository.findAll().size();
        memoAction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memoAction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memoAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository, times(0)).save(memoAction);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemoAction() throws Exception {
        int databaseSizeBeforeUpdate = memoActionRepository.findAll().size();
        memoAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memoAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository, times(0)).save(memoAction);
    }

    @Test
    @Transactional
    void partialUpdateMemoActionWithPatch() throws Exception {
        // Initialize the database
        memoActionRepository.saveAndFlush(memoAction);

        int databaseSizeBeforeUpdate = memoActionRepository.findAll().size();

        // Update the memoAction using partial update
        MemoAction partialUpdatedMemoAction = new MemoAction();
        partialUpdatedMemoAction.setId(memoAction.getId());

        restMemoActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemoAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemoAction))
            )
            .andExpect(status().isOk());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeUpdate);
        MemoAction testMemoAction = memoActionList.get(memoActionList.size() - 1);
        assertThat(testMemoAction.getAction()).isEqualTo(DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void fullUpdateMemoActionWithPatch() throws Exception {
        // Initialize the database
        memoActionRepository.saveAndFlush(memoAction);

        int databaseSizeBeforeUpdate = memoActionRepository.findAll().size();

        // Update the memoAction using partial update
        MemoAction partialUpdatedMemoAction = new MemoAction();
        partialUpdatedMemoAction.setId(memoAction.getId());

        partialUpdatedMemoAction.action(UPDATED_ACTION);

        restMemoActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemoAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemoAction))
            )
            .andExpect(status().isOk());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeUpdate);
        MemoAction testMemoAction = memoActionList.get(memoActionList.size() - 1);
        assertThat(testMemoAction.getAction()).isEqualTo(UPDATED_ACTION);
    }

    @Test
    @Transactional
    void patchNonExistingMemoAction() throws Exception {
        int databaseSizeBeforeUpdate = memoActionRepository.findAll().size();
        memoAction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memoAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memoAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository, times(0)).save(memoAction);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemoAction() throws Exception {
        int databaseSizeBeforeUpdate = memoActionRepository.findAll().size();
        memoAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemoActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memoAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemoAction in the database
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository, times(0)).save(memoAction);
    }

    @Test
    @Transactional
    void deleteMemoAction() throws Exception {
        // Initialize the database
        memoActionRepository.saveAndFlush(memoAction);

        int databaseSizeBeforeDelete = memoActionRepository.findAll().size();

        // Delete the memoAction
        restMemoActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, memoAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemoAction> memoActionList = memoActionRepository.findAll();
        assertThat(memoActionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MemoAction in Elasticsearch
        verify(mockMemoActionSearchRepository, times(1)).deleteById(memoAction.getId());
    }

    @Test
    @Transactional
    void searchMemoAction() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        memoActionRepository.saveAndFlush(memoAction);
        when(mockMemoActionSearchRepository.search("id:" + memoAction.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(memoAction), PageRequest.of(0, 1), 1));

        // Search the memoAction
        restMemoActionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + memoAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memoAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)));
    }
}
