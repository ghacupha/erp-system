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

describe('AgriculturalEnterpriseActivityType e2e test', () => {
  const agriculturalEnterpriseActivityTypePageUrl = '/agricultural-enterprise-activity-type';
  const agriculturalEnterpriseActivityTypePageUrlPattern = new RegExp('/agricultural-enterprise-activity-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const agriculturalEnterpriseActivityTypeSample = {
    agriculturalEnterpriseActivityTypeCode: 'Uganda',
    agriculturalEnterpriseActivityType: 'Cheese Metrics',
  };

  let agriculturalEnterpriseActivityType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agricultural-enterprise-activity-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agricultural-enterprise-activity-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agricultural-enterprise-activity-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agriculturalEnterpriseActivityType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agricultural-enterprise-activity-types/${agriculturalEnterpriseActivityType.id}`,
      }).then(() => {
        agriculturalEnterpriseActivityType = undefined;
      });
    }
  });

  it('AgriculturalEnterpriseActivityTypes menu should load AgriculturalEnterpriseActivityTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agricultural-enterprise-activity-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgriculturalEnterpriseActivityType').should('exist');
    cy.url().should('match', agriculturalEnterpriseActivityTypePageUrlPattern);
  });

  describe('AgriculturalEnterpriseActivityType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agriculturalEnterpriseActivityTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgriculturalEnterpriseActivityType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/agricultural-enterprise-activity-type/new$'));
        cy.getEntityCreateUpdateHeading('AgriculturalEnterpriseActivityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agriculturalEnterpriseActivityTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agricultural-enterprise-activity-types',
          body: agriculturalEnterpriseActivityTypeSample,
        }).then(({ body }) => {
          agriculturalEnterpriseActivityType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agricultural-enterprise-activity-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agriculturalEnterpriseActivityType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(agriculturalEnterpriseActivityTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgriculturalEnterpriseActivityType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agriculturalEnterpriseActivityType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agriculturalEnterpriseActivityTypePageUrlPattern);
      });

      it('edit button click should load edit AgriculturalEnterpriseActivityType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgriculturalEnterpriseActivityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agriculturalEnterpriseActivityTypePageUrlPattern);
      });

      it('last delete button click should delete instance of AgriculturalEnterpriseActivityType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('agriculturalEnterpriseActivityType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', agriculturalEnterpriseActivityTypePageUrlPattern);

        agriculturalEnterpriseActivityType = undefined;
      });
    });
  });

  describe('new AgriculturalEnterpriseActivityType page', () => {
    beforeEach(() => {
      cy.visit(`${agriculturalEnterpriseActivityTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AgriculturalEnterpriseActivityType');
    });

    it('should create an instance of AgriculturalEnterpriseActivityType', () => {
      cy.get(`[data-cy="agriculturalEnterpriseActivityTypeCode"]`).type('state').should('have.value', 'state');

      cy.get(`[data-cy="agriculturalEnterpriseActivityType"]`).type('Borders discrete').should('have.value', 'Borders discrete');

      cy.get(`[data-cy="agriculturalEnterpriseActivityTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        agriculturalEnterpriseActivityType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', agriculturalEnterpriseActivityTypePageUrlPattern);
    });
  });
});
