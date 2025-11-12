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

describe('TransactionAccount e2e test', () => {
  const transactionAccountPageUrl = '/transaction-account';
  const transactionAccountPageUrlPattern = new RegExp('/transaction-account(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const transactionAccountSample = {
    accountNumber: 'Forward',
    accountName: 'Home Loan Account',
    accountType: 'EQUITY',
    accountSubType: 'ACCOUNT_RECEIVABLE',
  };

  let transactionAccount: any;
  //let transactionAccountLedger: any;
  //let transactionAccountCategory: any;
  //let serviceOutlet: any;
  //let settlementCurrency: any;
  //let reportingEntity: any;

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
      url: '/api/transaction-account-ledgers',
      body: {"ledgerCode":"& primary","ledgerName":"Village"},
    }).then(({ body }) => {
      transactionAccountLedger = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-account-categories',
      body: {"name":"Human","transactionAccountPostingType":"DEBIT"},
    }).then(({ body }) => {
      transactionAccountCategory = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/service-outlets',
      body: {"outletCode":"Outdoors","outletName":"Bedfordshire","town":"invoice compressing Car","parliamentaryConstituency":"Gibraltar","gpsCoordinates":"Automotive haptic","outletOpeningDate":"2022-02-28","regulatorApprovalDate":"2022-03-01","outletClosureDate":"2022-02-28","dateLastModified":"2022-02-28","licenseFeePayable":7824},
    }).then(({ body }) => {
      serviceOutlet = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/settlement-currencies',
      body: {"iso4217CurrencyCode":"ROI","currencyName":"Iranian Rial","country":"Nicaragua","numericCode":"National","minorUnit":"protocol JSON Senior","fileUploadToken":"red","compilationToken":"Forward"},
    }).then(({ body }) => {
      settlementCurrency = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/reporting-entities',
      body: {"entityName":"bypassing"},
    }).then(({ body }) => {
      reportingEntity = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/transaction-accounts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-accounts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-accounts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/transaction-account-ledgers', {
      statusCode: 200,
      body: [transactionAccountLedger],
    });

    cy.intercept('GET', '/api/transaction-account-categories', {
      statusCode: 200,
      body: [transactionAccountCategory],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/service-outlets', {
      statusCode: 200,
      body: [serviceOutlet],
    });

    cy.intercept('GET', '/api/settlement-currencies', {
      statusCode: 200,
      body: [settlementCurrency],
    });

    cy.intercept('GET', '/api/reporting-entities', {
      statusCode: 200,
      body: [reportingEntity],
    });

  });
   */

  afterEach(() => {
    if (transactionAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-accounts/${transactionAccount.id}`,
      }).then(() => {
        transactionAccount = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (transactionAccountLedger) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-account-ledgers/${transactionAccountLedger.id}`,
      }).then(() => {
        transactionAccountLedger = undefined;
      });
    }
    if (transactionAccountCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-account-categories/${transactionAccountCategory.id}`,
      }).then(() => {
        transactionAccountCategory = undefined;
      });
    }
    if (serviceOutlet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-outlets/${serviceOutlet.id}`,
      }).then(() => {
        serviceOutlet = undefined;
      });
    }
    if (settlementCurrency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlement-currencies/${settlementCurrency.id}`,
      }).then(() => {
        settlementCurrency = undefined;
      });
    }
    if (reportingEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/reporting-entities/${reportingEntity.id}`,
      }).then(() => {
        reportingEntity = undefined;
      });
    }
  });
   */

  it('TransactionAccounts menu should load TransactionAccounts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-account');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionAccount').should('exist');
    cy.url().should('match', transactionAccountPageUrlPattern);
  });

  describe('TransactionAccount page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionAccountPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionAccount page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/transaction-account/new$'));
        cy.getEntityCreateUpdateHeading('TransactionAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transaction-accounts',
  
          body: {
            ...transactionAccountSample,
            accountLedger: transactionAccountLedger,
            accountCategory: transactionAccountCategory,
            serviceOutlet: serviceOutlet,
            settlementCurrency: settlementCurrency,
            institution: reportingEntity,
          },
        }).then(({ body }) => {
          transactionAccount = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transaction-accounts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [transactionAccount],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionAccountPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(transactionAccountPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TransactionAccount page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionAccount');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);
      });

      it('edit button click should load edit TransactionAccount page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TransactionAccount', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionAccount').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);

        transactionAccount = undefined;
      });
    });
  });

  describe('new TransactionAccount page', () => {
    beforeEach(() => {
      cy.visit(`${transactionAccountPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TransactionAccount');
    });

    it.skip('should create an instance of TransactionAccount', () => {
      cy.get(`[data-cy="accountNumber"]`).type('azure Communications withdrawal').should('have.value', 'azure Communications withdrawal');

      cy.get(`[data-cy="accountName"]`).type('Savings Account').should('have.value', 'Savings Account');

      cy.setFieldImageAsBytesOfEntity('notes', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="accountType"]`).select('ASSET');

      cy.get(`[data-cy="accountSubType"]`).select('OTHER_SHORT_LIVED_ASSET');

      cy.get(`[data-cy="dummyAccount"]`).should('not.be.checked');
      cy.get(`[data-cy="dummyAccount"]`).click().should('be.checked');

      cy.get(`[data-cy="accountLedger"]`).select(1);
      cy.get(`[data-cy="accountCategory"]`).select(1);
      cy.get(`[data-cy="serviceOutlet"]`).select(1);
      cy.get(`[data-cy="settlementCurrency"]`).select(1);
      cy.get(`[data-cy="institution"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        transactionAccount = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', transactionAccountPageUrlPattern);
    });
  });
});
