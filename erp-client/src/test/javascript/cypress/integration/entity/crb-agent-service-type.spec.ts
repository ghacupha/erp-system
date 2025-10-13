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

describe('CrbAgentServiceType e2e test', () => {
  const crbAgentServiceTypePageUrl = '/crb-agent-service-type';
  const crbAgentServiceTypePageUrlPattern = new RegExp('/crb-agent-service-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbAgentServiceTypeSample = { agentServiceTypeCode: 'Generic Brand' };

  let crbAgentServiceType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-agent-service-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-agent-service-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-agent-service-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbAgentServiceType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-agent-service-types/${crbAgentServiceType.id}`,
      }).then(() => {
        crbAgentServiceType = undefined;
      });
    }
  });

  it('CrbAgentServiceTypes menu should load CrbAgentServiceTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-agent-service-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbAgentServiceType').should('exist');
    cy.url().should('match', crbAgentServiceTypePageUrlPattern);
  });

  describe('CrbAgentServiceType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbAgentServiceTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbAgentServiceType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-agent-service-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbAgentServiceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgentServiceTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-agent-service-types',
          body: crbAgentServiceTypeSample,
        }).then(({ body }) => {
          crbAgentServiceType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-agent-service-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbAgentServiceType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbAgentServiceTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbAgentServiceType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbAgentServiceType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgentServiceTypePageUrlPattern);
      });

      it('edit button click should load edit CrbAgentServiceType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbAgentServiceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgentServiceTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbAgentServiceType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbAgentServiceType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAgentServiceTypePageUrlPattern);

        crbAgentServiceType = undefined;
      });
    });
  });

  describe('new CrbAgentServiceType page', () => {
    beforeEach(() => {
      cy.visit(`${crbAgentServiceTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbAgentServiceType');
    });

    it('should create an instance of CrbAgentServiceType', () => {
      cy.get(`[data-cy="agentServiceTypeCode"]`).type('Lek Flat Account').should('have.value', 'Lek Flat Account');

      cy.get(`[data-cy="agentServiceTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbAgentServiceType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbAgentServiceTypePageUrlPattern);
    });
  });
});
