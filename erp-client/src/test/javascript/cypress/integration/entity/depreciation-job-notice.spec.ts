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

describe('DepreciationJobNotice e2e test', () => {
  const depreciationJobNoticePageUrl = '/depreciation-job-notice';
  const depreciationJobNoticePageUrlPattern = new RegExp('/depreciation-job-notice(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const depreciationJobNoticeSample = {
    eventNarrative: 'Senior',
    eventTimeStamp: '2023-08-16T12:43:33.904Z',
    depreciationNoticeStatus: 'INFO',
  };

  let depreciationJobNotice: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/depreciation-job-notices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/depreciation-job-notices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/depreciation-job-notices/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (depreciationJobNotice) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-job-notices/${depreciationJobNotice.id}`,
      }).then(() => {
        depreciationJobNotice = undefined;
      });
    }
  });

  it('DepreciationJobNotices menu should load DepreciationJobNotices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('depreciation-job-notice');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepreciationJobNotice').should('exist');
    cy.url().should('match', depreciationJobNoticePageUrlPattern);
  });

  describe('DepreciationJobNotice page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(depreciationJobNoticePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepreciationJobNotice page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/depreciation-job-notice/new$'));
        cy.getEntityCreateUpdateHeading('DepreciationJobNotice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobNoticePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/depreciation-job-notices',
          body: depreciationJobNoticeSample,
        }).then(({ body }) => {
          depreciationJobNotice = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/depreciation-job-notices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [depreciationJobNotice],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(depreciationJobNoticePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DepreciationJobNotice page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('depreciationJobNotice');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobNoticePageUrlPattern);
      });

      it('edit button click should load edit DepreciationJobNotice page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepreciationJobNotice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobNoticePageUrlPattern);
      });

      it('last delete button click should delete instance of DepreciationJobNotice', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('depreciationJobNotice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationJobNoticePageUrlPattern);

        depreciationJobNotice = undefined;
      });
    });
  });

  describe('new DepreciationJobNotice page', () => {
    beforeEach(() => {
      cy.visit(`${depreciationJobNoticePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DepreciationJobNotice');
    });

    it('should create an instance of DepreciationJobNotice', () => {
      cy.get(`[data-cy="eventNarrative"]`).type('Nebraska payment').should('have.value', 'Nebraska payment');

      cy.get(`[data-cy="eventTimeStamp"]`).type('2023-08-16T12:37').should('have.value', '2023-08-16T12:37');

      cy.get(`[data-cy="depreciationNoticeStatus"]`).select('INFO');

      cy.get(`[data-cy="sourceModule"]`).type('Pizza Lari Tala').should('have.value', 'Pizza Lari Tala');

      cy.get(`[data-cy="sourceEntity"]`).type('Michigan').should('have.value', 'Michigan');

      cy.get(`[data-cy="errorCode"]`).type('Iowa').should('have.value', 'Iowa');

      cy.get(`[data-cy="errorMessage"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="userAction"]`).type('system Dynamic system').should('have.value', 'system Dynamic system');

      cy.get(`[data-cy="technicalDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        depreciationJobNotice = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', depreciationJobNoticePageUrlPattern);
    });
  });
});
