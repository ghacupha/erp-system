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

describe('CrbComplaintStatusType e2e test', () => {
  const crbComplaintStatusTypePageUrl = '/crb-complaint-status-type';
  const crbComplaintStatusTypePageUrlPattern = new RegExp('/crb-complaint-status-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbComplaintStatusTypeSample = { complaintStatusTypeCode: 'Soft parsing JSON', complaintStatusType: 'deposit Rustic' };

  let crbComplaintStatusType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-complaint-status-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-complaint-status-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-complaint-status-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbComplaintStatusType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-complaint-status-types/${crbComplaintStatusType.id}`,
      }).then(() => {
        crbComplaintStatusType = undefined;
      });
    }
  });

  it('CrbComplaintStatusTypes menu should load CrbComplaintStatusTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-complaint-status-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbComplaintStatusType').should('exist');
    cy.url().should('match', crbComplaintStatusTypePageUrlPattern);
  });

  describe('CrbComplaintStatusType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbComplaintStatusTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbComplaintStatusType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-complaint-status-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbComplaintStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintStatusTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-complaint-status-types',
          body: crbComplaintStatusTypeSample,
        }).then(({ body }) => {
          crbComplaintStatusType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-complaint-status-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbComplaintStatusType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbComplaintStatusTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbComplaintStatusType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbComplaintStatusType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintStatusTypePageUrlPattern);
      });

      it('edit button click should load edit CrbComplaintStatusType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbComplaintStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintStatusTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbComplaintStatusType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbComplaintStatusType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbComplaintStatusTypePageUrlPattern);

        crbComplaintStatusType = undefined;
      });
    });
  });

  describe('new CrbComplaintStatusType page', () => {
    beforeEach(() => {
      cy.visit(`${crbComplaintStatusTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbComplaintStatusType');
    });

    it('should create an instance of CrbComplaintStatusType', () => {
      cy.get(`[data-cy="complaintStatusTypeCode"]`).type('niches Yen transmit').should('have.value', 'niches Yen transmit');

      cy.get(`[data-cy="complaintStatusType"]`).type('Kuna Inverse Orchestrator').should('have.value', 'Kuna Inverse Orchestrator');

      cy.get(`[data-cy="complaintStatusDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbComplaintStatusType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbComplaintStatusTypePageUrlPattern);
    });
  });
});
