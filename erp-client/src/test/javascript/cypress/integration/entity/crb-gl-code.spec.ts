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

describe('CrbGlCode e2e test', () => {
  const crbGlCodePageUrl = '/crb-gl-code';
  const crbGlCodePageUrlPattern = new RegExp('/crb-gl-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbGlCodeSample = {
    glCode: 'override Integrated XML',
    glDescription: 'Brooks SMTP 24/365',
    glType: 'systems Concrete',
    institutionCategory: 'Rupee Concrete',
  };

  let crbGlCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-gl-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-gl-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-gl-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbGlCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-gl-codes/${crbGlCode.id}`,
      }).then(() => {
        crbGlCode = undefined;
      });
    }
  });

  it('CrbGlCodes menu should load CrbGlCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-gl-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbGlCode').should('exist');
    cy.url().should('match', crbGlCodePageUrlPattern);
  });

  describe('CrbGlCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbGlCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbGlCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-gl-code/new$'));
        cy.getEntityCreateUpdateHeading('CrbGlCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbGlCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-gl-codes',
          body: crbGlCodeSample,
        }).then(({ body }) => {
          crbGlCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-gl-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbGlCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbGlCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbGlCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbGlCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbGlCodePageUrlPattern);
      });

      it('edit button click should load edit CrbGlCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbGlCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbGlCodePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbGlCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbGlCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbGlCodePageUrlPattern);

        crbGlCode = undefined;
      });
    });
  });

  describe('new CrbGlCode page', () => {
    beforeEach(() => {
      cy.visit(`${crbGlCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbGlCode');
    });

    it('should create an instance of CrbGlCode', () => {
      cy.get(`[data-cy="glCode"]`).type('state Coordinator').should('have.value', 'state Coordinator');

      cy.get(`[data-cy="glDescription"]`).type('Lead Tasty').should('have.value', 'Lead Tasty');

      cy.get(`[data-cy="glType"]`).type('Web SMS').should('have.value', 'Web SMS');

      cy.get(`[data-cy="institutionCategory"]`).type('Intuitive matrix').should('have.value', 'Intuitive matrix');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbGlCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbGlCodePageUrlPattern);
    });
  });
});
