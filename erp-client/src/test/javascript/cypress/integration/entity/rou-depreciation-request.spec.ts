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

describe('RouDepreciationRequest e2e test', () => {
  const rouDepreciationRequestPageUrl = '/rou-depreciation-request';
  const rouDepreciationRequestPageUrlPattern = new RegExp('/rou-depreciation-request(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouDepreciationRequestSample = { requisitionId: '18c05eab-f95e-4b33-a084-d31a1b77ea6a' };

  let rouDepreciationRequest: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-depreciation-requests+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-depreciation-requests').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-depreciation-requests/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rouDepreciationRequest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-depreciation-requests/${rouDepreciationRequest.id}`,
      }).then(() => {
        rouDepreciationRequest = undefined;
      });
    }
  });

  it('RouDepreciationRequests menu should load RouDepreciationRequests page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-depreciation-request');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouDepreciationRequest').should('exist');
    cy.url().should('match', rouDepreciationRequestPageUrlPattern);
  });

  describe('RouDepreciationRequest page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouDepreciationRequestPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouDepreciationRequest page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-depreciation-request/new$'));
        cy.getEntityCreateUpdateHeading('RouDepreciationRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationRequestPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-depreciation-requests',
          body: rouDepreciationRequestSample,
        }).then(({ body }) => {
          rouDepreciationRequest = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-depreciation-requests+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouDepreciationRequest],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouDepreciationRequestPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RouDepreciationRequest page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouDepreciationRequest');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationRequestPageUrlPattern);
      });

      it('edit button click should load edit RouDepreciationRequest page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouDepreciationRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationRequestPageUrlPattern);
      });

      it('last delete button click should delete instance of RouDepreciationRequest', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouDepreciationRequest').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationRequestPageUrlPattern);

        rouDepreciationRequest = undefined;
      });
    });
  });

  describe('new RouDepreciationRequest page', () => {
    beforeEach(() => {
      cy.visit(`${rouDepreciationRequestPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouDepreciationRequest');
    });

    it('should create an instance of RouDepreciationRequest', () => {
      cy.get(`[data-cy="requisitionId"]`)
        .type('34808041-43ee-449a-9e16-50a8c7d31abc')
        .invoke('val')
        .should('match', new RegExp('34808041-43ee-449a-9e16-50a8c7d31abc'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-03-13T08:02').should('have.value', '2024-03-13T08:02');

      cy.get(`[data-cy="depreciationProcessStatus"]`).select('ERRORED');

      cy.get(`[data-cy="numberOfEnumeratedItems"]`).type('4062').should('have.value', '4062');

      cy.get(`[data-cy="invalidated"]`).should('not.be.checked');
      cy.get(`[data-cy="invalidated"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouDepreciationRequest = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouDepreciationRequestPageUrlPattern);
    });
  });
});
