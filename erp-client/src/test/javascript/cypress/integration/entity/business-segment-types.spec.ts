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

describe('BusinessSegmentTypes e2e test', () => {
  const businessSegmentTypesPageUrl = '/business-segment-types';
  const businessSegmentTypesPageUrlPattern = new RegExp('/business-segment-types(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const businessSegmentTypesSample = {
    businessEconomicSegmentCode: 'auxiliary Account Card',
    businessEconomicSegment: 'Luxembourg e-services Rubber',
  };

  let businessSegmentTypes: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/business-segment-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/business-segment-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/business-segment-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (businessSegmentTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/business-segment-types/${businessSegmentTypes.id}`,
      }).then(() => {
        businessSegmentTypes = undefined;
      });
    }
  });

  it('BusinessSegmentTypes menu should load BusinessSegmentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('business-segment-types');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BusinessSegmentTypes').should('exist');
    cy.url().should('match', businessSegmentTypesPageUrlPattern);
  });

  describe('BusinessSegmentTypes page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(businessSegmentTypesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BusinessSegmentTypes page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/business-segment-types/new$'));
        cy.getEntityCreateUpdateHeading('BusinessSegmentTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessSegmentTypesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/business-segment-types',
          body: businessSegmentTypesSample,
        }).then(({ body }) => {
          businessSegmentTypes = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/business-segment-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [businessSegmentTypes],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(businessSegmentTypesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BusinessSegmentTypes page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('businessSegmentTypes');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessSegmentTypesPageUrlPattern);
      });

      it('edit button click should load edit BusinessSegmentTypes page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BusinessSegmentTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessSegmentTypesPageUrlPattern);
      });

      it('last delete button click should delete instance of BusinessSegmentTypes', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('businessSegmentTypes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessSegmentTypesPageUrlPattern);

        businessSegmentTypes = undefined;
      });
    });
  });

  describe('new BusinessSegmentTypes page', () => {
    beforeEach(() => {
      cy.visit(`${businessSegmentTypesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('BusinessSegmentTypes');
    });

    it('should create an instance of BusinessSegmentTypes', () => {
      cy.get(`[data-cy="businessEconomicSegmentCode"]`)
        .type('Program Mississippi navigate')
        .should('have.value', 'Program Mississippi navigate');

      cy.get(`[data-cy="businessEconomicSegment"]`).type('recontextualize hack').should('have.value', 'recontextualize hack');

      cy.get(`[data-cy="details"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        businessSegmentTypes = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', businessSegmentTypesPageUrlPattern);
    });
  });
});
