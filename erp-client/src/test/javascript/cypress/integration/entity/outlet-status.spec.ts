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

describe('OutletStatus e2e test', () => {
  const outletStatusPageUrl = '/outlet-status';
  const outletStatusPageUrlPattern = new RegExp('/outlet-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const outletStatusSample = { branchStatusTypeCode: 'transmit Interactions dedicated', branchStatusType: 'CLOSED' };

  let outletStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/outlet-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/outlet-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/outlet-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (outletStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/outlet-statuses/${outletStatus.id}`,
      }).then(() => {
        outletStatus = undefined;
      });
    }
  });

  it('OutletStatuses menu should load OutletStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('outlet-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OutletStatus').should('exist');
    cy.url().should('match', outletStatusPageUrlPattern);
  });

  describe('OutletStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(outletStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OutletStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/outlet-status/new$'));
        cy.getEntityCreateUpdateHeading('OutletStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/outlet-statuses',
          body: outletStatusSample,
        }).then(({ body }) => {
          outletStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/outlet-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [outletStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(outletStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OutletStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('outletStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletStatusPageUrlPattern);
      });

      it('edit button click should load edit OutletStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OutletStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of OutletStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('outletStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', outletStatusPageUrlPattern);

        outletStatus = undefined;
      });
    });
  });

  describe('new OutletStatus page', () => {
    beforeEach(() => {
      cy.visit(`${outletStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('OutletStatus');
    });

    it('should create an instance of OutletStatus', () => {
      cy.get(`[data-cy="branchStatusTypeCode"]`).type('experiences').should('have.value', 'experiences');

      cy.get(`[data-cy="branchStatusType"]`).select('ACTIVE');

      cy.get(`[data-cy="branchStatusTypeDescription"]`).type('Future Cotton Borders').should('have.value', 'Future Cotton Borders');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        outletStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', outletStatusPageUrlPattern);
    });
  });
});
