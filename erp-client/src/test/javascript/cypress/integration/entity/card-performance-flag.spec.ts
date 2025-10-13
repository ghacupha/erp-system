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

describe('CardPerformanceFlag e2e test', () => {
  const cardPerformanceFlagPageUrl = '/card-performance-flag';
  const cardPerformanceFlagPageUrlPattern = new RegExp('/card-performance-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardPerformanceFlagSample = { cardPerformanceFlag: 'Y', cardPerformanceFlagDescription: 'Senior Specialist De-engineered' };

  let cardPerformanceFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-performance-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-performance-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-performance-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardPerformanceFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-performance-flags/${cardPerformanceFlag.id}`,
      }).then(() => {
        cardPerformanceFlag = undefined;
      });
    }
  });

  it('CardPerformanceFlags menu should load CardPerformanceFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-performance-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardPerformanceFlag').should('exist');
    cy.url().should('match', cardPerformanceFlagPageUrlPattern);
  });

  describe('CardPerformanceFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardPerformanceFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardPerformanceFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-performance-flag/new$'));
        cy.getEntityCreateUpdateHeading('CardPerformanceFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardPerformanceFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-performance-flags',
          body: cardPerformanceFlagSample,
        }).then(({ body }) => {
          cardPerformanceFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-performance-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardPerformanceFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardPerformanceFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardPerformanceFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardPerformanceFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardPerformanceFlagPageUrlPattern);
      });

      it('edit button click should load edit CardPerformanceFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardPerformanceFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardPerformanceFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of CardPerformanceFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardPerformanceFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardPerformanceFlagPageUrlPattern);

        cardPerformanceFlag = undefined;
      });
    });
  });

  describe('new CardPerformanceFlag page', () => {
    beforeEach(() => {
      cy.visit(`${cardPerformanceFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardPerformanceFlag');
    });

    it('should create an instance of CardPerformanceFlag', () => {
      cy.get(`[data-cy="cardPerformanceFlag"]`).select('N');

      cy.get(`[data-cy="cardPerformanceFlagDescription"]`).type('Developer Islands').should('have.value', 'Developer Islands');

      cy.get(`[data-cy="cardPerformanceFlagDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardPerformanceFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardPerformanceFlagPageUrlPattern);
    });
  });
});
