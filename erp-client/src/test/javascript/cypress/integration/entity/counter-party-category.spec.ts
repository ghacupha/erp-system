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

describe('CounterPartyCategory e2e test', () => {
  const counterPartyCategoryPageUrl = '/counter-party-category';
  const counterPartyCategoryPageUrlPattern = new RegExp('/counter-party-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const counterPartyCategorySample = { counterpartyCategoryCode: 'PCI', counterpartyCategoryCodeDetails: 'FOREIGN' };

  let counterPartyCategory: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/counter-party-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/counter-party-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/counter-party-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (counterPartyCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/counter-party-categories/${counterPartyCategory.id}`,
      }).then(() => {
        counterPartyCategory = undefined;
      });
    }
  });

  it('CounterPartyCategories menu should load CounterPartyCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('counter-party-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CounterPartyCategory').should('exist');
    cy.url().should('match', counterPartyCategoryPageUrlPattern);
  });

  describe('CounterPartyCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(counterPartyCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CounterPartyCategory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/counter-party-category/new$'));
        cy.getEntityCreateUpdateHeading('CounterPartyCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/counter-party-categories',
          body: counterPartyCategorySample,
        }).then(({ body }) => {
          counterPartyCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/counter-party-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [counterPartyCategory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(counterPartyCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CounterPartyCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('counterPartyCategory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyCategoryPageUrlPattern);
      });

      it('edit button click should load edit CounterPartyCategory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CounterPartyCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of CounterPartyCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('counterPartyCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyCategoryPageUrlPattern);

        counterPartyCategory = undefined;
      });
    });
  });

  describe('new CounterPartyCategory page', () => {
    beforeEach(() => {
      cy.visit(`${counterPartyCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CounterPartyCategory');
    });

    it('should create an instance of CounterPartyCategory', () => {
      cy.get(`[data-cy="counterpartyCategoryCode"]`).type('Ranch Road Sausages').should('have.value', 'Ranch Road Sausages');

      cy.get(`[data-cy="counterpartyCategoryCodeDetails"]`).select('LOCAL');

      cy.get(`[data-cy="counterpartyCategoryDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        counterPartyCategory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', counterPartyCategoryPageUrlPattern);
    });
  });
});
