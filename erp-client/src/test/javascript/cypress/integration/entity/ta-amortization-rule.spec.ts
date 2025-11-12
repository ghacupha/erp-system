///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

describe('TAAmortizationRule e2e test', () => {
  const tAAmortizationRulePageUrl = '/ta-amortization-rule';
  const tAAmortizationRulePageUrlPattern = new RegExp('/ta-amortization-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tAAmortizationRuleSample = { name: 'silver Frozen Grass-roots', identifier: '86418e19-d4f5-4c6c-b770-2ee9759ba5f5' };

  let tAAmortizationRule: any;
  //let iFRS16LeaseContract: any;
  //let transactionAccount: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/ifrs-16-lease-contracts',
      body: {"bookingId":"Tennessee","leaseTitle":"Home","shortTitle":"Ohio Andorra","description":"Direct e-commerce","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"2ecf5b6d-475f-47e0-a5df-5675577fe088"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"Integration Shoes viral","accountName":"Savings Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"ASSET","accountSubType":"ACCOUNT_RECEIVABLE","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ta-amortization-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ta-amortization-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ta-amortization-rules/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

    cy.intercept('GET', '/api/transaction-accounts', {
      statusCode: 200,
      body: [transactionAccount],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (tAAmortizationRule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ta-amortization-rules/${tAAmortizationRule.id}`,
      }).then(() => {
        tAAmortizationRule = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (iFRS16LeaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ifrs-16-lease-contracts/${iFRS16LeaseContract.id}`,
      }).then(() => {
        iFRS16LeaseContract = undefined;
      });
    }
    if (transactionAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-accounts/${transactionAccount.id}`,
      }).then(() => {
        transactionAccount = undefined;
      });
    }
  });
   */

  it('TAAmortizationRules menu should load TAAmortizationRules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ta-amortization-rule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TAAmortizationRule').should('exist');
    cy.url().should('match', tAAmortizationRulePageUrlPattern);
  });

  describe('TAAmortizationRule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tAAmortizationRulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TAAmortizationRule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ta-amortization-rule/new$'));
        cy.getEntityCreateUpdateHeading('TAAmortizationRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAAmortizationRulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ta-amortization-rules',
  
          body: {
            ...tAAmortizationRuleSample,
            leaseContract: iFRS16LeaseContract,
            debit: transactionAccount,
            credit: transactionAccount,
          },
        }).then(({ body }) => {
          tAAmortizationRule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ta-amortization-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tAAmortizationRule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tAAmortizationRulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(tAAmortizationRulePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TAAmortizationRule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tAAmortizationRule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAAmortizationRulePageUrlPattern);
      });

      it('edit button click should load edit TAAmortizationRule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TAAmortizationRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAAmortizationRulePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TAAmortizationRule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tAAmortizationRule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAAmortizationRulePageUrlPattern);

        tAAmortizationRule = undefined;
      });
    });
  });

  describe('new TAAmortizationRule page', () => {
    beforeEach(() => {
      cy.visit(`${tAAmortizationRulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TAAmortizationRule');
    });

    it.skip('should create an instance of TAAmortizationRule', () => {
      cy.get(`[data-cy="name"]`).type('IB').should('have.value', 'IB');

      cy.get(`[data-cy="identifier"]`)
        .type('0b32527f-1bde-47dc-9b4f-00796f6dc028')
        .invoke('val')
        .should('match', new RegExp('0b32527f-1bde-47dc-9b4f-00796f6dc028'));

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="debit"]`).select(1);
      cy.get(`[data-cy="credit"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tAAmortizationRule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tAAmortizationRulePageUrlPattern);
    });
  });
});
