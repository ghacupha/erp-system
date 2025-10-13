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

describe('DepreciationJob e2e test', () => {
  const depreciationJobPageUrl = '/depreciation-job';
  const depreciationJobPageUrlPattern = new RegExp('/depreciation-job(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const depreciationJobSample = {};

  let depreciationJob: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/depreciation-jobs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/depreciation-jobs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/depreciation-jobs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (depreciationJob) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-jobs/${depreciationJob.id}`,
      }).then(() => {
        depreciationJob = undefined;
      });
    }
  });

  it('DepreciationJobs menu should load DepreciationJobs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('depreciation-job');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepreciationJob').should('exist');
    cy.url().should('match', depreciationJobPageUrlPattern);
  });

  describe('DepreciationJob page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(depreciationJobPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepreciationJob page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/depreciation-job/new$'));
        cy.getEntityCreateUpdateHeading('DepreciationJob');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/depreciation-jobs',
          body: depreciationJobSample,
        }).then(({ body }) => {
          depreciationJob = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/depreciation-jobs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [depreciationJob],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(depreciationJobPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DepreciationJob page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('depreciationJob');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobPageUrlPattern);
      });

      it('edit button click should load edit DepreciationJob page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepreciationJob');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobPageUrlPattern);
      });

      it('last delete button click should delete instance of DepreciationJob', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('depreciationJob').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobPageUrlPattern);

        depreciationJob = undefined;
      });
    });
  });

  describe('new DepreciationJob page', () => {
    beforeEach(() => {
      cy.visit(`${depreciationJobPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DepreciationJob');
    });

    it('should create an instance of DepreciationJob', () => {
      cy.get(`[data-cy="timeOfCommencement"]`).type('2023-07-04T20:00').should('have.value', '2023-07-04T20:00');

      cy.get(`[data-cy="depreciationJobStatus"]`).select('ERRORED');

      cy.get(`[data-cy="description"]`).type('scalable revolutionize Shilling').should('have.value', 'scalable revolutionize Shilling');

      cy.get(`[data-cy="numberOfBatches"]`).type('82368').should('have.value', '82368');

      cy.get(`[data-cy="processedBatches"]`).type('67367').should('have.value', '67367');

      cy.get(`[data-cy="lastBatchSize"]`).type('37752').should('have.value', '37752');

      cy.get(`[data-cy="processedItems"]`).type('72778').should('have.value', '72778');

      cy.get(`[data-cy="processingTime"]`).type('PT25M').should('have.value', 'PT25M');

      cy.get(`[data-cy="totalItems"]`).type('81144').should('have.value', '81144');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        depreciationJob = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', depreciationJobPageUrlPattern);
    });
  });
});
