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

describe('LeaseLiabilityCompilation e2e test', () => {
  const leaseLiabilityCompilationPageUrl = '/lease-liability-compilation';
  const leaseLiabilityCompilationPageUrlPattern = new RegExp('/lease-liability-compilation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityCompilationSample = { requestId: '716eb130-7e8d-426a-9bde-153c2b536bd5', timeOfRequest: '2024-06-18T03:59:26.539Z' };

  let leaseLiabilityCompilation: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-compilations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-compilations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-compilations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (leaseLiabilityCompilation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-compilations/${leaseLiabilityCompilation.id}`,
      }).then(() => {
        leaseLiabilityCompilation = undefined;
      });
    }
  });

  it('LeaseLiabilityCompilations menu should load LeaseLiabilityCompilations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-compilation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityCompilation').should('exist');
    cy.url().should('match', leaseLiabilityCompilationPageUrlPattern);
  });

  describe('LeaseLiabilityCompilation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseLiabilityCompilationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseLiabilityCompilation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-liability-compilation/new$'));
        cy.getEntityCreateUpdateHeading('LeaseLiabilityCompilation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityCompilationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-liability-compilations',
          body: leaseLiabilityCompilationSample,
        }).then(({ body }) => {
          leaseLiabilityCompilation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-liability-compilations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseLiabilityCompilation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseLiabilityCompilationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LeaseLiabilityCompilation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityCompilation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityCompilationPageUrlPattern);
      });

      it('edit button click should load edit LeaseLiabilityCompilation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseLiabilityCompilation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityCompilationPageUrlPattern);
      });

      it('last delete button click should delete instance of LeaseLiabilityCompilation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseLiabilityCompilation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityCompilationPageUrlPattern);

        leaseLiabilityCompilation = undefined;
      });
    });
  });

  describe('new LeaseLiabilityCompilation page', () => {
    beforeEach(() => {
      cy.visit(`${leaseLiabilityCompilationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseLiabilityCompilation');
    });

    it('should create an instance of LeaseLiabilityCompilation', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('7662b1b2-8639-49da-bcf3-19cd95738040')
        .invoke('val')
        .should('match', new RegExp('7662b1b2-8639-49da-bcf3-19cd95738040'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-06-17T17:30').should('have.value', '2024-06-17T17:30');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseLiabilityCompilation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseLiabilityCompilationPageUrlPattern);
    });
  });
});
