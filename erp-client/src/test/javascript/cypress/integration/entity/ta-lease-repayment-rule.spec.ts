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

describe('TALeaseRepaymentRule e2e test', () => {
  const tALeaseRepaymentRulePageUrl = '/ta-lease-repayment-rule';
  const tALeaseRepaymentRulePageUrlPattern = new RegExp('/ta-lease-repayment-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tALeaseRepaymentRuleSample = { name: 'benchmark Account', identifier: 'f99fa949-2f18-468f-917d-dcb4ad3a508b' };

  let tALeaseRepaymentRule: any;
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
      body: {"bookingId":"Multi-layered Path","leaseTitle":"EXE Cape","shortTitle":"transmitting pixel","description":"cross-platform","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"c660713e-98fa-4860-9c19-c869e0b10d4b"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"Cotton Kentucky Small","accountName":"Checking Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"EQUITY","accountSubType":"ACCOUNT_PAYABLE","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ta-lease-repayment-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ta-lease-repayment-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ta-lease-repayment-rules/*').as('deleteEntityRequest');
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
    if (tALeaseRepaymentRule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ta-lease-repayment-rules/${tALeaseRepaymentRule.id}`,
      }).then(() => {
        tALeaseRepaymentRule = undefined;
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

  it('TALeaseRepaymentRules menu should load TALeaseRepaymentRules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ta-lease-repayment-rule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TALeaseRepaymentRule').should('exist');
    cy.url().should('match', tALeaseRepaymentRulePageUrlPattern);
  });

  describe('TALeaseRepaymentRule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tALeaseRepaymentRulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TALeaseRepaymentRule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ta-lease-repayment-rule/new$'));
        cy.getEntityCreateUpdateHeading('TALeaseRepaymentRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRepaymentRulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ta-lease-repayment-rules',
  
          body: {
            ...tALeaseRepaymentRuleSample,
            leaseContract: iFRS16LeaseContract,
            debit: transactionAccount,
            credit: transactionAccount,
          },
        }).then(({ body }) => {
          tALeaseRepaymentRule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ta-lease-repayment-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tALeaseRepaymentRule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tALeaseRepaymentRulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(tALeaseRepaymentRulePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TALeaseRepaymentRule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tALeaseRepaymentRule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRepaymentRulePageUrlPattern);
      });

      it('edit button click should load edit TALeaseRepaymentRule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TALeaseRepaymentRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRepaymentRulePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TALeaseRepaymentRule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tALeaseRepaymentRule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRepaymentRulePageUrlPattern);

        tALeaseRepaymentRule = undefined;
      });
    });
  });

  describe('new TALeaseRepaymentRule page', () => {
    beforeEach(() => {
      cy.visit(`${tALeaseRepaymentRulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TALeaseRepaymentRule');
    });

    it.skip('should create an instance of TALeaseRepaymentRule', () => {
      cy.get(`[data-cy="name"]`).type('Intuitive Engineer').should('have.value', 'Intuitive Engineer');

      cy.get(`[data-cy="identifier"]`)
        .type('433d9b03-122e-4fca-9d53-59549871e4a2')
        .invoke('val')
        .should('match', new RegExp('433d9b03-122e-4fca-9d53-59549871e4a2'));

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="debit"]`).select(1);
      cy.get(`[data-cy="credit"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tALeaseRepaymentRule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tALeaseRepaymentRulePageUrlPattern);
    });
  });
});
