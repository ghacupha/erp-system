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

describe('PrepaymentCompilationRequest e2e test', () => {
  const prepaymentCompilationRequestPageUrl = '/prepayment-compilation-request';
  const prepaymentCompilationRequestPageUrlPattern = new RegExp('/prepayment-compilation-request(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const prepaymentCompilationRequestSample = { compilationToken: 'a7fda709-e1ab-4867-9703-67529bf0de6c' };

  let prepaymentCompilationRequest: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/prepayment-compilation-requests+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/prepayment-compilation-requests').as('postEntityRequest');
    cy.intercept('DELETE', '/api/prepayment-compilation-requests/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (prepaymentCompilationRequest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-compilation-requests/${prepaymentCompilationRequest.id}`,
      }).then(() => {
        prepaymentCompilationRequest = undefined;
      });
    }
  });

  it('PrepaymentCompilationRequests menu should load PrepaymentCompilationRequests page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('prepayment-compilation-request');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrepaymentCompilationRequest').should('exist');
    cy.url().should('match', prepaymentCompilationRequestPageUrlPattern);
  });

  describe('PrepaymentCompilationRequest page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(prepaymentCompilationRequestPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PrepaymentCompilationRequest page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/prepayment-compilation-request/new$'));
        cy.getEntityCreateUpdateHeading('PrepaymentCompilationRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentCompilationRequestPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/prepayment-compilation-requests',
          body: prepaymentCompilationRequestSample,
        }).then(({ body }) => {
          prepaymentCompilationRequest = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/prepayment-compilation-requests+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [prepaymentCompilationRequest],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(prepaymentCompilationRequestPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PrepaymentCompilationRequest page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('prepaymentCompilationRequest');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentCompilationRequestPageUrlPattern);
      });

      it('edit button click should load edit PrepaymentCompilationRequest page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PrepaymentCompilationRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentCompilationRequestPageUrlPattern);
      });

      it('last delete button click should delete instance of PrepaymentCompilationRequest', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('prepaymentCompilationRequest').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentCompilationRequestPageUrlPattern);

        prepaymentCompilationRequest = undefined;
      });
    });
  });

  describe('new PrepaymentCompilationRequest page', () => {
    beforeEach(() => {
      cy.visit(`${prepaymentCompilationRequestPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PrepaymentCompilationRequest');
    });

    it('should create an instance of PrepaymentCompilationRequest', () => {
      cy.get(`[data-cy="timeOfRequest"]`).type('2023-11-21T07:51').should('have.value', '2023-11-21T07:51');

      cy.get(`[data-cy="compilationStatus"]`).select('COMPLETE');

      cy.get(`[data-cy="itemsProcessed"]`).type('59199').should('have.value', '59199');

      cy.get(`[data-cy="compilationToken"]`)
        .type('e101e6fd-4278-4b89-b653-d1ac282568c7')
        .invoke('val')
        .should('match', new RegExp('e101e6fd-4278-4b89-b653-d1ac282568c7'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        prepaymentCompilationRequest = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', prepaymentCompilationRequestPageUrlPattern);
    });
  });
});
