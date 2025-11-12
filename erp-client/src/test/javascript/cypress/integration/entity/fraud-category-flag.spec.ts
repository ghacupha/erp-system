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

describe('FraudCategoryFlag e2e test', () => {
  const fraudCategoryFlagPageUrl = '/fraud-category-flag';
  const fraudCategoryFlagPageUrlPattern = new RegExp('/fraud-category-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fraudCategoryFlagSample = { fraudCategoryFlag: 'Y' };

  let fraudCategoryFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fraud-category-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fraud-category-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fraud-category-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fraudCategoryFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fraud-category-flags/${fraudCategoryFlag.id}`,
      }).then(() => {
        fraudCategoryFlag = undefined;
      });
    }
  });

  it('FraudCategoryFlags menu should load FraudCategoryFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fraud-category-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FraudCategoryFlag').should('exist');
    cy.url().should('match', fraudCategoryFlagPageUrlPattern);
  });

  describe('FraudCategoryFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fraudCategoryFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FraudCategoryFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fraud-category-flag/new$'));
        cy.getEntityCreateUpdateHeading('FraudCategoryFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudCategoryFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fraud-category-flags',
          body: fraudCategoryFlagSample,
        }).then(({ body }) => {
          fraudCategoryFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fraud-category-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fraudCategoryFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fraudCategoryFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FraudCategoryFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fraudCategoryFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudCategoryFlagPageUrlPattern);
      });

      it('edit button click should load edit FraudCategoryFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FraudCategoryFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudCategoryFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of FraudCategoryFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fraudCategoryFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudCategoryFlagPageUrlPattern);

        fraudCategoryFlag = undefined;
      });
    });
  });

  describe('new FraudCategoryFlag page', () => {
    beforeEach(() => {
      cy.visit(`${fraudCategoryFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FraudCategoryFlag');
    });

    it('should create an instance of FraudCategoryFlag', () => {
      cy.get(`[data-cy="fraudCategoryFlag"]`).select('Y');

      cy.get(`[data-cy="fraudCategoryTypeDetails"]`)
        .type('wireless client-driven Mouse')
        .should('have.value', 'wireless client-driven Mouse');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fraudCategoryFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fraudCategoryFlagPageUrlPattern);
    });
  });
});
