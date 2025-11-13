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

describe('BankTransactionType e2e test', () => {
  const bankTransactionTypePageUrl = '/bank-transaction-type';
  const bankTransactionTypePageUrlPattern = new RegExp('/bank-transaction-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const bankTransactionTypeSample = { transactionTypeCode: 'SCSI', transactionTypeDetails: 'lime' };

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
    cy.intercept('GET', '/api/bank-transaction-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bank-transaction-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bank-transaction-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bankTransactionType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-transaction-types/${bankTransactionType.id}`,
      }).then(() => {
        bankTransactionType = undefined;
      });
    }
  });

  it('BankTransactionTypes menu should load BankTransactionTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bank-transaction-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BankTransactionType').should('exist');
    cy.url().should('match', bankTransactionTypePageUrlPattern);
  });

  describe('BankTransactionType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bankTransactionTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BankTransactionType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/bank-transaction-type/new$'));
        cy.getEntityCreateUpdateHeading('BankTransactionType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankTransactionTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bank-transaction-types',
          body: bankTransactionTypeSample,
        }).then(({ body }) => {
          bankTransactionType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bank-transaction-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [bankTransactionType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(bankTransactionTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BankTransactionType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bankTransactionType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankTransactionTypePageUrlPattern);
      });

      it('edit button click should load edit BankTransactionType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BankTransactionType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankTransactionTypePageUrlPattern);
      });

      it('last delete button click should delete instance of BankTransactionType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('bankTransactionType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankTransactionTypePageUrlPattern);

        bankTransactionType = undefined;
      });
    });
  });

  describe('new BankTransactionType page', () => {
    beforeEach(() => {
      cy.visit(`${bankTransactionTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('BankTransactionType');
    });

    it('should create an instance of BankTransactionType', () => {
      cy.get(`[data-cy="transactionTypeCode"]`).type('User-friendly View').should('have.value', 'User-friendly View');

      cy.get(`[data-cy="transactionTypeDetails"]`).type('services Dynamic calculate').should('have.value', 'services Dynamic calculate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        bankTransactionType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', bankTransactionTypePageUrlPattern);
    });
  });
});
