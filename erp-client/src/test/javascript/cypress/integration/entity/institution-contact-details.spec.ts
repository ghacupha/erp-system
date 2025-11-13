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

describe('InstitutionContactDetails e2e test', () => {
  const institutionContactDetailsPageUrl = '/institution-contact-details';
  const institutionContactDetailsPageUrlPattern = new RegExp('/institution-contact-details(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const institutionContactDetailsSample = {
    entityId: 'Buckinghamshire',
    entityName: 'Trail Ball Borders',
    contactType: 'invoice Kids visualize',
  };

  let institutionContactDetails: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/institution-contact-details+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/institution-contact-details').as('postEntityRequest');
    cy.intercept('DELETE', '/api/institution-contact-details/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (institutionContactDetails) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-contact-details/${institutionContactDetails.id}`,
      }).then(() => {
        institutionContactDetails = undefined;
      });
    }
  });

  it('InstitutionContactDetails menu should load InstitutionContactDetails page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('institution-contact-details');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InstitutionContactDetails').should('exist');
    cy.url().should('match', institutionContactDetailsPageUrlPattern);
  });

  describe('InstitutionContactDetails page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(institutionContactDetailsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InstitutionContactDetails page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/institution-contact-details/new$'));
        cy.getEntityCreateUpdateHeading('InstitutionContactDetails');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionContactDetailsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/institution-contact-details',
          body: institutionContactDetailsSample,
        }).then(({ body }) => {
          institutionContactDetails = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/institution-contact-details+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [institutionContactDetails],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(institutionContactDetailsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InstitutionContactDetails page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('institutionContactDetails');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionContactDetailsPageUrlPattern);
      });

      it('edit button click should load edit InstitutionContactDetails page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InstitutionContactDetails');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionContactDetailsPageUrlPattern);
      });

      it('last delete button click should delete instance of InstitutionContactDetails', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('institutionContactDetails').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionContactDetailsPageUrlPattern);

        institutionContactDetails = undefined;
      });
    });
  });

  describe('new InstitutionContactDetails page', () => {
    beforeEach(() => {
      cy.visit(`${institutionContactDetailsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('InstitutionContactDetails');
    });

    it('should create an instance of InstitutionContactDetails', () => {
      cy.get(`[data-cy="entityId"]`).type('Cambridgeshire').should('have.value', 'Cambridgeshire');

      cy.get(`[data-cy="entityName"]`).type('West').should('have.value', 'West');

      cy.get(`[data-cy="contactType"]`).type('Metal District blue').should('have.value', 'Metal District blue');

      cy.get(`[data-cy="contactLevel"]`).type('Avon Nevada GB').should('have.value', 'Avon Nevada GB');

      cy.get(`[data-cy="contactValue"]`).type('clear-thinking Balanced').should('have.value', 'clear-thinking Balanced');

      cy.get(`[data-cy="contactName"]`).type('digital lavender deposit').should('have.value', 'digital lavender deposit');

      cy.get(`[data-cy="contactDesignation"]`).type('static Legacy Wooden').should('have.value', 'static Legacy Wooden');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        institutionContactDetails = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', institutionContactDetailsPageUrlPattern);
    });
  });
});
