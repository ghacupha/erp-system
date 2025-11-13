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

describe('CrbSubmittingInstitutionCategory e2e test', () => {
  const crbSubmittingInstitutionCategoryPageUrl = '/crb-submitting-institution-category';
  const crbSubmittingInstitutionCategoryPageUrlPattern = new RegExp('/crb-submitting-institution-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbSubmittingInstitutionCategorySample = {
    submittingInstitutionCategoryTypeCode: 'Concrete intuitive Plaza',
    submittingInstitutionCategoryType: 'neural',
  };

  let crbSubmittingInstitutionCategory: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-submitting-institution-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-submitting-institution-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-submitting-institution-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbSubmittingInstitutionCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-submitting-institution-categories/${crbSubmittingInstitutionCategory.id}`,
      }).then(() => {
        crbSubmittingInstitutionCategory = undefined;
      });
    }
  });

  it('CrbSubmittingInstitutionCategories menu should load CrbSubmittingInstitutionCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-submitting-institution-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbSubmittingInstitutionCategory').should('exist');
    cy.url().should('match', crbSubmittingInstitutionCategoryPageUrlPattern);
  });

  describe('CrbSubmittingInstitutionCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbSubmittingInstitutionCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbSubmittingInstitutionCategory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-submitting-institution-category/new$'));
        cy.getEntityCreateUpdateHeading('CrbSubmittingInstitutionCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubmittingInstitutionCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-submitting-institution-categories',
          body: crbSubmittingInstitutionCategorySample,
        }).then(({ body }) => {
          crbSubmittingInstitutionCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-submitting-institution-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbSubmittingInstitutionCategory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbSubmittingInstitutionCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbSubmittingInstitutionCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbSubmittingInstitutionCategory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubmittingInstitutionCategoryPageUrlPattern);
      });

      it('edit button click should load edit CrbSubmittingInstitutionCategory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbSubmittingInstitutionCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubmittingInstitutionCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbSubmittingInstitutionCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbSubmittingInstitutionCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbSubmittingInstitutionCategoryPageUrlPattern);

        crbSubmittingInstitutionCategory = undefined;
      });
    });
  });

  describe('new CrbSubmittingInstitutionCategory page', () => {
    beforeEach(() => {
      cy.visit(`${crbSubmittingInstitutionCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbSubmittingInstitutionCategory');
    });

    it('should create an instance of CrbSubmittingInstitutionCategory', () => {
      cy.get(`[data-cy="submittingInstitutionCategoryTypeCode"]`)
        .type('Mews Handcrafted Arizona')
        .should('have.value', 'Mews Handcrafted Arizona');

      cy.get(`[data-cy="submittingInstitutionCategoryType"]`).type('Executive').should('have.value', 'Executive');

      cy.get(`[data-cy="submittingInstitutionCategoryDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbSubmittingInstitutionCategory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbSubmittingInstitutionCategoryPageUrlPattern);
    });
  });
});
