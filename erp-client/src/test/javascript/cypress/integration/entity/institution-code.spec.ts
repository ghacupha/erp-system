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

describe('InstitutionCode e2e test', () => {
  const institutionCodePageUrl = '/institution-code';
  const institutionCodePageUrlPattern = new RegExp('/institution-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const institutionCodeSample = { institutionCode: 'Optional Buckinghamshire', institutionName: 'intermediate connect' };

  let institutionCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/institution-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/institution-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/institution-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (institutionCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-codes/${institutionCode.id}`,
      }).then(() => {
        institutionCode = undefined;
      });
    }
  });

  it('InstitutionCodes menu should load InstitutionCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('institution-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InstitutionCode').should('exist');
    cy.url().should('match', institutionCodePageUrlPattern);
  });

  describe('InstitutionCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(institutionCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InstitutionCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/institution-code/new$'));
        cy.getEntityCreateUpdateHeading('InstitutionCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/institution-codes',
          body: institutionCodeSample,
        }).then(({ body }) => {
          institutionCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/institution-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [institutionCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(institutionCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InstitutionCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('institutionCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionCodePageUrlPattern);
      });

      it('edit button click should load edit InstitutionCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InstitutionCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionCodePageUrlPattern);
      });

      it('last delete button click should delete instance of InstitutionCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('institutionCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionCodePageUrlPattern);

        institutionCode = undefined;
      });
    });
  });

  describe('new InstitutionCode page', () => {
    beforeEach(() => {
      cy.visit(`${institutionCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('InstitutionCode');
    });

    it('should create an instance of InstitutionCode', () => {
      cy.get(`[data-cy="institutionCode"]`).type('Pennsylvania Lead Principal').should('have.value', 'Pennsylvania Lead Principal');

      cy.get(`[data-cy="institutionName"]`).type('Granite Cotton').should('have.value', 'Granite Cotton');

      cy.get(`[data-cy="shortName"]`).type('mobile Nauru').should('have.value', 'mobile Nauru');

      cy.get(`[data-cy="category"]`).type('Ohio').should('have.value', 'Ohio');

      cy.get(`[data-cy="institutionCategory"]`).type('bus indexing auxiliary').should('have.value', 'bus indexing auxiliary');

      cy.get(`[data-cy="institutionOwnership"]`).type('COM').should('have.value', 'COM');

      cy.get(`[data-cy="dateLicensed"]`).type('2022-04-05').should('have.value', '2022-04-05');

      cy.get(`[data-cy="institutionStatus"]`).type('Illinois cross-platform').should('have.value', 'Illinois cross-platform');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        institutionCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', institutionCodePageUrlPattern);
    });
  });
});
