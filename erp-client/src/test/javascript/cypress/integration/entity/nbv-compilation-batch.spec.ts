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

describe('NbvCompilationBatch e2e test', () => {
  const nbvCompilationBatchPageUrl = '/nbv-compilation-batch';
  const nbvCompilationBatchPageUrlPattern = new RegExp('/nbv-compilation-batch(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const nbvCompilationBatchSample = {};

  let nbvCompilationBatch: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/nbv-compilation-batches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/nbv-compilation-batches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/nbv-compilation-batches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (nbvCompilationBatch) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/nbv-compilation-batches/${nbvCompilationBatch.id}`,
      }).then(() => {
        nbvCompilationBatch = undefined;
      });
    }
  });

  it('NbvCompilationBatches menu should load NbvCompilationBatches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('nbv-compilation-batch');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NbvCompilationBatch').should('exist');
    cy.url().should('match', nbvCompilationBatchPageUrlPattern);
  });

  describe('NbvCompilationBatch page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(nbvCompilationBatchPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NbvCompilationBatch page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/nbv-compilation-batch/new$'));
        cy.getEntityCreateUpdateHeading('NbvCompilationBatch');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationBatchPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/nbv-compilation-batches',
          body: nbvCompilationBatchSample,
        }).then(({ body }) => {
          nbvCompilationBatch = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/nbv-compilation-batches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [nbvCompilationBatch],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(nbvCompilationBatchPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NbvCompilationBatch page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('nbvCompilationBatch');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationBatchPageUrlPattern);
      });

      it('edit button click should load edit NbvCompilationBatch page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NbvCompilationBatch');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationBatchPageUrlPattern);
      });

      it('last delete button click should delete instance of NbvCompilationBatch', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('nbvCompilationBatch').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationBatchPageUrlPattern);

        nbvCompilationBatch = undefined;
      });
    });
  });

  describe('new NbvCompilationBatch page', () => {
    beforeEach(() => {
      cy.visit(`${nbvCompilationBatchPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('NbvCompilationBatch');
    });

    it('should create an instance of NbvCompilationBatch', () => {
      cy.get(`[data-cy="startIndex"]`).type('39823').should('have.value', '39823');

      cy.get(`[data-cy="endIndex"]`).type('98134').should('have.value', '98134');

      cy.get(`[data-cy="compilationBatchStatus"]`).select('COMPLETED');

      cy.get(`[data-cy="compilationBatchIdentifier"]`)
        .type('5daf4c46-28f9-476e-af41-023cf9494033')
        .invoke('val')
        .should('match', new RegExp('5daf4c46-28f9-476e-af41-023cf9494033'));

      cy.get(`[data-cy="compilationJobidentifier"]`)
        .type('805a1c92-79f2-44d6-94f9-bb86131e2ae8')
        .invoke('val')
        .should('match', new RegExp('805a1c92-79f2-44d6-94f9-bb86131e2ae8'));

      cy.get(`[data-cy="depreciationPeriodIdentifier"]`)
        .type('da8e4271-c810-420c-be50-75bea9092220')
        .invoke('val')
        .should('match', new RegExp('da8e4271-c810-420c-be50-75bea9092220'));

      cy.get(`[data-cy="fiscalMonthIdentifier"]`)
        .type('defde5f7-eb94-4d0f-a860-7b9f16893d55')
        .invoke('val')
        .should('match', new RegExp('defde5f7-eb94-4d0f-a860-7b9f16893d55'));

      cy.get(`[data-cy="batchSize"]`).type('14494').should('have.value', '14494');

      cy.get(`[data-cy="processedItems"]`).type('14719').should('have.value', '14719');

      cy.get(`[data-cy="sequenceNumber"]`).type('17694').should('have.value', '17694');

      cy.get(`[data-cy="isLastBatch"]`).should('not.be.checked');
      cy.get(`[data-cy="isLastBatch"]`).click().should('be.checked');

      cy.get(`[data-cy="processingTime"]`).type('PT23M').should('have.value', 'PT23M');

      cy.get(`[data-cy="totalItems"]`).type('67914').should('have.value', '67914');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        nbvCompilationBatch = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', nbvCompilationBatchPageUrlPattern);
    });
  });
});
