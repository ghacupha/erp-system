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

describe('CardCategoryType e2e test', () => {
  const cardCategoryTypePageUrl = '/card-category-type';
  const cardCategoryTypePageUrlPattern = new RegExp('/card-category-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardCategoryTypeSample = { cardCategoryFlag: 'I', cardCategoryDescription: 'infrastructures Health Advanced' };

  let cardCategoryType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-category-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-category-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-category-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardCategoryType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-category-types/${cardCategoryType.id}`,
      }).then(() => {
        cardCategoryType = undefined;
      });
    }
  });

  it('CardCategoryTypes menu should load CardCategoryTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-category-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardCategoryType').should('exist');
    cy.url().should('match', cardCategoryTypePageUrlPattern);
  });

  describe('CardCategoryType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardCategoryTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardCategoryType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-category-type/new$'));
        cy.getEntityCreateUpdateHeading('CardCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardCategoryTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-category-types',
          body: cardCategoryTypeSample,
        }).then(({ body }) => {
          cardCategoryType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-category-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardCategoryType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardCategoryTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardCategoryType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardCategoryType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardCategoryTypePageUrlPattern);
      });

      it('edit button click should load edit CardCategoryType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardCategoryType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardCategoryTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CardCategoryType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardCategoryType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardCategoryTypePageUrlPattern);

        cardCategoryType = undefined;
      });
    });
  });

  describe('new CardCategoryType page', () => {
    beforeEach(() => {
      cy.visit(`${cardCategoryTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardCategoryType');
    });

    it('should create an instance of CardCategoryType', () => {
      cy.get(`[data-cy="cardCategoryFlag"]`).select('L');

      cy.get(`[data-cy="cardCategoryDescription"]`).type('seize').should('have.value', 'seize');

      cy.get(`[data-cy="cardCategoryDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardCategoryType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardCategoryTypePageUrlPattern);
    });
  });
});
