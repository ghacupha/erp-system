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

describe('OutletType e2e test', () => {
  const outletTypePageUrl = '/outlet-type';
  const outletTypePageUrlPattern = new RegExp('/outlet-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const outletTypeSample = { outletTypeCode: 'Prairie Loan Lodge', outletType: 'payment' };

  let outletType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/outlet-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/outlet-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/outlet-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (outletType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/outlet-types/${outletType.id}`,
      }).then(() => {
        outletType = undefined;
      });
    }
  });

  it('OutletTypes menu should load OutletTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('outlet-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OutletType').should('exist');
    cy.url().should('match', outletTypePageUrlPattern);
  });

  describe('OutletType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(outletTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OutletType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/outlet-type/new$'));
        cy.getEntityCreateUpdateHeading('OutletType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/outlet-types',
          body: outletTypeSample,
        }).then(({ body }) => {
          outletType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/outlet-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [outletType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(outletTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OutletType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('outletType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletTypePageUrlPattern);
      });

      it('edit button click should load edit OutletType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OutletType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletTypePageUrlPattern);
      });

      it('last delete button click should delete instance of OutletType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('outletType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletTypePageUrlPattern);

        outletType = undefined;
      });
    });
  });

  describe('new OutletType page', () => {
    beforeEach(() => {
      cy.visit(`${outletTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('OutletType');
    });

    it('should create an instance of OutletType', () => {
      cy.get(`[data-cy="outletTypeCode"]`).type('empower').should('have.value', 'empower');

      cy.get(`[data-cy="outletType"]`).type('transmit Buckinghamshire').should('have.value', 'transmit Buckinghamshire');

      cy.get(`[data-cy="outletTypeDetails"]`).type('feed auxiliary').should('have.value', 'feed auxiliary');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        outletType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', outletTypePageUrlPattern);
    });
  });
});
