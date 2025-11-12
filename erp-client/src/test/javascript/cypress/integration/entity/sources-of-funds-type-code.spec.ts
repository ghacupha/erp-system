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

describe('SourcesOfFundsTypeCode e2e test', () => {
  const sourcesOfFundsTypeCodePageUrl = '/sources-of-funds-type-code';
  const sourcesOfFundsTypeCodePageUrlPattern = new RegExp('/sources-of-funds-type-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const sourcesOfFundsTypeCodeSample = { sourceOfFundsTypeCode: 'Specialist', sourceOfFundsType: 'efficient maximize' };

  let sourcesOfFundsTypeCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sources-of-funds-type-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sources-of-funds-type-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sources-of-funds-type-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sourcesOfFundsTypeCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sources-of-funds-type-codes/${sourcesOfFundsTypeCode.id}`,
      }).then(() => {
        sourcesOfFundsTypeCode = undefined;
      });
    }
  });

  it('SourcesOfFundsTypeCodes menu should load SourcesOfFundsTypeCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sources-of-funds-type-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SourcesOfFundsTypeCode').should('exist');
    cy.url().should('match', sourcesOfFundsTypeCodePageUrlPattern);
  });

  describe('SourcesOfFundsTypeCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sourcesOfFundsTypeCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SourcesOfFundsTypeCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/sources-of-funds-type-code/new$'));
        cy.getEntityCreateUpdateHeading('SourcesOfFundsTypeCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesOfFundsTypeCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sources-of-funds-type-codes',
          body: sourcesOfFundsTypeCodeSample,
        }).then(({ body }) => {
          sourcesOfFundsTypeCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sources-of-funds-type-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [sourcesOfFundsTypeCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(sourcesOfFundsTypeCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SourcesOfFundsTypeCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sourcesOfFundsTypeCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesOfFundsTypeCodePageUrlPattern);
      });

      it('edit button click should load edit SourcesOfFundsTypeCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SourcesOfFundsTypeCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesOfFundsTypeCodePageUrlPattern);
      });

      it('last delete button click should delete instance of SourcesOfFundsTypeCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('sourcesOfFundsTypeCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesOfFundsTypeCodePageUrlPattern);

        sourcesOfFundsTypeCode = undefined;
      });
    });
  });

  describe('new SourcesOfFundsTypeCode page', () => {
    beforeEach(() => {
      cy.visit(`${sourcesOfFundsTypeCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SourcesOfFundsTypeCode');
    });

    it('should create an instance of SourcesOfFundsTypeCode', () => {
      cy.get(`[data-cy="sourceOfFundsTypeCode"]`).type('networks').should('have.value', 'networks');

      cy.get(`[data-cy="sourceOfFundsType"]`).type('ROI PNG').should('have.value', 'ROI PNG');

      cy.get(`[data-cy="sourceOfFundsTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        sourcesOfFundsTypeCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', sourcesOfFundsTypeCodePageUrlPattern);
    });
  });
});
