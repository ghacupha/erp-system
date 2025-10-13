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

describe('UltimateBeneficiaryCategory e2e test', () => {
  const ultimateBeneficiaryCategoryPageUrl = '/ultimate-beneficiary-category';
  const ultimateBeneficiaryCategoryPageUrlPattern = new RegExp('/ultimate-beneficiary-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const ultimateBeneficiaryCategorySample = {
    ultimateBeneficiaryCategoryTypeCode: 'blockchains',
    ultimateBeneficiaryType: 'Incredible generate Michigan',
  };

  let ultimateBeneficiaryCategory: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ultimate-beneficiary-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ultimate-beneficiary-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ultimate-beneficiary-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ultimateBeneficiaryCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ultimate-beneficiary-categories/${ultimateBeneficiaryCategory.id}`,
      }).then(() => {
        ultimateBeneficiaryCategory = undefined;
      });
    }
  });

  it('UltimateBeneficiaryCategories menu should load UltimateBeneficiaryCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ultimate-beneficiary-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UltimateBeneficiaryCategory').should('exist');
    cy.url().should('match', ultimateBeneficiaryCategoryPageUrlPattern);
  });

  describe('UltimateBeneficiaryCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ultimateBeneficiaryCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UltimateBeneficiaryCategory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ultimate-beneficiary-category/new$'));
        cy.getEntityCreateUpdateHeading('UltimateBeneficiaryCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ultimate-beneficiary-categories',
          body: ultimateBeneficiaryCategorySample,
        }).then(({ body }) => {
          ultimateBeneficiaryCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ultimate-beneficiary-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ultimateBeneficiaryCategory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ultimateBeneficiaryCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UltimateBeneficiaryCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ultimateBeneficiaryCategory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryCategoryPageUrlPattern);
      });

      it('edit button click should load edit UltimateBeneficiaryCategory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UltimateBeneficiaryCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of UltimateBeneficiaryCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ultimateBeneficiaryCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryCategoryPageUrlPattern);

        ultimateBeneficiaryCategory = undefined;
      });
    });
  });

  describe('new UltimateBeneficiaryCategory page', () => {
    beforeEach(() => {
      cy.visit(`${ultimateBeneficiaryCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('UltimateBeneficiaryCategory');
    });

    it('should create an instance of UltimateBeneficiaryCategory', () => {
      cy.get(`[data-cy="ultimateBeneficiaryCategoryTypeCode"]`)
        .type('compressing background transmitting')
        .should('have.value', 'compressing background transmitting');

      cy.get(`[data-cy="ultimateBeneficiaryType"]`).type('Intranet Forge Krone').should('have.value', 'Intranet Forge Krone');

      cy.get(`[data-cy="ultimateBeneficiaryCategoryTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        ultimateBeneficiaryCategory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', ultimateBeneficiaryCategoryPageUrlPattern);
    });
  });
});
