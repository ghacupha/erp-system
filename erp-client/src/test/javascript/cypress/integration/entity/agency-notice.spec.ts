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

describe('AgencyNotice e2e test', () => {
  const agencyNoticePageUrl = '/agency-notice';
  const agencyNoticePageUrlPattern = new RegExp('/agency-notice(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const agencyNoticeSample = { referenceNumber: 'communities Regional needs-based', assessmentAmount: 64325, agencyStatus: 'NOT_CLEARED' };

  let agencyNotice: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agency-notices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agency-notices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agency-notices/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agencyNotice) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agency-notices/${agencyNotice.id}`,
      }).then(() => {
        agencyNotice = undefined;
      });
    }
  });

  it('AgencyNotices menu should load AgencyNotices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agency-notice');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgencyNotice').should('exist');
    cy.url().should('match', agencyNoticePageUrlPattern);
  });

  describe('AgencyNotice page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agencyNoticePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgencyNotice page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/agency-notice/new$'));
        cy.getEntityCreateUpdateHeading('AgencyNotice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyNoticePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agency-notices',
          body: agencyNoticeSample,
        }).then(({ body }) => {
          agencyNotice = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agency-notices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agencyNotice],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(agencyNoticePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgencyNotice page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agencyNotice');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyNoticePageUrlPattern);
      });

      it('edit button click should load edit AgencyNotice page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgencyNotice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyNoticePageUrlPattern);
      });

      it('last delete button click should delete instance of AgencyNotice', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('agencyNotice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyNoticePageUrlPattern);

        agencyNotice = undefined;
      });
    });
  });

  describe('new AgencyNotice page', () => {
    beforeEach(() => {
      cy.visit(`${agencyNoticePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AgencyNotice');
    });

    it('should create an instance of AgencyNotice', () => {
      cy.get(`[data-cy="referenceNumber"]`).type('Architect Music').should('have.value', 'Architect Music');

      cy.get(`[data-cy="referenceDate"]`).type('2022-02-03').should('have.value', '2022-02-03');

      cy.get(`[data-cy="assessmentAmount"]`).type('80085').should('have.value', '80085');

      cy.get(`[data-cy="agencyStatus"]`).select('CLEARED');

      cy.setFieldImageAsBytesOfEntity('assessmentNotice', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        agencyNotice = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', agencyNoticePageUrlPattern);
    });
  });
});
