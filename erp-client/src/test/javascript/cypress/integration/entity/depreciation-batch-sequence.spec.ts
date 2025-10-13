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

describe('DepreciationBatchSequence e2e test', () => {
  const depreciationBatchSequencePageUrl = '/depreciation-batch-sequence';
  const depreciationBatchSequencePageUrlPattern = new RegExp('/depreciation-batch-sequence(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const depreciationBatchSequenceSample = {};

  let depreciationBatchSequence: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/depreciation-batch-sequences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/depreciation-batch-sequences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/depreciation-batch-sequences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (depreciationBatchSequence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-batch-sequences/${depreciationBatchSequence.id}`,
      }).then(() => {
        depreciationBatchSequence = undefined;
      });
    }
  });

  it('DepreciationBatchSequences menu should load DepreciationBatchSequences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('depreciation-batch-sequence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepreciationBatchSequence').should('exist');
    cy.url().should('match', depreciationBatchSequencePageUrlPattern);
  });

  describe('DepreciationBatchSequence page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(depreciationBatchSequencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepreciationBatchSequence page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/depreciation-batch-sequence/new$'));
        cy.getEntityCreateUpdateHeading('DepreciationBatchSequence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationBatchSequencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/depreciation-batch-sequences',
          body: depreciationBatchSequenceSample,
        }).then(({ body }) => {
          depreciationBatchSequence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/depreciation-batch-sequences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [depreciationBatchSequence],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(depreciationBatchSequencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DepreciationBatchSequence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('depreciationBatchSequence');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationBatchSequencePageUrlPattern);
      });

      it('edit button click should load edit DepreciationBatchSequence page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepreciationBatchSequence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationBatchSequencePageUrlPattern);
      });

      it('last delete button click should delete instance of DepreciationBatchSequence', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('depreciationBatchSequence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationBatchSequencePageUrlPattern);

        depreciationBatchSequence = undefined;
      });
    });
  });

  describe('new DepreciationBatchSequence page', () => {
    beforeEach(() => {
      cy.visit(`${depreciationBatchSequencePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DepreciationBatchSequence');
    });

    it('should create an instance of DepreciationBatchSequence', () => {
      cy.get(`[data-cy="startIndex"]`).type('40047').should('have.value', '40047');

      cy.get(`[data-cy="endIndex"]`).type('85251').should('have.value', '85251');

      cy.get(`[data-cy="createdAt"]`).type('2023-07-04T16:14').should('have.value', '2023-07-04T16:14');

      cy.get(`[data-cy="depreciationBatchStatus"]`).select('ERRORED');

      cy.get(`[data-cy="batchSize"]`).type('15128').should('have.value', '15128');

      cy.get(`[data-cy="processedItems"]`).type('93266').should('have.value', '93266');

      cy.get(`[data-cy="sequenceNumber"]`).type('17670').should('have.value', '17670');

      cy.get(`[data-cy="isLastBatch"]`).should('not.be.checked');
      cy.get(`[data-cy="isLastBatch"]`).click().should('be.checked');

      cy.get(`[data-cy="processingTime"]`).type('PT57M').should('have.value', 'PT57M');

      cy.get(`[data-cy="totalItems"]`).type('38902').should('have.value', '38902');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        depreciationBatchSequence = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', depreciationBatchSequencePageUrlPattern);
    });
  });
});
