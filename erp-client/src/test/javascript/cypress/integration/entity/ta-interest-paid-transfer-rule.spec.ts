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

describe('TAInterestPaidTransferRule e2e test', () => {
  const tAInterestPaidTransferRulePageUrl = '/ta-interest-paid-transfer-rule';
  const tAInterestPaidTransferRulePageUrlPattern = new RegExp('/ta-interest-paid-transfer-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tAInterestPaidTransferRuleSample = { name: 'Optimization parsing', identifier: 'c68e59c3-5b83-4307-bd93-6fdb0168a373' };

  let tAInterestPaidTransferRule: any;
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
      body: {"bookingId":"drive invoice","leaseTitle":"multi-byte Books payment","shortTitle":"pink Graphical","description":"bandwidth Car","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"5b4af427-8df5-447d-8751-c1bce9449d9a"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"Sausages","accountName":"Auto Loan Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"ASSET","accountSubType":"ACCOUNT_RECEIVABLE","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ta-interest-paid-transfer-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ta-interest-paid-transfer-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ta-interest-paid-transfer-rules/*').as('deleteEntityRequest');
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
    if (tAInterestPaidTransferRule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ta-interest-paid-transfer-rules/${tAInterestPaidTransferRule.id}`,
      }).then(() => {
        tAInterestPaidTransferRule = undefined;
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

  it('TAInterestPaidTransferRules menu should load TAInterestPaidTransferRules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ta-interest-paid-transfer-rule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TAInterestPaidTransferRule').should('exist');
    cy.url().should('match', tAInterestPaidTransferRulePageUrlPattern);
  });

  describe('TAInterestPaidTransferRule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tAInterestPaidTransferRulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TAInterestPaidTransferRule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ta-interest-paid-transfer-rule/new$'));
        cy.getEntityCreateUpdateHeading('TAInterestPaidTransferRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAInterestPaidTransferRulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ta-interest-paid-transfer-rules',
  
          body: {
            ...tAInterestPaidTransferRuleSample,
            leaseContract: iFRS16LeaseContract,
            debit: transactionAccount,
            credit: transactionAccount,
          },
        }).then(({ body }) => {
          tAInterestPaidTransferRule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ta-interest-paid-transfer-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tAInterestPaidTransferRule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tAInterestPaidTransferRulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(tAInterestPaidTransferRulePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TAInterestPaidTransferRule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tAInterestPaidTransferRule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAInterestPaidTransferRulePageUrlPattern);
      });

      it('edit button click should load edit TAInterestPaidTransferRule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TAInterestPaidTransferRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAInterestPaidTransferRulePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TAInterestPaidTransferRule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tAInterestPaidTransferRule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tAInterestPaidTransferRulePageUrlPattern);

        tAInterestPaidTransferRule = undefined;
      });
    });
  });

  describe('new TAInterestPaidTransferRule page', () => {
    beforeEach(() => {
      cy.visit(`${tAInterestPaidTransferRulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TAInterestPaidTransferRule');
    });

    it.skip('should create an instance of TAInterestPaidTransferRule', () => {
      cy.get(`[data-cy="name"]`).type('Sleek Steel').should('have.value', 'Sleek Steel');

      cy.get(`[data-cy="identifier"]`)
        .type('2b683b14-819b-4f90-9d02-15b5990c0d19')
        .invoke('val')
        .should('match', new RegExp('2b683b14-819b-4f90-9d02-15b5990c0d19'));

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="debit"]`).select(1);
      cy.get(`[data-cy="credit"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tAInterestPaidTransferRule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tAInterestPaidTransferRulePageUrlPattern);
    });
  });
});
