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

describe('EmploymentTerms e2e test', () => {
  const employmentTermsPageUrl = '/employment-terms';
  const employmentTermsPageUrlPattern = new RegExp('/employment-terms(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const employmentTermsSample = { employmentTermsCode: 'connecting', employmentTermsStatus: 'Iran Bedfordshire Plastic' };

  let employmentTerms: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/employment-terms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employment-terms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employment-terms/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (employmentTerms) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employment-terms/${employmentTerms.id}`,
      }).then(() => {
        employmentTerms = undefined;
      });
    }
  });

  it('EmploymentTerms menu should load EmploymentTerms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employment-terms');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EmploymentTerms').should('exist');
    cy.url().should('match', employmentTermsPageUrlPattern);
  });

  describe('EmploymentTerms page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(employmentTermsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EmploymentTerms page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/employment-terms/new$'));
        cy.getEntityCreateUpdateHeading('EmploymentTerms');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', employmentTermsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/employment-terms',
          body: employmentTermsSample,
        }).then(({ body }) => {
          employmentTerms = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/employment-terms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [employmentTerms],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(employmentTermsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EmploymentTerms page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('employmentTerms');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', employmentTermsPageUrlPattern);
      });

      it('edit button click should load edit EmploymentTerms page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmploymentTerms');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', employmentTermsPageUrlPattern);
      });

      it('last delete button click should delete instance of EmploymentTerms', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('employmentTerms').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', employmentTermsPageUrlPattern);

        employmentTerms = undefined;
      });
    });
  });

  describe('new EmploymentTerms page', () => {
    beforeEach(() => {
      cy.visit(`${employmentTermsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('EmploymentTerms');
    });

    it('should create an instance of EmploymentTerms', () => {
      cy.get(`[data-cy="employmentTermsCode"]`).type('Turnpike Grocery').should('have.value', 'Turnpike Grocery');

      cy.get(`[data-cy="employmentTermsStatus"]`).type('Table Ohio').should('have.value', 'Table Ohio');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        employmentTerms = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', employmentTermsPageUrlPattern);
    });
  });
});
