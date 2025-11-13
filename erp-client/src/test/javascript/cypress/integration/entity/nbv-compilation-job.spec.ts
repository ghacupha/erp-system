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

describe('NbvCompilationJob e2e test', () => {
  const nbvCompilationJobPageUrl = '/nbv-compilation-job';
  const nbvCompilationJobPageUrlPattern = new RegExp('/nbv-compilation-job(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const nbvCompilationJobSample = {
    compilationJobIdentifier: '1d2c09bb-9cd4-43b0-8bea-4c26536b0319',
    jobTitle: 'Legacy Security Assistant',
  };

  let nbvCompilationJob: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/nbv-compilation-jobs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/nbv-compilation-jobs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/nbv-compilation-jobs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (nbvCompilationJob) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/nbv-compilation-jobs/${nbvCompilationJob.id}`,
      }).then(() => {
        nbvCompilationJob = undefined;
      });
    }
  });

  it('NbvCompilationJobs menu should load NbvCompilationJobs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('nbv-compilation-job');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NbvCompilationJob').should('exist');
    cy.url().should('match', nbvCompilationJobPageUrlPattern);
  });

  describe('NbvCompilationJob page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(nbvCompilationJobPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NbvCompilationJob page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/nbv-compilation-job/new$'));
        cy.getEntityCreateUpdateHeading('NbvCompilationJob');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationJobPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/nbv-compilation-jobs',
          body: nbvCompilationJobSample,
        }).then(({ body }) => {
          nbvCompilationJob = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/nbv-compilation-jobs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [nbvCompilationJob],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(nbvCompilationJobPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NbvCompilationJob page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('nbvCompilationJob');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationJobPageUrlPattern);
      });

      it('edit button click should load edit NbvCompilationJob page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NbvCompilationJob');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationJobPageUrlPattern);
      });

      it('last delete button click should delete instance of NbvCompilationJob', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('nbvCompilationJob').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', nbvCompilationJobPageUrlPattern);

        nbvCompilationJob = undefined;
      });
    });
  });

  describe('new NbvCompilationJob page', () => {
    beforeEach(() => {
      cy.visit(`${nbvCompilationJobPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('NbvCompilationJob');
    });

    it('should create an instance of NbvCompilationJob', () => {
      cy.get(`[data-cy="compilationJobIdentifier"]`)
        .type('d7748ec1-ce39-4524-9504-b08fcf97a8fd')
        .invoke('val')
        .should('match', new RegExp('d7748ec1-ce39-4524-9504-b08fcf97a8fd'));

      cy.get(`[data-cy="compilationJobTimeOfRequest"]`).type('2024-02-18T10:11').should('have.value', '2024-02-18T10:11');

      cy.get(`[data-cy="entitiesAffected"]`).type('34718').should('have.value', '34718');

      cy.get(`[data-cy="compilationStatus"]`).select('ENQUEUED');

      cy.get(`[data-cy="jobTitle"]`).type('Chief Interactions Manager').should('have.value', 'Chief Interactions Manager');

      cy.get(`[data-cy="numberOfBatches"]`).type('73912').should('have.value', '73912');

      cy.get(`[data-cy="numberOfProcessedBatches"]`).type('45318').should('have.value', '45318');

      cy.get(`[data-cy="processingTime"]`).type('PT48M').should('have.value', 'PT48M');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        nbvCompilationJob = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', nbvCompilationJobPageUrlPattern);
    });
  });
});
