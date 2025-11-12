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

describe('AccountBalance e2e test', () => {
  const accountBalancePageUrl = '/account-balance';
  const accountBalancePageUrlPattern = new RegExp('/account-balance(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const accountBalanceSample = {
    reportingDate: '2023-10-04',
    customerId: 'virtual',
    accountContractNumber: '741579906148525',
    accruedInterestBalanceFCY: 75083,
    accruedInterestBalanceLCY: 31014,
    accountBalanceFCY: 74040,
    accountBalanceLCY: 18994,
  };

  let accountBalance: any;
  let institutionCode: any;
  let bankBranchCode: any;
  let isoCurrencyCode: any;

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
        institutionCode: 'ivory',
        institutionName: 'protocol Directives Malaysia',
        shortName: 'brand',
        category: 'Track',
        institutionCategory: 'Chair',
        institutionOwnership: 'User-centric Cambridgeshire',
        dateLicensed: '2022-04-05',
        institutionStatus: 'deposit clicks-and-mortar',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'platforms Tools Agent',
        bankName: 'Credit Future',
        branchCode: 'Computer compress Loan',
        branchName: 'experiences',
        notes: 'interface Legacy',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/iso-currency-codes',
      body: {
        alphabeticCode: 'Bedfordshire',
        numericCode: 'Forks Bedfordshire',
        minorUnit: 'Cayman strategy',
        currency: 'Hawaii Borders',
        country: 'Malta',
      },
    }).then(({ body }) => {
      isoCurrencyCode = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/account-balances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/account-balances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/account-balances/*').as('deleteEntityRequest');
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

    cy.intercept('GET', '/api/iso-currency-codes', {
      statusCode: 200,
      body: [isoCurrencyCode],
    });
  });

  afterEach(() => {
    if (accountBalance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-balances/${accountBalance.id}`,
      }).then(() => {
        accountBalance = undefined;
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
    if (isoCurrencyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/iso-currency-codes/${isoCurrencyCode.id}`,
      }).then(() => {
        isoCurrencyCode = undefined;
      });
    }
  });

  it('AccountBalances menu should load AccountBalances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('account-balance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountBalance').should('exist');
    cy.url().should('match', accountBalancePageUrlPattern);
  });

  describe('AccountBalance page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountBalancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountBalance page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/account-balance/new$'));
        cy.getEntityCreateUpdateHeading('AccountBalance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountBalancePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/account-balances',

          body: {
            ...accountBalanceSample,
            bankCode: institutionCode,
            branchId: bankBranchCode,
            currencyCode: isoCurrencyCode,
          },
        }).then(({ body }) => {
          accountBalance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/account-balances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [accountBalance],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountBalancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountBalance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountBalance');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountBalancePageUrlPattern);
      });

      it('edit button click should load edit AccountBalance page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountBalance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountBalancePageUrlPattern);
      });

      it('last delete button click should delete instance of AccountBalance', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountBalance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountBalancePageUrlPattern);

        accountBalance = undefined;
      });
    });
  });

  describe('new AccountBalance page', () => {
    beforeEach(() => {
      cy.visit(`${accountBalancePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AccountBalance');
    });

    it('should create an instance of AccountBalance', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="customerId"]`).type('Gloves').should('have.value', 'Gloves');

      cy.get(`[data-cy="accountContractNumber"]`).type('001689301496966').should('have.value', '001689301496966');

      cy.get(`[data-cy="accruedInterestBalanceFCY"]`).type('41775').should('have.value', '41775');

      cy.get(`[data-cy="accruedInterestBalanceLCY"]`).type('5481').should('have.value', '5481');

      cy.get(`[data-cy="accountBalanceFCY"]`).type('70406').should('have.value', '70406');

      cy.get(`[data-cy="accountBalanceLCY"]`).type('91374').should('have.value', '91374');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="branchId"]`).select(1);
      cy.get(`[data-cy="currencyCode"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        accountBalance = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', accountBalancePageUrlPattern);
    });
  });
});
