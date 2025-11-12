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

describe('BouncedChequeCategories e2e test', () => {
  const bouncedChequeCategoriesPageUrl = '/bounced-cheque-categories';
  const bouncedChequeCategoriesPageUrlPattern = new RegExp('/bounced-cheque-categories(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const bouncedChequeCategoriesSample = {
    bouncedChequeCategoryTypeCode: 'Oklahoma compressing',
    bouncedChequeCategoryType: 'Representative invoice black',
  };

  let bouncedChequeCategories: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bounced-cheque-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bounced-cheque-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bounced-cheque-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bouncedChequeCategories) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bounced-cheque-categories/${bouncedChequeCategories.id}`,
      }).then(() => {
        bouncedChequeCategories = undefined;
      });
    }
  });

  it('BouncedChequeCategories menu should load BouncedChequeCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bounced-cheque-categories');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BouncedChequeCategories').should('exist');
    cy.url().should('match', bouncedChequeCategoriesPageUrlPattern);
  });

  describe('BouncedChequeCategories page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bouncedChequeCategoriesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BouncedChequeCategories page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/bounced-cheque-categories/new$'));
        cy.getEntityCreateUpdateHeading('BouncedChequeCategories');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bouncedChequeCategoriesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bounced-cheque-categories',
          body: bouncedChequeCategoriesSample,
        }).then(({ body }) => {
          bouncedChequeCategories = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bounced-cheque-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [bouncedChequeCategories],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(bouncedChequeCategoriesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BouncedChequeCategories page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bouncedChequeCategories');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bouncedChequeCategoriesPageUrlPattern);
      });

      it('edit button click should load edit BouncedChequeCategories page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BouncedChequeCategories');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bouncedChequeCategoriesPageUrlPattern);
      });

      it('last delete button click should delete instance of BouncedChequeCategories', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('bouncedChequeCategories').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bouncedChequeCategoriesPageUrlPattern);

        bouncedChequeCategories = undefined;
      });
    });
  });

  describe('new BouncedChequeCategories page', () => {
    beforeEach(() => {
      cy.visit(`${bouncedChequeCategoriesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('BouncedChequeCategories');
    });

    it('should create an instance of BouncedChequeCategories', () => {
      cy.get(`[data-cy="bouncedChequeCategoryTypeCode"]`).type('plum Account').should('have.value', 'plum Account');

      cy.get(`[data-cy="bouncedChequeCategoryType"]`).type('Monitored firewall Kids').should('have.value', 'Monitored firewall Kids');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        bouncedChequeCategories = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', bouncedChequeCategoriesPageUrlPattern);
    });
  });
});
