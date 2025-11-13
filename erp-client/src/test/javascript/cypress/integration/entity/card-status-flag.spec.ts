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

describe('CardStatusFlag e2e test', () => {
  const cardStatusFlagPageUrl = '/card-status-flag';
  const cardStatusFlagPageUrlPattern = new RegExp('/card-status-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardStatusFlagSample = { cardStatusFlag: 'Y', cardStatusFlagDescription: 'generating Savings Path' };

  let cardStatusFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-status-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-status-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-status-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardStatusFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-status-flags/${cardStatusFlag.id}`,
      }).then(() => {
        cardStatusFlag = undefined;
      });
    }
  });

  it('CardStatusFlags menu should load CardStatusFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-status-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardStatusFlag').should('exist');
    cy.url().should('match', cardStatusFlagPageUrlPattern);
  });

  describe('CardStatusFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardStatusFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardStatusFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-status-flag/new$'));
        cy.getEntityCreateUpdateHeading('CardStatusFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatusFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-status-flags',
          body: cardStatusFlagSample,
        }).then(({ body }) => {
          cardStatusFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-status-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardStatusFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardStatusFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardStatusFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardStatusFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatusFlagPageUrlPattern);
      });

      it('edit button click should load edit CardStatusFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardStatusFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatusFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of CardStatusFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardStatusFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardStatusFlagPageUrlPattern);

        cardStatusFlag = undefined;
      });
    });
  });

  describe('new CardStatusFlag page', () => {
    beforeEach(() => {
      cy.visit(`${cardStatusFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardStatusFlag');
    });

    it('should create an instance of CardStatusFlag', () => {
      cy.get(`[data-cy="cardStatusFlag"]`).select('N');

      cy.get(`[data-cy="cardStatusFlagDescription"]`)
        .type('Orchestrator Bedfordshire Prairie')
        .should('have.value', 'Orchestrator Bedfordshire Prairie');

      cy.get(`[data-cy="cardStatusFlagDetails"]`).type('orange Unbranded Berkshire').should('have.value', 'orange Unbranded Berkshire');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardStatusFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardStatusFlagPageUrlPattern);
    });
  });
});
