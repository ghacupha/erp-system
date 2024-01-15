package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AmortizationPostingReport;
import io.github.erp.repository.AmortizationPostingReportRepository;
import io.github.erp.repository.search.AmortizationPostingReportSearchRepository;
import io.github.erp.service.criteria.AmortizationPostingReportCriteria;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import io.github.erp.service.mapper.AmortizationPostingReportMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link AmortizationPostingReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AmortizationPostingReportResourceIT {

    private static final String DEFAULT_CATALOGUE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CATALOGUE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEBIT_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DEBIT_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMORTIZATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMORTIZATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMORTIZATION_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/amortization-posting-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/amortization-posting-reports";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmortizationPostingReportRepository amortizationPostingReportRepository;

    @Autowired
    private AmortizationPostingReportMapper amortizationPostingReportMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AmortizationPostingReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private AmortizationPostingReportSearchRepository mockAmortizationPostingReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmortizationPostingReportMockMvc;

    private AmortizationPostingReport amortizationPostingReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationPostingReport createEntity(EntityManager em) {
        AmortizationPostingReport amortizationPostingReport = new AmortizationPostingReport()
            .catalogueNumber(DEFAULT_CATALOGUE_NUMBER)
            .debitAccount(DEFAULT_DEBIT_ACCOUNT)
            .creditAccount(DEFAULT_CREDIT_ACCOUNT)
            .description(DEFAULT_DESCRIPTION)
            .amortizationAmount(DEFAULT_AMORTIZATION_AMOUNT);
        return amortizationPostingReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AmortizationPostingReport createUpdatedEntity(EntityManager em) {
        AmortizationPostingReport amortizationPostingReport = new AmortizationPostingReport()
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .debitAccount(UPDATED_DEBIT_ACCOUNT)
            .creditAccount(UPDATED_CREDIT_ACCOUNT)
            .description(UPDATED_DESCRIPTION)
            .amortizationAmount(UPDATED_AMORTIZATION_AMOUNT);
        return amortizationPostingReport;
    }

    @BeforeEach
    public void initTest() {
        amortizationPostingReport = createEntity(em);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReports() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList
        restAmortizationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPostingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].debitAccount").value(hasItem(DEFAULT_DEBIT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].creditAccount").value(hasItem(DEFAULT_CREDIT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amortizationAmount").value(hasItem(sameNumber(DEFAULT_AMORTIZATION_AMOUNT))));
    }

    @Test
    @Transactional
    void getAmortizationPostingReport() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get the amortizationPostingReport
        restAmortizationPostingReportMockMvc
            .perform(get(ENTITY_API_URL_ID, amortizationPostingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amortizationPostingReport.getId().intValue()))
            .andExpect(jsonPath("$.catalogueNumber").value(DEFAULT_CATALOGUE_NUMBER))
            .andExpect(jsonPath("$.debitAccount").value(DEFAULT_DEBIT_ACCOUNT))
            .andExpect(jsonPath("$.creditAccount").value(DEFAULT_CREDIT_ACCOUNT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amortizationAmount").value(sameNumber(DEFAULT_AMORTIZATION_AMOUNT)));
    }

    @Test
    @Transactional
    void getAmortizationPostingReportsByIdFiltering() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        Long id = amortizationPostingReport.getId();

        defaultAmortizationPostingReportShouldBeFound("id.equals=" + id);
        defaultAmortizationPostingReportShouldNotBeFound("id.notEquals=" + id);

        defaultAmortizationPostingReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAmortizationPostingReportShouldNotBeFound("id.greaterThan=" + id);

        defaultAmortizationPostingReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAmortizationPostingReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCatalogueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where catalogueNumber equals to DEFAULT_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldBeFound("catalogueNumber.equals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the amortizationPostingReportList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldNotBeFound("catalogueNumber.equals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCatalogueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where catalogueNumber not equals to DEFAULT_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldNotBeFound("catalogueNumber.notEquals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the amortizationPostingReportList where catalogueNumber not equals to UPDATED_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldBeFound("catalogueNumber.notEquals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCatalogueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where catalogueNumber in DEFAULT_CATALOGUE_NUMBER or UPDATED_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldBeFound("catalogueNumber.in=" + DEFAULT_CATALOGUE_NUMBER + "," + UPDATED_CATALOGUE_NUMBER);

        // Get all the amortizationPostingReportList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldNotBeFound("catalogueNumber.in=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCatalogueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where catalogueNumber is not null
        defaultAmortizationPostingReportShouldBeFound("catalogueNumber.specified=true");

        // Get all the amortizationPostingReportList where catalogueNumber is null
        defaultAmortizationPostingReportShouldNotBeFound("catalogueNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCatalogueNumberContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where catalogueNumber contains DEFAULT_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldBeFound("catalogueNumber.contains=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the amortizationPostingReportList where catalogueNumber contains UPDATED_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldNotBeFound("catalogueNumber.contains=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCatalogueNumberNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where catalogueNumber does not contain DEFAULT_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldNotBeFound("catalogueNumber.doesNotContain=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the amortizationPostingReportList where catalogueNumber does not contain UPDATED_CATALOGUE_NUMBER
        defaultAmortizationPostingReportShouldBeFound("catalogueNumber.doesNotContain=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDebitAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where debitAccount equals to DEFAULT_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("debitAccount.equals=" + DEFAULT_DEBIT_ACCOUNT);

        // Get all the amortizationPostingReportList where debitAccount equals to UPDATED_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("debitAccount.equals=" + UPDATED_DEBIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDebitAccountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where debitAccount not equals to DEFAULT_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("debitAccount.notEquals=" + DEFAULT_DEBIT_ACCOUNT);

        // Get all the amortizationPostingReportList where debitAccount not equals to UPDATED_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("debitAccount.notEquals=" + UPDATED_DEBIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDebitAccountIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where debitAccount in DEFAULT_DEBIT_ACCOUNT or UPDATED_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("debitAccount.in=" + DEFAULT_DEBIT_ACCOUNT + "," + UPDATED_DEBIT_ACCOUNT);

        // Get all the amortizationPostingReportList where debitAccount equals to UPDATED_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("debitAccount.in=" + UPDATED_DEBIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDebitAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where debitAccount is not null
        defaultAmortizationPostingReportShouldBeFound("debitAccount.specified=true");

        // Get all the amortizationPostingReportList where debitAccount is null
        defaultAmortizationPostingReportShouldNotBeFound("debitAccount.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDebitAccountContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where debitAccount contains DEFAULT_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("debitAccount.contains=" + DEFAULT_DEBIT_ACCOUNT);

        // Get all the amortizationPostingReportList where debitAccount contains UPDATED_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("debitAccount.contains=" + UPDATED_DEBIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDebitAccountNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where debitAccount does not contain DEFAULT_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("debitAccount.doesNotContain=" + DEFAULT_DEBIT_ACCOUNT);

        // Get all the amortizationPostingReportList where debitAccount does not contain UPDATED_DEBIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("debitAccount.doesNotContain=" + UPDATED_DEBIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCreditAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where creditAccount equals to DEFAULT_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("creditAccount.equals=" + DEFAULT_CREDIT_ACCOUNT);

        // Get all the amortizationPostingReportList where creditAccount equals to UPDATED_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("creditAccount.equals=" + UPDATED_CREDIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCreditAccountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where creditAccount not equals to DEFAULT_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("creditAccount.notEquals=" + DEFAULT_CREDIT_ACCOUNT);

        // Get all the amortizationPostingReportList where creditAccount not equals to UPDATED_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("creditAccount.notEquals=" + UPDATED_CREDIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCreditAccountIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where creditAccount in DEFAULT_CREDIT_ACCOUNT or UPDATED_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("creditAccount.in=" + DEFAULT_CREDIT_ACCOUNT + "," + UPDATED_CREDIT_ACCOUNT);

        // Get all the amortizationPostingReportList where creditAccount equals to UPDATED_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("creditAccount.in=" + UPDATED_CREDIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCreditAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where creditAccount is not null
        defaultAmortizationPostingReportShouldBeFound("creditAccount.specified=true");

        // Get all the amortizationPostingReportList where creditAccount is null
        defaultAmortizationPostingReportShouldNotBeFound("creditAccount.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCreditAccountContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where creditAccount contains DEFAULT_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("creditAccount.contains=" + DEFAULT_CREDIT_ACCOUNT);

        // Get all the amortizationPostingReportList where creditAccount contains UPDATED_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("creditAccount.contains=" + UPDATED_CREDIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByCreditAccountNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where creditAccount does not contain DEFAULT_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldNotBeFound("creditAccount.doesNotContain=" + DEFAULT_CREDIT_ACCOUNT);

        // Get all the amortizationPostingReportList where creditAccount does not contain UPDATED_CREDIT_ACCOUNT
        defaultAmortizationPostingReportShouldBeFound("creditAccount.doesNotContain=" + UPDATED_CREDIT_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where description equals to DEFAULT_DESCRIPTION
        defaultAmortizationPostingReportShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the amortizationPostingReportList where description equals to UPDATED_DESCRIPTION
        defaultAmortizationPostingReportShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where description not equals to DEFAULT_DESCRIPTION
        defaultAmortizationPostingReportShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the amortizationPostingReportList where description not equals to UPDATED_DESCRIPTION
        defaultAmortizationPostingReportShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAmortizationPostingReportShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the amortizationPostingReportList where description equals to UPDATED_DESCRIPTION
        defaultAmortizationPostingReportShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where description is not null
        defaultAmortizationPostingReportShouldBeFound("description.specified=true");

        // Get all the amortizationPostingReportList where description is null
        defaultAmortizationPostingReportShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where description contains DEFAULT_DESCRIPTION
        defaultAmortizationPostingReportShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the amortizationPostingReportList where description contains UPDATED_DESCRIPTION
        defaultAmortizationPostingReportShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where description does not contain DEFAULT_DESCRIPTION
        defaultAmortizationPostingReportShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the amortizationPostingReportList where description does not contain UPDATED_DESCRIPTION
        defaultAmortizationPostingReportShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount equals to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldBeFound("amortizationAmount.equals=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationPostingReportList where amortizationAmount equals to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.equals=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount not equals to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.notEquals=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationPostingReportList where amortizationAmount not equals to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldBeFound("amortizationAmount.notEquals=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount in DEFAULT_AMORTIZATION_AMOUNT or UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldBeFound(
            "amortizationAmount.in=" + DEFAULT_AMORTIZATION_AMOUNT + "," + UPDATED_AMORTIZATION_AMOUNT
        );

        // Get all the amortizationPostingReportList where amortizationAmount equals to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.in=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount is not null
        defaultAmortizationPostingReportShouldBeFound("amortizationAmount.specified=true");

        // Get all the amortizationPostingReportList where amortizationAmount is null
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount is greater than or equal to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldBeFound("amortizationAmount.greaterThanOrEqual=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationPostingReportList where amortizationAmount is greater than or equal to UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.greaterThanOrEqual=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount is less than or equal to DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldBeFound("amortizationAmount.lessThanOrEqual=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationPostingReportList where amortizationAmount is less than or equal to SMALLER_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.lessThanOrEqual=" + SMALLER_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount is less than DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.lessThan=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationPostingReportList where amortizationAmount is less than UPDATED_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldBeFound("amortizationAmount.lessThan=" + UPDATED_AMORTIZATION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAmortizationPostingReportsByAmortizationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);

        // Get all the amortizationPostingReportList where amortizationAmount is greater than DEFAULT_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldNotBeFound("amortizationAmount.greaterThan=" + DEFAULT_AMORTIZATION_AMOUNT);

        // Get all the amortizationPostingReportList where amortizationAmount is greater than SMALLER_AMORTIZATION_AMOUNT
        defaultAmortizationPostingReportShouldBeFound("amortizationAmount.greaterThan=" + SMALLER_AMORTIZATION_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAmortizationPostingReportShouldBeFound(String filter) throws Exception {
        restAmortizationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPostingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].debitAccount").value(hasItem(DEFAULT_DEBIT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].creditAccount").value(hasItem(DEFAULT_CREDIT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amortizationAmount").value(hasItem(sameNumber(DEFAULT_AMORTIZATION_AMOUNT))));

        // Check, that the count call also returns 1
        restAmortizationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAmortizationPostingReportShouldNotBeFound(String filter) throws Exception {
        restAmortizationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAmortizationPostingReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAmortizationPostingReport() throws Exception {
        // Get the amortizationPostingReport
        restAmortizationPostingReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchAmortizationPostingReport() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        amortizationPostingReportRepository.saveAndFlush(amortizationPostingReport);
        when(mockAmortizationPostingReportSearchRepository.search("id:" + amortizationPostingReport.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(amortizationPostingReport), PageRequest.of(0, 1), 1));

        // Search the amortizationPostingReport
        restAmortizationPostingReportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + amortizationPostingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amortizationPostingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].debitAccount").value(hasItem(DEFAULT_DEBIT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].creditAccount").value(hasItem(DEFAULT_CREDIT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amortizationAmount").value(hasItem(sameNumber(DEFAULT_AMORTIZATION_AMOUNT))));
    }
}
