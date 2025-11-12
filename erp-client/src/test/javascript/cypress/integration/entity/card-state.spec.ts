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

describe('CardState e2e test', () => {
  const cardStatePageUrl = '/card-state';
  const cardStatePageUrlPattern = new RegExp('/card-state(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardStateSample = { cardStateFlagDetails: 'responsive ROI' };

  let cardState: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-states+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-states').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-states/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardState) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-states/${cardState.id}`,
      }).then(() => {
        cardState = undefined;
      });
    }
  });

  it('CardStates menu should load CardStates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-state');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardState').should('exist');
    cy.url().should('match', cardStatePageUrlPattern);
  });

  describe('CardState page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardStatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardState page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-state/new$'));
        cy.getEntityCreateUpdateHeading('CardState');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-states',
          body: cardStateSample,
        }).then(({ body }) => {
          cardState = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-states+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardState],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardStatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardState page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardState');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatePageUrlPattern);
      });

      it('edit button click should load edit CardState page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardState');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatePageUrlPattern);
      });

      it('last delete button click should delete instance of CardState', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardState').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatePageUrlPattern);

        cardState = undefined;
      });
    });
  });

  describe('new CardState page', () => {
    beforeEach(() => {
      cy.visit(`${cardStatePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardState');
    });

    it('should create an instance of CardState', () => {
      cy.get(`[data-cy="cardStateFlag"]`).select('V');

      cy.get(`[data-cy="cardStateFlagDetails"]`).type('Spain').should('have.value', 'Spain');

      cy.get(`[data-cy="cardStateFlagDescription"]`).type('Integration').should('have.value', 'Integration');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardState = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardStatePageUrlPattern);
    });
  });
});
