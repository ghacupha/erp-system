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

describe('ServiceOutlet e2e test', () => {
  const serviceOutletPageUrl = '/service-outlet';
  const serviceOutletPageUrlPattern = new RegExp('/service-outlet(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const serviceOutletSample = { outletCode: 'Rubber withdrawal', outletName: 'Fantastic' };

  let serviceOutlet: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/service-outlets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/service-outlets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/service-outlets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (serviceOutlet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-outlets/${serviceOutlet.id}`,
      }).then(() => {
        serviceOutlet = undefined;
      });
    }
  });

  it('ServiceOutlets menu should load ServiceOutlets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('service-outlet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServiceOutlet').should('exist');
    cy.url().should('match', serviceOutletPageUrlPattern);
  });

  describe('ServiceOutlet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(serviceOutletPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServiceOutlet page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/service-outlet/new$'));
        cy.getEntityCreateUpdateHeading('ServiceOutlet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceOutletPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/service-outlets',
          body: serviceOutletSample,
        }).then(({ body }) => {
          serviceOutlet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/service-outlets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [serviceOutlet],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(serviceOutletPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ServiceOutlet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('serviceOutlet');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceOutletPageUrlPattern);
      });

      it('edit button click should load edit ServiceOutlet page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceOutlet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceOutletPageUrlPattern);
      });

      it('last delete button click should delete instance of ServiceOutlet', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('serviceOutlet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceOutletPageUrlPattern);

        serviceOutlet = undefined;
      });
    });
  });

  describe('new ServiceOutlet page', () => {
    beforeEach(() => {
      cy.visit(`${serviceOutletPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ServiceOutlet');
    });

    it('should create an instance of ServiceOutlet', () => {
      cy.get(`[data-cy="outletCode"]`).type('programming').should('have.value', 'programming');

      cy.get(`[data-cy="outletName"]`).type('Steel').should('have.value', 'Steel');

      cy.get(`[data-cy="town"]`).type('Kiribati').should('have.value', 'Kiribati');

      cy.get(`[data-cy="parliamentaryConstituency"]`).type('Fish transmitting').should('have.value', 'Fish transmitting');

      cy.get(`[data-cy="gpsCoordinates"]`).type('invoice connecting').should('have.value', 'invoice connecting');

      cy.get(`[data-cy="outletOpeningDate"]`).type('2022-02-28').should('have.value', '2022-02-28');

      cy.get(`[data-cy="regulatorApprovalDate"]`).type('2022-02-28').should('have.value', '2022-02-28');

      cy.get(`[data-cy="outletClosureDate"]`).type('2022-02-28').should('have.value', '2022-02-28');

      cy.get(`[data-cy="dateLastModified"]`).type('2022-03-01').should('have.value', '2022-03-01');

      cy.get(`[data-cy="licenseFeePayable"]`).type('32805').should('have.value', '32805');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        serviceOutlet = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', serviceOutletPageUrlPattern);
    });
  });
});
