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

describe('CardFraudIncidentCategory e2e test', () => {
  const cardFraudIncidentCategoryPageUrl = '/card-fraud-incident-category';
  const cardFraudIncidentCategoryPageUrlPattern = new RegExp('/card-fraud-incident-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardFraudIncidentCategorySample = {
    cardFraudCategoryTypeCode: 'e-business navigate reboot',
    cardFraudCategoryType: 'relationships',
  };

  let cardFraudIncidentCategory: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-fraud-incident-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-fraud-incident-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-fraud-incident-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardFraudIncidentCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-fraud-incident-categories/${cardFraudIncidentCategory.id}`,
      }).then(() => {
        cardFraudIncidentCategory = undefined;
      });
    }
  });

  it('CardFraudIncidentCategories menu should load CardFraudIncidentCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-fraud-incident-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardFraudIncidentCategory').should('exist');
    cy.url().should('match', cardFraudIncidentCategoryPageUrlPattern);
  });

  describe('CardFraudIncidentCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardFraudIncidentCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardFraudIncidentCategory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-fraud-incident-category/new$'));
        cy.getEntityCreateUpdateHeading('CardFraudIncidentCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudIncidentCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-fraud-incident-categories',
          body: cardFraudIncidentCategorySample,
        }).then(({ body }) => {
          cardFraudIncidentCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-fraud-incident-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardFraudIncidentCategory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardFraudIncidentCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardFraudIncidentCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardFraudIncidentCategory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudIncidentCategoryPageUrlPattern);
      });

      it('edit button click should load edit CardFraudIncidentCategory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardFraudIncidentCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudIncidentCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of CardFraudIncidentCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardFraudIncidentCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudIncidentCategoryPageUrlPattern);

        cardFraudIncidentCategory = undefined;
      });
    });
  });

  describe('new CardFraudIncidentCategory page', () => {
    beforeEach(() => {
      cy.visit(`${cardFraudIncidentCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardFraudIncidentCategory');
    });

    it('should create an instance of CardFraudIncidentCategory', () => {
      cy.get(`[data-cy="cardFraudCategoryTypeCode"]`).type('ADP').should('have.value', 'ADP');

      cy.get(`[data-cy="cardFraudCategoryType"]`).type('Granite open-source reinvent').should('have.value', 'Granite open-source reinvent');

      cy.get(`[data-cy="cardFraudCategoryTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardFraudIncidentCategory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardFraudIncidentCategoryPageUrlPattern);
    });
  });
});
