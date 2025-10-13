///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

describe('TALeaseInterestAccrualRule e2e test', () => {
  const tALeaseInterestAccrualRulePageUrl = '/ta-lease-interest-accrual-rule';
  const tALeaseInterestAccrualRulePageUrlPattern = new RegExp('/ta-lease-interest-accrual-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tALeaseInterestAccrualRuleSample = { name: 'Czech online Cross-platform', identifier: '150d68fa-7692-4a81-a5e7-0ba63aace7f1' };

  let tALeaseInterestAccrualRule: any;
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
      body: {"bookingId":"definition Renminbi Market","leaseTitle":"Creative","shortTitle":"Soap bricks-and-clicks Soft","description":"parse copying","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"cbb0d256-5939-4dcc-b350-47936002449e"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"index","accountName":"Checking Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"ASSET","accountSubType":"OTHER_COMPREHENSIVE_INCOME","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ta-lease-interest-accrual-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ta-lease-interest-accrual-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ta-lease-interest-accrual-rules/*').as('deleteEntityRequest');
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
    if (tALeaseInterestAccrualRule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ta-lease-interest-accrual-rules/${tALeaseInterestAccrualRule.id}`,
      }).then(() => {
        tALeaseInterestAccrualRule = undefined;
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

  it('TALeaseInterestAccrualRules menu should load TALeaseInterestAccrualRules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ta-lease-interest-accrual-rule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TALeaseInterestAccrualRule').should('exist');
    cy.url().should('match', tALeaseInterestAccrualRulePageUrlPattern);
  });

  describe('TALeaseInterestAccrualRule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tALeaseInterestAccrualRulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TALeaseInterestAccrualRule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ta-lease-interest-accrual-rule/new$'));
        cy.getEntityCreateUpdateHeading('TALeaseInterestAccrualRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseInterestAccrualRulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ta-lease-interest-accrual-rules',
  
          body: {
            ...tALeaseInterestAccrualRuleSample,
            leaseContract: iFRS16LeaseContract,
            debit: transactionAccount,
            credit: transactionAccount,
          },
        }).then(({ body }) => {
          tALeaseInterestAccrualRule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ta-lease-interest-accrual-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tALeaseInterestAccrualRule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tALeaseInterestAccrualRulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(tALeaseInterestAccrualRulePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TALeaseInterestAccrualRule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tALeaseInterestAccrualRule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseInterestAccrualRulePageUrlPattern);
      });

      it('edit button click should load edit TALeaseInterestAccrualRule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TALeaseInterestAccrualRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseInterestAccrualRulePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TALeaseInterestAccrualRule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tALeaseInterestAccrualRule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseInterestAccrualRulePageUrlPattern);

        tALeaseInterestAccrualRule = undefined;
      });
    });
  });

  describe('new TALeaseInterestAccrualRule page', () => {
    beforeEach(() => {
      cy.visit(`${tALeaseInterestAccrualRulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TALeaseInterestAccrualRule');
    });

    it.skip('should create an instance of TALeaseInterestAccrualRule', () => {
      cy.get(`[data-cy="name"]`).type('Total View Generic').should('have.value', 'Total View Generic');

      cy.get(`[data-cy="identifier"]`)
        .type('b47198a4-f1b4-4d2b-b47b-c81f43e391b1')
        .invoke('val')
        .should('match', new RegExp('b47198a4-f1b4-4d2b-b47b-c81f43e391b1'));

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="debit"]`).select(1);
      cy.get(`[data-cy="credit"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tALeaseInterestAccrualRule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tALeaseInterestAccrualRulePageUrlPattern);
    });
  });
});
