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

describe('SnaSectorCode e2e test', () => {
  const snaSectorCodePageUrl = '/sna-sector-code';
  const snaSectorCodePageUrlPattern = new RegExp('/sna-sector-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const snaSectorCodeSample = { sectorTypeCode: 'Intranet hack' };

  let snaSectorCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sna-sector-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sna-sector-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sna-sector-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (snaSectorCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sna-sector-codes/${snaSectorCode.id}`,
      }).then(() => {
        snaSectorCode = undefined;
      });
    }
  });

  it('SnaSectorCodes menu should load SnaSectorCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sna-sector-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SnaSectorCode').should('exist');
    cy.url().should('match', snaSectorCodePageUrlPattern);
  });

  describe('SnaSectorCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(snaSectorCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SnaSectorCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/sna-sector-code/new$'));
        cy.getEntityCreateUpdateHeading('SnaSectorCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', snaSectorCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sna-sector-codes',
          body: snaSectorCodeSample,
        }).then(({ body }) => {
          snaSectorCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sna-sector-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [snaSectorCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(snaSectorCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SnaSectorCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('snaSectorCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', snaSectorCodePageUrlPattern);
      });

      it('edit button click should load edit SnaSectorCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SnaSectorCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', snaSectorCodePageUrlPattern);
      });

      it('last delete button click should delete instance of SnaSectorCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('snaSectorCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', snaSectorCodePageUrlPattern);

        snaSectorCode = undefined;
      });
    });
  });

  describe('new SnaSectorCode page', () => {
    beforeEach(() => {
      cy.visit(`${snaSectorCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SnaSectorCode');
    });

    it('should create an instance of SnaSectorCode', () => {
      cy.get(`[data-cy="sectorTypeCode"]`).type('Outdoors').should('have.value', 'Outdoors');

      cy.get(`[data-cy="mainSectorCode"]`).type('strategize connecting Dynamic').should('have.value', 'strategize connecting Dynamic');

      cy.get(`[data-cy="mainSectorTypeName"]`).type('e-business').should('have.value', 'e-business');

      cy.get(`[data-cy="subSectorCode"]`).type('matrix USB').should('have.value', 'matrix USB');

      cy.get(`[data-cy="subSectorName"]`).type('wireless').should('have.value', 'wireless');

      cy.get(`[data-cy="subSubSectorCode"]`).type('Plastic dot-com Automotive').should('have.value', 'Plastic dot-com Automotive');

      cy.get(`[data-cy="subSubSectorName"]`).type('copy Rubber').should('have.value', 'copy Rubber');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        snaSectorCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', snaSectorCodePageUrlPattern);
    });
  });
});
