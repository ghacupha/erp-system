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

describe('AccountAttribute e2e test', () => {
  const accountAttributePageUrl = '/account-attribute';
  const accountAttributePageUrlPattern = new RegExp('/account-attribute(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const accountAttributeSample = {
    reportingDate: '2023-10-03',
    customerNumber: 'Cliffs',
    accountContractNumber: "People's Kiribati",
    accountName: 'Credit Card Account',
    debitInterestRate: 38441,
    creditInterestRate: 64692,
    sanctionedAccountLimitFcy: 15978,
    sanctionedAccountLimitLcy: 812,
  };

  let accountAttribute: any;
  let institutionCode: any;
  let bankBranchCode: any;
  let accountOwnershipType: any;

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
        institutionCode: 'actuating',
        institutionName: 'Toys',
        shortName: 'Arkansas Intelligent Orchard',
        category: 'transition',
        institutionCategory: 'engage',
        institutionOwnership: 'array',
        dateLicensed: '2022-04-05',
        institutionStatus: 'drive',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'Directives Neck',
        bankName: 'transmit',
        branchCode: 'productize models Street',
        branchName: 'transmit Nepal Table',
        notes: 'Tuna Regional',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/account-ownership-types',
      body: {
        accountOwnershipTypeCode: 'Wooden Analyst',
        accountOwnershipType: 'connecting Nevada impactful',
        description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      accountOwnershipType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/account-attributes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/account-attributes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/account-attributes/*').as('deleteEntityRequest');
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

    cy.intercept('GET', '/api/account-ownership-types', {
      statusCode: 200,
      body: [accountOwnershipType],
    });
  });

  afterEach(() => {
    if (accountAttribute) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-attributes/${accountAttribute.id}`,
      }).then(() => {
        accountAttribute = undefined;
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
    if (accountOwnershipType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-ownership-types/${accountOwnershipType.id}`,
      }).then(() => {
        accountOwnershipType = undefined;
      });
    }
  });

  it('AccountAttributes menu should load AccountAttributes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('account-attribute');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountAttribute').should('exist');
    cy.url().should('match', accountAttributePageUrlPattern);
  });

  describe('AccountAttribute page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountAttributePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountAttribute page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/account-attribute/new$'));
        cy.getEntityCreateUpdateHeading('AccountAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/account-attributes',

          body: {
            ...accountAttributeSample,
            bankCode: institutionCode,
            branchCode: bankBranchCode,
            accountOwnershipType: accountOwnershipType,
          },
        }).then(({ body }) => {
          accountAttribute = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/account-attributes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [accountAttribute],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountAttributePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountAttribute page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountAttribute');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributePageUrlPattern);
      });

      it('edit button click should load edit AccountAttribute page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributePageUrlPattern);
      });

      it('last delete button click should delete instance of AccountAttribute', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountAttribute').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributePageUrlPattern);

        accountAttribute = undefined;
      });
    });
  });

  describe('new AccountAttribute page', () => {
    beforeEach(() => {
      cy.visit(`${accountAttributePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AccountAttribute');
    });

    it('should create an instance of AccountAttribute', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-02').should('have.value', '2023-10-02');

      cy.get(`[data-cy="customerNumber"]`).type('Alaska payment').should('have.value', 'Alaska payment');

      cy.get(`[data-cy="accountContractNumber"]`).type('wireless RAM microchip').should('have.value', 'wireless RAM microchip');

      cy.get(`[data-cy="accountName"]`).type('Personal Loan Account').should('have.value', 'Personal Loan Account');

      cy.get(`[data-cy="accountOpeningDate"]`).type('2023-10-02').should('have.value', '2023-10-02');

      cy.get(`[data-cy="accountClosingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="debitInterestRate"]`).type('12201').should('have.value', '12201');

      cy.get(`[data-cy="creditInterestRate"]`).type('97124').should('have.value', '97124');

      cy.get(`[data-cy="sanctionedAccountLimitFcy"]`).type('67698').should('have.value', '67698');

      cy.get(`[data-cy="sanctionedAccountLimitLcy"]`).type('13972').should('have.value', '13972');

      cy.get(`[data-cy="accountStatusChangeDate"]`).type('2023-10-02').should('have.value', '2023-10-02');

      cy.get(`[data-cy="expiryDate"]`).type('2023-10-02').should('have.value', '2023-10-02');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="branchCode"]`).select(1);
      cy.get(`[data-cy="accountOwnershipType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        accountAttribute = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', accountAttributePageUrlPattern);
    });
  });
});
