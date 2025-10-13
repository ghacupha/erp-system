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

describe('SystemModule e2e test', () => {
  const systemModulePageUrl = '/system-module';
  const systemModulePageUrlPattern = new RegExp('/system-module(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const systemModuleSample = { moduleName: 'Avon Delaware Architect' };

  let systemModule: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/system-modules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/system-modules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/system-modules/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (systemModule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/system-modules/${systemModule.id}`,
      }).then(() => {
        systemModule = undefined;
      });
    }
  });

  it('SystemModules menu should load SystemModules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('system-module');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SystemModule').should('exist');
    cy.url().should('match', systemModulePageUrlPattern);
  });

  describe('SystemModule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(systemModulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SystemModule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/system-module/new$'));
        cy.getEntityCreateUpdateHeading('SystemModule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemModulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/system-modules',
          body: systemModuleSample,
        }).then(({ body }) => {
          systemModule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/system-modules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [systemModule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(systemModulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SystemModule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('systemModule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemModulePageUrlPattern);
      });

      it('edit button click should load edit SystemModule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SystemModule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemModulePageUrlPattern);
      });

      it('last delete button click should delete instance of SystemModule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('systemModule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', systemModulePageUrlPattern);

        systemModule = undefined;
      });
    });
  });

  describe('new SystemModule page', () => {
    beforeEach(() => {
      cy.visit(`${systemModulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SystemModule');
    });

    it('should create an instance of SystemModule', () => {
      cy.get(`[data-cy="moduleName"]`).type('Analyst Shoals Metal').should('have.value', 'Analyst Shoals Metal');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        systemModule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', systemModulePageUrlPattern);
    });
  });
});
