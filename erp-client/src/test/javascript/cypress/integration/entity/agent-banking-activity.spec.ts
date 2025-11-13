///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('AgentBankingActivity e2e test', () => {
  const agentBankingActivityPageUrl = '/agent-banking-activity';
  const agentBankingActivityPageUrlPattern = new RegExp('/agent-banking-activity(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const agentBankingActivitySample = {
    reportingDate: '2023-10-04',
    agentUniqueId: 'Tasty',
    terminalUniqueId: 'Licensed metrics',
    totalCountOfTransactions: 4093,
    totalValueOfTransactionsInLCY: 36362,
  };

  let agentBankingActivity: any;
  let institutionCode: any;
  let bankBranchCode: any;
  let bankTransactionType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/institution-codes',
      body: {
        institutionCode: 'Graphic',
        institutionName: 'bypassing Ergonomic',
        shortName: 'SQL',
        category: 'Dinar Computers (Keeling)',
        institutionCategory: 'Soft Generic Shirt',
        institutionOwnership: 'Fork',
        dateLicensed: '2022-04-05',
        institutionStatus: 'Principal Fantastic York',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'Liechtenstein Chair',
        bankName: 'Wooden Data Solomon',
        branchCode: 'synergy Account Tuna',
        branchName: 'Generic Configuration Belarus',
        notes: 'calculate Strategist',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-transaction-types',
      body: { transactionTypeCode: 'implement Idaho array', transactionTypeDetails: 'Canadian Associate' },
    }).then(({ body }) => {
      bankTransactionType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agent-banking-activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agent-banking-activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agent-banking-activities/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/bank-branch-codes', {
      statusCode: 200,
      body: [bankBranchCode],
    });

    cy.intercept('GET', '/api/bank-transaction-types', {
      statusCode: 200,
      body: [bankTransactionType],
    });
  });

  afterEach(() => {
    if (agentBankingActivity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agent-banking-activities/${agentBankingActivity.id}`,
      }).then(() => {
        agentBankingActivity = undefined;
      });
    }
  });

  afterEach(() => {
    if (institutionCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-codes/${institutionCode.id}`,
      }).then(() => {
        institutionCode = undefined;
      });
    }
    if (bankBranchCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-branch-codes/${bankBranchCode.id}`,
      }).then(() => {
        bankBranchCode = undefined;
      });
    }
    if (bankTransactionType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-transaction-types/${bankTransactionType.id}`,
      }).then(() => {
        bankTransactionType = undefined;
      });
    }
  });

  it('AgentBankingActivities menu should load AgentBankingActivities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agent-banking-activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgentBankingActivity').should('exist');
    cy.url().should('match', agentBankingActivityPageUrlPattern);
  });

  describe('AgentBankingActivity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agentBankingActivityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgentBankingActivity page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/agent-banking-activity/new$'));
        cy.getEntityCreateUpdateHeading('AgentBankingActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agentBankingActivityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agent-banking-activities',

          body: {
            ...agentBankingActivitySample,
            bankCode: institutionCode,
            branchCode: bankBranchCode,
            transactionType: bankTransactionType,
          },
        }).then(({ body }) => {
          agentBankingActivity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agent-banking-activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agentBankingActivity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(agentBankingActivityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgentBankingActivity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agentBankingActivity');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agentBankingActivityPageUrlPattern);
      });

      it('edit button click should load edit AgentBankingActivity page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgentBankingActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agentBankingActivityPageUrlPattern);
      });

      it('last delete button click should delete instance of AgentBankingActivity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('agentBankingActivity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agentBankingActivityPageUrlPattern);

        agentBankingActivity = undefined;
      });
    });
  });

  describe('new AgentBankingActivity page', () => {
    beforeEach(() => {
      cy.visit(`${agentBankingActivityPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AgentBankingActivity');
    });

    it('should create an instance of AgentBankingActivity', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="agentUniqueId"]`).type('circuit Internal').should('have.value', 'circuit Internal');

      cy.get(`[data-cy="terminalUniqueId"]`).type('synthesizing International').should('have.value', 'synthesizing International');

      cy.get(`[data-cy="totalCountOfTransactions"]`).type('13654').should('have.value', '13654');

      cy.get(`[data-cy="totalValueOfTransactionsInLCY"]`).type('26001').should('have.value', '26001');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="branchCode"]`).select(1);
      cy.get(`[data-cy="transactionType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        agentBankingActivity = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', agentBankingActivityPageUrlPattern);
    });
  });
});
