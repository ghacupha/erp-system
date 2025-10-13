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

describe('CrbReportViewBand e2e test', () => {
  const crbReportViewBandPageUrl = '/crb-report-view-band';
  const crbReportViewBandPageUrlPattern = new RegExp('/crb-report-view-band(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbReportViewBandSample = { reportViewCode: 'multimedia', reportViewCategory: 'end-to-end' };

  let crbReportViewBand: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-report-view-bands+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-report-view-bands').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-report-view-bands/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbReportViewBand) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-report-view-bands/${crbReportViewBand.id}`,
      }).then(() => {
        crbReportViewBand = undefined;
      });
    }
  });

  it('CrbReportViewBands menu should load CrbReportViewBands page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-report-view-band');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbReportViewBand').should('exist');
    cy.url().should('match', crbReportViewBandPageUrlPattern);
  });

  describe('CrbReportViewBand page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbReportViewBandPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbReportViewBand page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-report-view-band/new$'));
        cy.getEntityCreateUpdateHeading('CrbReportViewBand');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportViewBandPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-report-view-bands',
          body: crbReportViewBandSample,
        }).then(({ body }) => {
          crbReportViewBand = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-report-view-bands+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbReportViewBand],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbReportViewBandPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbReportViewBand page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbReportViewBand');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportViewBandPageUrlPattern);
      });

      it('edit button click should load edit CrbReportViewBand page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbReportViewBand');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportViewBandPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbReportViewBand', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbReportViewBand').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbReportViewBandPageUrlPattern);

        crbReportViewBand = undefined;
      });
    });
  });

  describe('new CrbReportViewBand page', () => {
    beforeEach(() => {
      cy.visit(`${crbReportViewBandPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbReportViewBand');
    });

    it('should create an instance of CrbReportViewBand', () => {
      cy.get(`[data-cy="reportViewCode"]`).type('Money open-source').should('have.value', 'Money open-source');

      cy.get(`[data-cy="reportViewCategory"]`).type('Trace Turkmenistan').should('have.value', 'Trace Turkmenistan');

      cy.get(`[data-cy="reportViewCategoryDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbReportViewBand = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbReportViewBandPageUrlPattern);
    });
  });
});
