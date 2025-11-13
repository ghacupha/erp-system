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

describe('CardTypes e2e test', () => {
  const cardTypesPageUrl = '/card-types';
  const cardTypesPageUrlPattern = new RegExp('/card-types(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardTypesSample = { cardTypeCode: 'metrics port', cardType: 'Massachusetts Brazil Arkansas' };

  let cardTypes: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-types/${cardTypes.id}`,
      }).then(() => {
        cardTypes = undefined;
      });
    }
  });

  it('CardTypes menu should load CardTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-types');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardTypes').should('exist');
    cy.url().should('match', cardTypesPageUrlPattern);
  });

  describe('CardTypes page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardTypesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardTypes page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-types/new$'));
        cy.getEntityCreateUpdateHeading('CardTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardTypesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-types',
          body: cardTypesSample,
        }).then(({ body }) => {
          cardTypes = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardTypes],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardTypesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardTypes page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardTypes');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardTypesPageUrlPattern);
      });

      it('edit button click should load edit CardTypes page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardTypesPageUrlPattern);
      });

      it('last delete button click should delete instance of CardTypes', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardTypes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardTypesPageUrlPattern);

        cardTypes = undefined;
      });
    });
  });

  describe('new CardTypes page', () => {
    beforeEach(() => {
      cy.visit(`${cardTypesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardTypes');
    });

    it('should create an instance of CardTypes', () => {
      cy.get(`[data-cy="cardTypeCode"]`).type('invoice').should('have.value', 'invoice');

      cy.get(`[data-cy="cardType"]`).type('Washington').should('have.value', 'Washington');

      cy.get(`[data-cy="cardTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardTypes = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardTypesPageUrlPattern);
    });
  });
});
