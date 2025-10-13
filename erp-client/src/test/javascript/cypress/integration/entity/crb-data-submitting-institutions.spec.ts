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

describe('CrbDataSubmittingInstitutions e2e test', () => {
  const crbDataSubmittingInstitutionsPageUrl = '/crb-data-submitting-institutions';
  const crbDataSubmittingInstitutionsPageUrlPattern = new RegExp('/crb-data-submitting-institutions(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbDataSubmittingInstitutionsSample = {
    institutionCode: 'Lead Synchronised Auto',
    institutionName: 'Handcrafted',
    institutionCategory: 'functionalities Account',
  };

  let crbDataSubmittingInstitutions: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-data-submitting-institutions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-data-submitting-institutions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-data-submitting-institutions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbDataSubmittingInstitutions) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-data-submitting-institutions/${crbDataSubmittingInstitutions.id}`,
      }).then(() => {
        crbDataSubmittingInstitutions = undefined;
      });
    }
  });

  it('CrbDataSubmittingInstitutions menu should load CrbDataSubmittingInstitutions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-data-submitting-institutions');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbDataSubmittingInstitutions').should('exist');
    cy.url().should('match', crbDataSubmittingInstitutionsPageUrlPattern);
  });

  describe('CrbDataSubmittingInstitutions page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbDataSubmittingInstitutionsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbDataSubmittingInstitutions page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-data-submitting-institutions/new$'));
        cy.getEntityCreateUpdateHeading('CrbDataSubmittingInstitutions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbDataSubmittingInstitutionsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-data-submitting-institutions',
          body: crbDataSubmittingInstitutionsSample,
        }).then(({ body }) => {
          crbDataSubmittingInstitutions = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-data-submitting-institutions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbDataSubmittingInstitutions],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbDataSubmittingInstitutionsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbDataSubmittingInstitutions page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbDataSubmittingInstitutions');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbDataSubmittingInstitutionsPageUrlPattern);
      });

      it('edit button click should load edit CrbDataSubmittingInstitutions page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbDataSubmittingInstitutions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbDataSubmittingInstitutionsPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbDataSubmittingInstitutions', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbDataSubmittingInstitutions').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbDataSubmittingInstitutionsPageUrlPattern);

        crbDataSubmittingInstitutions = undefined;
      });
    });
  });

  describe('new CrbDataSubmittingInstitutions page', () => {
    beforeEach(() => {
      cy.visit(`${crbDataSubmittingInstitutionsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbDataSubmittingInstitutions');
    });

    it('should create an instance of CrbDataSubmittingInstitutions', () => {
      cy.get(`[data-cy="institutionCode"]`).type('Buckinghamshire').should('have.value', 'Buckinghamshire');

      cy.get(`[data-cy="institutionName"]`).type('deposit software Chicken').should('have.value', 'deposit software Chicken');

      cy.get(`[data-cy="institutionCategory"]`).type('payment').should('have.value', 'payment');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbDataSubmittingInstitutions = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbDataSubmittingInstitutionsPageUrlPattern);
    });
  });
});
