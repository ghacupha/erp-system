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

describe('ProcessStatus e2e test', () => {
  const processStatusPageUrl = '/process-status';
  const processStatusPageUrlPattern = new RegExp('/process-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const processStatusSample = { statusCode: 'quantifying Integration', description: 'SMTP Indiana' };

  let processStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/process-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/process-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/process-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (processStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/process-statuses/${processStatus.id}`,
      }).then(() => {
        processStatus = undefined;
      });
    }
  });

  it('ProcessStatuses menu should load ProcessStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('process-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProcessStatus').should('exist');
    cy.url().should('match', processStatusPageUrlPattern);
  });

  describe('ProcessStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(processStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProcessStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/process-status/new$'));
        cy.getEntityCreateUpdateHeading('ProcessStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', processStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/process-statuses',
          body: processStatusSample,
        }).then(({ body }) => {
          processStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/process-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [processStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(processStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProcessStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('processStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', processStatusPageUrlPattern);
      });

      it('edit button click should load edit ProcessStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProcessStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', processStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of ProcessStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('processStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', processStatusPageUrlPattern);

        processStatus = undefined;
      });
    });
  });

  describe('new ProcessStatus page', () => {
    beforeEach(() => {
      cy.visit(`${processStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ProcessStatus');
    });

    it('should create an instance of ProcessStatus', () => {
      cy.get(`[data-cy="statusCode"]`).type('Engineer wireless').should('have.value', 'Engineer wireless');

      cy.get(`[data-cy="description"]`).type('Netherlands hacking').should('have.value', 'Netherlands hacking');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        processStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', processStatusPageUrlPattern);
    });
  });
});
