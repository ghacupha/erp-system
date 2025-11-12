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

describe('CardBrandType e2e test', () => {
  const cardBrandTypePageUrl = '/card-brand-type';
  const cardBrandTypePageUrlPattern = new RegExp('/card-brand-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardBrandTypeSample = { cardBrandTypeCode: 'blue', cardBrandType: 'cross-platform Card' };

  let cardBrandType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-brand-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-brand-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-brand-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardBrandType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-brand-types/${cardBrandType.id}`,
      }).then(() => {
        cardBrandType = undefined;
      });
    }
  });

  it('CardBrandTypes menu should load CardBrandTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-brand-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardBrandType').should('exist');
    cy.url().should('match', cardBrandTypePageUrlPattern);
  });

  describe('CardBrandType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardBrandTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardBrandType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-brand-type/new$'));
        cy.getEntityCreateUpdateHeading('CardBrandType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardBrandTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-brand-types',
          body: cardBrandTypeSample,
        }).then(({ body }) => {
          cardBrandType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-brand-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardBrandType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardBrandTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardBrandType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardBrandType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardBrandTypePageUrlPattern);
      });

      it('edit button click should load edit CardBrandType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardBrandType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardBrandTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CardBrandType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardBrandType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardBrandTypePageUrlPattern);

        cardBrandType = undefined;
      });
    });
  });

  describe('new CardBrandType page', () => {
    beforeEach(() => {
      cy.visit(`${cardBrandTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardBrandType');
    });

    it('should create an instance of CardBrandType', () => {
      cy.get(`[data-cy="cardBrandTypeCode"]`).type('Berkshire optical').should('have.value', 'Berkshire optical');

      cy.get(`[data-cy="cardBrandType"]`).type('Usability SMS Virginia').should('have.value', 'Usability SMS Virginia');

      cy.get(`[data-cy="cardBrandTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardBrandType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardBrandTypePageUrlPattern);
    });
  });
});
