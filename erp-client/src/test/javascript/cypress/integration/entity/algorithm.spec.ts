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

describe('Algorithm e2e test', () => {
  const algorithmPageUrl = '/algorithm';
  const algorithmPageUrlPattern = new RegExp('/algorithm(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const algorithmSample = { name: 'Loan' };

  let algorithm: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/algorithms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/algorithms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/algorithms/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (algorithm) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/algorithms/${algorithm.id}`,
      }).then(() => {
        algorithm = undefined;
      });
    }
  });

  it('Algorithms menu should load Algorithms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('algorithm');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Algorithm').should('exist');
    cy.url().should('match', algorithmPageUrlPattern);
  });

  describe('Algorithm page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(algorithmPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Algorithm page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/algorithm/new$'));
        cy.getEntityCreateUpdateHeading('Algorithm');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', algorithmPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/algorithms',
          body: algorithmSample,
        }).then(({ body }) => {
          algorithm = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/algorithms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [algorithm],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(algorithmPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Algorithm page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('algorithm');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', algorithmPageUrlPattern);
      });

      it('edit button click should load edit Algorithm page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Algorithm');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', algorithmPageUrlPattern);
      });

      it('last delete button click should delete instance of Algorithm', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('algorithm').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', algorithmPageUrlPattern);

        algorithm = undefined;
      });
    });
  });

  describe('new Algorithm page', () => {
    beforeEach(() => {
      cy.visit(`${algorithmPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Algorithm');
    });

    it('should create an instance of Algorithm', () => {
      cy.get(`[data-cy="name"]`).type('encoding').should('have.value', 'encoding');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        algorithm = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', algorithmPageUrlPattern);
    });
  });
});
