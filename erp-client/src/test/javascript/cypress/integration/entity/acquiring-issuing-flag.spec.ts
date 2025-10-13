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

describe('AcquiringIssuingFlag e2e test', () => {
  const acquiringIssuingFlagPageUrl = '/acquiring-issuing-flag';
  const acquiringIssuingFlagPageUrlPattern = new RegExp('/acquiring-issuing-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const acquiringIssuingFlagSample = {
    cardAcquiringIssuingFlagCode: 'Specialist deliver Loan',
    cardAcquiringIssuingDescription: 'Consultant',
  };

  let acquiringIssuingFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/acquiring-issuing-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/acquiring-issuing-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/acquiring-issuing-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (acquiringIssuingFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/acquiring-issuing-flags/${acquiringIssuingFlag.id}`,
      }).then(() => {
        acquiringIssuingFlag = undefined;
      });
    }
  });

  it('AcquiringIssuingFlags menu should load AcquiringIssuingFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('acquiring-issuing-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AcquiringIssuingFlag').should('exist');
    cy.url().should('match', acquiringIssuingFlagPageUrlPattern);
  });

  describe('AcquiringIssuingFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(acquiringIssuingFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AcquiringIssuingFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/acquiring-issuing-flag/new$'));
        cy.getEntityCreateUpdateHeading('AcquiringIssuingFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', acquiringIssuingFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/acquiring-issuing-flags',
          body: acquiringIssuingFlagSample,
        }).then(({ body }) => {
          acquiringIssuingFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/acquiring-issuing-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [acquiringIssuingFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(acquiringIssuingFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AcquiringIssuingFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('acquiringIssuingFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', acquiringIssuingFlagPageUrlPattern);
      });

      it('edit button click should load edit AcquiringIssuingFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AcquiringIssuingFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', acquiringIssuingFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of AcquiringIssuingFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('acquiringIssuingFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', acquiringIssuingFlagPageUrlPattern);

        acquiringIssuingFlag = undefined;
      });
    });
  });

  describe('new AcquiringIssuingFlag page', () => {
    beforeEach(() => {
      cy.visit(`${acquiringIssuingFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AcquiringIssuingFlag');
    });

    it('should create an instance of AcquiringIssuingFlag', () => {
      cy.get(`[data-cy="cardAcquiringIssuingFlagCode"]`).type('deliver Ruble').should('have.value', 'deliver Ruble');

      cy.get(`[data-cy="cardAcquiringIssuingDescription"]`).type('Progressive').should('have.value', 'Progressive');

      cy.get(`[data-cy="cardAcquiringIssuingDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        acquiringIssuingFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', acquiringIssuingFlagPageUrlPattern);
    });
  });
});
