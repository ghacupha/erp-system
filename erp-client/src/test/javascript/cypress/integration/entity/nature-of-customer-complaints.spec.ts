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

describe('NatureOfCustomerComplaints e2e test', () => {
  const natureOfCustomerComplaintsPageUrl = '/nature-of-customer-complaints';
  const natureOfCustomerComplaintsPageUrlPattern = new RegExp('/nature-of-customer-complaints(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const natureOfCustomerComplaintsSample = {
    natureOfComplaintTypeCode: 'withdrawal payment',
    natureOfComplaintType: 'Buckinghamshire quantify',
  };

  let natureOfCustomerComplaints: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/nature-of-customer-complaints+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/nature-of-customer-complaints').as('postEntityRequest');
    cy.intercept('DELETE', '/api/nature-of-customer-complaints/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (natureOfCustomerComplaints) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/nature-of-customer-complaints/${natureOfCustomerComplaints.id}`,
      }).then(() => {
        natureOfCustomerComplaints = undefined;
      });
    }
  });

  it('NatureOfCustomerComplaints menu should load NatureOfCustomerComplaints page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('nature-of-customer-complaints');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NatureOfCustomerComplaints').should('exist');
    cy.url().should('match', natureOfCustomerComplaintsPageUrlPattern);
  });

  describe('NatureOfCustomerComplaints page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(natureOfCustomerComplaintsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NatureOfCustomerComplaints page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/nature-of-customer-complaints/new$'));
        cy.getEntityCreateUpdateHeading('NatureOfCustomerComplaints');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', natureOfCustomerComplaintsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/nature-of-customer-complaints',
          body: natureOfCustomerComplaintsSample,
        }).then(({ body }) => {
          natureOfCustomerComplaints = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/nature-of-customer-complaints+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [natureOfCustomerComplaints],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(natureOfCustomerComplaintsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NatureOfCustomerComplaints page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('natureOfCustomerComplaints');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', natureOfCustomerComplaintsPageUrlPattern);
      });

      it('edit button click should load edit NatureOfCustomerComplaints page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NatureOfCustomerComplaints');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', natureOfCustomerComplaintsPageUrlPattern);
      });

      it('last delete button click should delete instance of NatureOfCustomerComplaints', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('natureOfCustomerComplaints').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', natureOfCustomerComplaintsPageUrlPattern);

        natureOfCustomerComplaints = undefined;
      });
    });
  });

  describe('new NatureOfCustomerComplaints page', () => {
    beforeEach(() => {
      cy.visit(`${natureOfCustomerComplaintsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('NatureOfCustomerComplaints');
    });

    it('should create an instance of NatureOfCustomerComplaints', () => {
      cy.get(`[data-cy="natureOfComplaintTypeCode"]`).type('synthesize').should('have.value', 'synthesize');

      cy.get(`[data-cy="natureOfComplaintType"]`).type('orchid Rustic Crescent').should('have.value', 'orchid Rustic Crescent');

      cy.get(`[data-cy="natureOfComplaintTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        natureOfCustomerComplaints = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', natureOfCustomerComplaintsPageUrlPattern);
    });
  });
});
