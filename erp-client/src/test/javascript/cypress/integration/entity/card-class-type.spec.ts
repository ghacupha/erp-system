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

describe('CardClassType e2e test', () => {
  const cardClassTypePageUrl = '/card-class-type';
  const cardClassTypePageUrlPattern = new RegExp('/card-class-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardClassTypeSample = { cardClassTypeCode: 'Branding killer circuit', cardClassType: 'Money Avon' };

  let cardClassType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-class-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-class-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-class-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardClassType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-class-types/${cardClassType.id}`,
      }).then(() => {
        cardClassType = undefined;
      });
    }
  });

  it('CardClassTypes menu should load CardClassTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-class-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardClassType').should('exist');
    cy.url().should('match', cardClassTypePageUrlPattern);
  });

  describe('CardClassType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardClassTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardClassType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-class-type/new$'));
        cy.getEntityCreateUpdateHeading('CardClassType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardClassTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-class-types',
          body: cardClassTypeSample,
        }).then(({ body }) => {
          cardClassType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-class-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardClassType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardClassTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardClassType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardClassType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardClassTypePageUrlPattern);
      });

      it('edit button click should load edit CardClassType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardClassType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardClassTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CardClassType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardClassType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardClassTypePageUrlPattern);

        cardClassType = undefined;
      });
    });
  });

  describe('new CardClassType page', () => {
    beforeEach(() => {
      cy.visit(`${cardClassTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardClassType');
    });

    it('should create an instance of CardClassType', () => {
      cy.get(`[data-cy="cardClassTypeCode"]`).type('Avon').should('have.value', 'Avon');

      cy.get(`[data-cy="cardClassType"]`).type('mission-critical Shirt').should('have.value', 'mission-critical Shirt');

      cy.get(`[data-cy="cardClassDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardClassType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardClassTypePageUrlPattern);
    });
  });
});
