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

describe('TACompilationRequest e2e test', () => {
  const tACompilationRequestPageUrl = '/ta-compilation-request';
  const tACompilationRequestPageUrlPattern = new RegExp('/ta-compilation-request(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tACompilationRequestSample = {
    requisitionId: 'e24d367f-65f5-4802-9b60-103721b935c0',
    batchJobIdentifier: '025453ac-40b8-4c24-877c-cd80881507dc',
  };

  let tACompilationRequest: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ta-compilation-requests+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ta-compilation-requests').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ta-compilation-requests/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tACompilationRequest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ta-compilation-requests/${tACompilationRequest.id}`,
      }).then(() => {
        tACompilationRequest = undefined;
      });
    }
  });

  it('TACompilationRequests menu should load TACompilationRequests page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ta-compilation-request');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TACompilationRequest').should('exist');
    cy.url().should('match', tACompilationRequestPageUrlPattern);
  });

  describe('TACompilationRequest page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tACompilationRequestPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TACompilationRequest page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ta-compilation-request/new$'));
        cy.getEntityCreateUpdateHeading('TACompilationRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tACompilationRequestPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ta-compilation-requests',
          body: tACompilationRequestSample,
        }).then(({ body }) => {
          tACompilationRequest = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ta-compilation-requests+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tACompilationRequest],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tACompilationRequestPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TACompilationRequest page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tACompilationRequest');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tACompilationRequestPageUrlPattern);
      });

      it('edit button click should load edit TACompilationRequest page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TACompilationRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tACompilationRequestPageUrlPattern);
      });

      it('last delete button click should delete instance of TACompilationRequest', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tACompilationRequest').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tACompilationRequestPageUrlPattern);

        tACompilationRequest = undefined;
      });
    });
  });

  describe('new TACompilationRequest page', () => {
    beforeEach(() => {
      cy.visit(`${tACompilationRequestPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TACompilationRequest');
    });

    it('should create an instance of TACompilationRequest', () => {
      cy.get(`[data-cy="requisitionId"]`)
        .type('0c00a183-48e3-478e-806a-030398d86843')
        .invoke('val')
        .should('match', new RegExp('0c00a183-48e3-478e-806a-030398d86843'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-10-14T12:04').should('have.value', '2024-10-14T12:04');

      cy.get(`[data-cy="compilationProcessStatus"]`).select('COMPLETE');

      cy.get(`[data-cy="numberOfEnumeratedItems"]`).type('36605').should('have.value', '36605');

      cy.get(`[data-cy="batchJobIdentifier"]`)
        .type('df04dbf7-a162-469a-949a-576ae96c230e')
        .invoke('val')
        .should('match', new RegExp('df04dbf7-a162-469a-949a-576ae96c230e'));

      cy.get(`[data-cy="compilationTime"]`).type('2024-10-14T13:50').should('have.value', '2024-10-14T13:50');

      cy.get(`[data-cy="invalidated"]`).should('not.be.checked');
      cy.get(`[data-cy="invalidated"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tACompilationRequest = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tACompilationRequestPageUrlPattern);
    });
  });
});
