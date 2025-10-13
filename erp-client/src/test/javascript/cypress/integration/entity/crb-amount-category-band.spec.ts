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

describe('CrbAmountCategoryBand e2e test', () => {
  const crbAmountCategoryBandPageUrl = '/crb-amount-category-band';
  const crbAmountCategoryBandPageUrlPattern = new RegExp('/crb-amount-category-band(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbAmountCategoryBandSample = { amountCategoryBandCode: 'Lari', amountCategoryBand: 'SMS bricks-and-clicks Director' };

  let crbAmountCategoryBand: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-amount-category-bands+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-amount-category-bands').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-amount-category-bands/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbAmountCategoryBand) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-amount-category-bands/${crbAmountCategoryBand.id}`,
      }).then(() => {
        crbAmountCategoryBand = undefined;
      });
    }
  });

  it('CrbAmountCategoryBands menu should load CrbAmountCategoryBands page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-amount-category-band');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbAmountCategoryBand').should('exist');
    cy.url().should('match', crbAmountCategoryBandPageUrlPattern);
  });

  describe('CrbAmountCategoryBand page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbAmountCategoryBandPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbAmountCategoryBand page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-amount-category-band/new$'));
        cy.getEntityCreateUpdateHeading('CrbAmountCategoryBand');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAmountCategoryBandPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-amount-category-bands',
          body: crbAmountCategoryBandSample,
        }).then(({ body }) => {
          crbAmountCategoryBand = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-amount-category-bands+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbAmountCategoryBand],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbAmountCategoryBandPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbAmountCategoryBand page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbAmountCategoryBand');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAmountCategoryBandPageUrlPattern);
      });

      it('edit button click should load edit CrbAmountCategoryBand page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbAmountCategoryBand');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAmountCategoryBandPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbAmountCategoryBand', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbAmountCategoryBand').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAmountCategoryBandPageUrlPattern);

        crbAmountCategoryBand = undefined;
      });
    });
  });

  describe('new CrbAmountCategoryBand page', () => {
    beforeEach(() => {
      cy.visit(`${crbAmountCategoryBandPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbAmountCategoryBand');
    });

    it('should create an instance of CrbAmountCategoryBand', () => {
      cy.get(`[data-cy="amountCategoryBandCode"]`).type('mobile empower streamline').should('have.value', 'mobile empower streamline');

      cy.get(`[data-cy="amountCategoryBand"]`).type('Officer').should('have.value', 'Officer');

      cy.get(`[data-cy="amountCategoryBandDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbAmountCategoryBand = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbAmountCategoryBandPageUrlPattern);
    });
  });
});
