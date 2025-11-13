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

describe('AccountStatusType e2e test', () => {
  const accountStatusTypePageUrl = '/account-status-type';
  const accountStatusTypePageUrlPattern = new RegExp('/account-status-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const accountStatusTypeSample = { accountStatusCode: 'initiatives Reunion Identity', accountStatusType: 'INACTIVE' };

  let accountStatusType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/account-status-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/account-status-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/account-status-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountStatusType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-status-types/${accountStatusType.id}`,
      }).then(() => {
        accountStatusType = undefined;
      });
    }
  });

  it('AccountStatusTypes menu should load AccountStatusTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('account-status-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountStatusType').should('exist');
    cy.url().should('match', accountStatusTypePageUrlPattern);
  });

  describe('AccountStatusType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountStatusTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountStatusType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/account-status-type/new$'));
        cy.getEntityCreateUpdateHeading('AccountStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountStatusTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/account-status-types',
          body: accountStatusTypeSample,
        }).then(({ body }) => {
          accountStatusType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/account-status-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [accountStatusType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountStatusTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountStatusType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountStatusType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountStatusTypePageUrlPattern);
      });

      it('edit button click should load edit AccountStatusType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountStatusTypePageUrlPattern);
      });

      it('last delete button click should delete instance of AccountStatusType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountStatusType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountStatusTypePageUrlPattern);

        accountStatusType = undefined;
      });
    });
  });

  describe('new AccountStatusType page', () => {
    beforeEach(() => {
      cy.visit(`${accountStatusTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AccountStatusType');
    });

    it('should create an instance of AccountStatusType', () => {
      cy.get(`[data-cy="accountStatusCode"]`).type('Baht').should('have.value', 'Baht');

      cy.get(`[data-cy="accountStatusType"]`).select('ACTIVE');

      cy.get(`[data-cy="accountStatusDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        accountStatusType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', accountStatusTypePageUrlPattern);
    });
  });
});
