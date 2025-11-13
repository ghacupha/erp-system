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

describe('AccountType e2e test', () => {
  const accountTypePageUrl = '/account-type';
  const accountTypePageUrlPattern = new RegExp('/account-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const accountTypeSample = { accountTypeCode: 'innovative Chair' };

  let accountType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/account-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/account-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/account-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-types/${accountType.id}`,
      }).then(() => {
        accountType = undefined;
      });
    }
  });

  it('AccountTypes menu should load AccountTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('account-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountType').should('exist');
    cy.url().should('match', accountTypePageUrlPattern);
  });

  describe('AccountType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/account-type/new$'));
        cy.getEntityCreateUpdateHeading('AccountType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/account-types',
          body: accountTypeSample,
        }).then(({ body }) => {
          accountType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/account-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [accountType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTypePageUrlPattern);
      });

      it('edit button click should load edit AccountType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTypePageUrlPattern);
      });

      it('last delete button click should delete instance of AccountType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTypePageUrlPattern);

        accountType = undefined;
      });
    });
  });

  describe('new AccountType page', () => {
    beforeEach(() => {
      cy.visit(`${accountTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AccountType');
    });

    it('should create an instance of AccountType', () => {
      cy.get(`[data-cy="accountTypeCode"]`).type('payment Persevering').should('have.value', 'payment Persevering');

      cy.get(`[data-cy="accountType"]`).type('Planner').should('have.value', 'Planner');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        accountType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', accountTypePageUrlPattern);
    });
  });
});
