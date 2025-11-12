///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

describe('CrbFileTransmissionStatus e2e test', () => {
  const crbFileTransmissionStatusPageUrl = '/crb-file-transmission-status';
  const crbFileTransmissionStatusPageUrlPattern = new RegExp('/crb-file-transmission-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbFileTransmissionStatusSample = {
    submittedFileStatusTypeCode: 'Electronics extensible proactive',
    submittedFileStatusType: 'INCORRECT',
  };

  let crbFileTransmissionStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-file-transmission-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-file-transmission-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-file-transmission-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbFileTransmissionStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-file-transmission-statuses/${crbFileTransmissionStatus.id}`,
      }).then(() => {
        crbFileTransmissionStatus = undefined;
      });
    }
  });

  it('CrbFileTransmissionStatuses menu should load CrbFileTransmissionStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-file-transmission-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbFileTransmissionStatus').should('exist');
    cy.url().should('match', crbFileTransmissionStatusPageUrlPattern);
  });

  describe('CrbFileTransmissionStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbFileTransmissionStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbFileTransmissionStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-file-transmission-status/new$'));
        cy.getEntityCreateUpdateHeading('CrbFileTransmissionStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbFileTransmissionStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-file-transmission-statuses',
          body: crbFileTransmissionStatusSample,
        }).then(({ body }) => {
          crbFileTransmissionStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-file-transmission-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbFileTransmissionStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbFileTransmissionStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbFileTransmissionStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbFileTransmissionStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbFileTransmissionStatusPageUrlPattern);
      });

      it('edit button click should load edit CrbFileTransmissionStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbFileTransmissionStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbFileTransmissionStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbFileTransmissionStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbFileTransmissionStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbFileTransmissionStatusPageUrlPattern);

        crbFileTransmissionStatus = undefined;
      });
    });
  });

  describe('new CrbFileTransmissionStatus page', () => {
    beforeEach(() => {
      cy.visit(`${crbFileTransmissionStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbFileTransmissionStatus');
    });

    it('should create an instance of CrbFileTransmissionStatus', () => {
      cy.get(`[data-cy="submittedFileStatusTypeCode"]`).type('GB primary Maine').should('have.value', 'GB primary Maine');

      cy.get(`[data-cy="submittedFileStatusType"]`).select('CORRECT');

      cy.get(`[data-cy="submittedFileStatusTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbFileTransmissionStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbFileTransmissionStatusPageUrlPattern);
    });
  });
});
