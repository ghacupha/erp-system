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

describe('CardCharges e2e test', () => {
  const cardChargesPageUrl = '/card-charges';
  const cardChargesPageUrlPattern = new RegExp('/card-charges(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardChargesSample = { cardChargeType: 'blockchains', cardChargeTypeName: 'withdrawal' };

  let cardCharges: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-charges+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-charges').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-charges/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardCharges) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-charges/${cardCharges.id}`,
      }).then(() => {
        cardCharges = undefined;
      });
    }
  });

  it('CardCharges menu should load CardCharges page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-charges');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardCharges').should('exist');
    cy.url().should('match', cardChargesPageUrlPattern);
  });

  describe('CardCharges page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardChargesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardCharges page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-charges/new$'));
        cy.getEntityCreateUpdateHeading('CardCharges');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardChargesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-charges',
          body: cardChargesSample,
        }).then(({ body }) => {
          cardCharges = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-charges+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardCharges],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardChargesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardCharges page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardCharges');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardChargesPageUrlPattern);
      });

      it('edit button click should load edit CardCharges page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardCharges');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardChargesPageUrlPattern);
      });

      it('last delete button click should delete instance of CardCharges', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardCharges').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardChargesPageUrlPattern);

        cardCharges = undefined;
      });
    });
  });

  describe('new CardCharges page', () => {
    beforeEach(() => {
      cy.visit(`${cardChargesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardCharges');
    });

    it('should create an instance of CardCharges', () => {
      cy.get(`[data-cy="cardChargeType"]`).type('SAS capacitor India').should('have.value', 'SAS capacitor India');

      cy.get(`[data-cy="cardChargeTypeName"]`).type('tan SSL Pants').should('have.value', 'tan SSL Pants');

      cy.get(`[data-cy="cardChargeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardCharges = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardChargesPageUrlPattern);
    });
  });
});
