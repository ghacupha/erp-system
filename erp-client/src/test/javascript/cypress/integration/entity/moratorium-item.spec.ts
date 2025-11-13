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

describe('MoratoriumItem e2e test', () => {
  const moratoriumItemPageUrl = '/moratorium-item';
  const moratoriumItemPageUrlPattern = new RegExp('/moratorium-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const moratoriumItemSample = { moratoriumItemTypeCode: 'neutral Metrics', moratoriumItemType: 'compressing Cotton' };

  let moratoriumItem: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/moratorium-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/moratorium-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/moratorium-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (moratoriumItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/moratorium-items/${moratoriumItem.id}`,
      }).then(() => {
        moratoriumItem = undefined;
      });
    }
  });

  it('MoratoriumItems menu should load MoratoriumItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('moratorium-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MoratoriumItem').should('exist');
    cy.url().should('match', moratoriumItemPageUrlPattern);
  });

  describe('MoratoriumItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moratoriumItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MoratoriumItem page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/moratorium-item/new$'));
        cy.getEntityCreateUpdateHeading('MoratoriumItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', moratoriumItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/moratorium-items',
          body: moratoriumItemSample,
        }).then(({ body }) => {
          moratoriumItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/moratorium-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [moratoriumItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(moratoriumItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MoratoriumItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moratoriumItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', moratoriumItemPageUrlPattern);
      });

      it('edit button click should load edit MoratoriumItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoratoriumItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', moratoriumItemPageUrlPattern);
      });

      it('last delete button click should delete instance of MoratoriumItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('moratoriumItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', moratoriumItemPageUrlPattern);

        moratoriumItem = undefined;
      });
    });
  });

  describe('new MoratoriumItem page', () => {
    beforeEach(() => {
      cy.visit(`${moratoriumItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('MoratoriumItem');
    });

    it('should create an instance of MoratoriumItem', () => {
      cy.get(`[data-cy="moratoriumItemTypeCode"]`).type('invoice Malawi technologies').should('have.value', 'invoice Malawi technologies');

      cy.get(`[data-cy="moratoriumItemType"]`).type('Russian Shoes').should('have.value', 'Russian Shoes');

      cy.get(`[data-cy="moratoriumTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        moratoriumItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', moratoriumItemPageUrlPattern);
    });
  });
});
