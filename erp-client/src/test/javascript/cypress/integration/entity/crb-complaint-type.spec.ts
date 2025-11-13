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

describe('CrbComplaintType e2e test', () => {
  const crbComplaintTypePageUrl = '/crb-complaint-type';
  const crbComplaintTypePageUrlPattern = new RegExp('/crb-complaint-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbComplaintTypeSample = { complaintTypeCode: 'Communications Factors Executive', complaintType: 'transmitter Program Berkshire' };

  let crbComplaintType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-complaint-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-complaint-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-complaint-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbComplaintType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-complaint-types/${crbComplaintType.id}`,
      }).then(() => {
        crbComplaintType = undefined;
      });
    }
  });

  it('CrbComplaintTypes menu should load CrbComplaintTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-complaint-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbComplaintType').should('exist');
    cy.url().should('match', crbComplaintTypePageUrlPattern);
  });

  describe('CrbComplaintType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbComplaintTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbComplaintType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-complaint-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbComplaintType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-complaint-types',
          body: crbComplaintTypeSample,
        }).then(({ body }) => {
          crbComplaintType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-complaint-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbComplaintType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbComplaintTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbComplaintType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbComplaintType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintTypePageUrlPattern);
      });

      it('edit button click should load edit CrbComplaintType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbComplaintType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbComplaintType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbComplaintType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintTypePageUrlPattern);

        crbComplaintType = undefined;
      });
    });
  });

  describe('new CrbComplaintType page', () => {
    beforeEach(() => {
      cy.visit(`${crbComplaintTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbComplaintType');
    });

    it('should create an instance of CrbComplaintType', () => {
      cy.get(`[data-cy="complaintTypeCode"]`).type('Health eyeballs regional').should('have.value', 'Health eyeballs regional');

      cy.get(`[data-cy="complaintType"]`).type('Harbors').should('have.value', 'Harbors');

      cy.get(`[data-cy="complaintTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbComplaintType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbComplaintTypePageUrlPattern);
    });
  });
});
