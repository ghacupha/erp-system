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

describe('CrbSourceOfInformationType e2e test', () => {
  const crbSourceOfInformationTypePageUrl = '/crb-source-of-information-type';
  const crbSourceOfInformationTypePageUrlPattern = new RegExp('/crb-source-of-information-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbSourceOfInformationTypeSample = { sourceOfInformationTypeCode: 'invoice EXE directional' };

  let crbSourceOfInformationType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-source-of-information-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-source-of-information-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-source-of-information-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbSourceOfInformationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-source-of-information-types/${crbSourceOfInformationType.id}`,
      }).then(() => {
        crbSourceOfInformationType = undefined;
      });
    }
  });

  it('CrbSourceOfInformationTypes menu should load CrbSourceOfInformationTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-source-of-information-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbSourceOfInformationType').should('exist');
    cy.url().should('match', crbSourceOfInformationTypePageUrlPattern);
  });

  describe('CrbSourceOfInformationType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbSourceOfInformationTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbSourceOfInformationType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-source-of-information-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbSourceOfInformationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSourceOfInformationTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-source-of-information-types',
          body: crbSourceOfInformationTypeSample,
        }).then(({ body }) => {
          crbSourceOfInformationType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-source-of-information-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbSourceOfInformationType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbSourceOfInformationTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbSourceOfInformationType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbSourceOfInformationType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSourceOfInformationTypePageUrlPattern);
      });

      it('edit button click should load edit CrbSourceOfInformationType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbSourceOfInformationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSourceOfInformationTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbSourceOfInformationType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbSourceOfInformationType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSourceOfInformationTypePageUrlPattern);

        crbSourceOfInformationType = undefined;
      });
    });
  });

  describe('new CrbSourceOfInformationType page', () => {
    beforeEach(() => {
      cy.visit(`${crbSourceOfInformationTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbSourceOfInformationType');
    });

    it('should create an instance of CrbSourceOfInformationType', () => {
      cy.get(`[data-cy="sourceOfInformationTypeCode"]`).type('SMTP navigating').should('have.value', 'SMTP navigating');

      cy.get(`[data-cy="sourceOfInformationTypeDescription"]`).type('Associate').should('have.value', 'Associate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbSourceOfInformationType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbSourceOfInformationTypePageUrlPattern);
    });
  });
});
