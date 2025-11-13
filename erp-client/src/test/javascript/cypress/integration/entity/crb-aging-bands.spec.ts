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

describe('CrbAgingBands e2e test', () => {
  const crbAgingBandsPageUrl = '/crb-aging-bands';
  const crbAgingBandsPageUrlPattern = new RegExp('/crb-aging-bands(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbAgingBandsSample = {
    agingBandCategoryCode: 'Analyst deliverables Computers',
    agingBandCategory: 'Forward',
    agingBandCategoryDetails: 'asymmetric protocol',
  };

  let crbAgingBands: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-aging-bands+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-aging-bands').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-aging-bands/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbAgingBands) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-aging-bands/${crbAgingBands.id}`,
      }).then(() => {
        crbAgingBands = undefined;
      });
    }
  });

  it('CrbAgingBands menu should load CrbAgingBands page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-aging-bands');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbAgingBands').should('exist');
    cy.url().should('match', crbAgingBandsPageUrlPattern);
  });

  describe('CrbAgingBands page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbAgingBandsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbAgingBands page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-aging-bands/new$'));
        cy.getEntityCreateUpdateHeading('CrbAgingBands');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgingBandsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-aging-bands',
          body: crbAgingBandsSample,
        }).then(({ body }) => {
          crbAgingBands = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-aging-bands+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbAgingBands],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbAgingBandsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbAgingBands page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbAgingBands');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgingBandsPageUrlPattern);
      });

      it('edit button click should load edit CrbAgingBands page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbAgingBands');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgingBandsPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbAgingBands', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbAgingBands').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgingBandsPageUrlPattern);

        crbAgingBands = undefined;
      });
    });
  });

  describe('new CrbAgingBands page', () => {
    beforeEach(() => {
      cy.visit(`${crbAgingBandsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbAgingBands');
    });

    it('should create an instance of CrbAgingBands', () => {
      cy.get(`[data-cy="agingBandCategoryCode"]`).type('Orchestrator').should('have.value', 'Orchestrator');

      cy.get(`[data-cy="agingBandCategory"]`)
        .type('withdrawal transmitter Accountability')
        .should('have.value', 'withdrawal transmitter Accountability');

      cy.get(`[data-cy="agingBandCategoryDetails"]`).type('visualize').should('have.value', 'visualize');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbAgingBands = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbAgingBandsPageUrlPattern);
    });
  });
});
