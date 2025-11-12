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

describe('TaxRule e2e test', () => {
  const taxRulePageUrl = '/tax-rule';
  const taxRulePageUrlPattern = new RegExp('/tax-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const taxRuleSample = {};

  let taxRule: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-rules/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taxRule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-rules/${taxRule.id}`,
      }).then(() => {
        taxRule = undefined;
      });
    }
  });

  it('TaxRules menu should load TaxRules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-rule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxRule').should('exist');
    cy.url().should('match', taxRulePageUrlPattern);
  });

  describe('TaxRule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxRulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxRule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/tax-rule/new$'));
        cy.getEntityCreateUpdateHeading('TaxRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxRulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-rules',
          body: taxRuleSample,
        }).then(({ body }) => {
          taxRule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [taxRule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxRulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxRule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxRule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxRulePageUrlPattern);
      });

      it('edit button click should load edit TaxRule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxRulePageUrlPattern);
      });

      it('last delete button click should delete instance of TaxRule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('taxRule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxRulePageUrlPattern);

        taxRule = undefined;
      });
    });
  });

  describe('new TaxRule page', () => {
    beforeEach(() => {
      cy.visit(`${taxRulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TaxRule');
    });

    it('should create an instance of TaxRule', () => {
      cy.get(`[data-cy="telcoExciseDuty"]`).type('40474').should('have.value', '40474');

      cy.get(`[data-cy="valueAddedTax"]`).type('81457').should('have.value', '81457');

      cy.get(`[data-cy="withholdingVAT"]`).type('40594').should('have.value', '40594');

      cy.get(`[data-cy="withholdingTaxConsultancy"]`).type('63354').should('have.value', '63354');

      cy.get(`[data-cy="withholdingTaxRent"]`).type('38661').should('have.value', '38661');

      cy.get(`[data-cy="cateringLevy"]`).type('87370').should('have.value', '87370');

      cy.get(`[data-cy="serviceCharge"]`).type('93722').should('have.value', '93722');

      cy.get(`[data-cy="withholdingTaxImportedService"]`).type('45663').should('have.value', '45663');

      cy.get(`[data-cy="fileUploadToken"]`).type('port Human deposit').should('have.value', 'port Human deposit');

      cy.get(`[data-cy="compilationToken"]`).type('Yuan Berkshire').should('have.value', 'Yuan Berkshire');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        taxRule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', taxRulePageUrlPattern);
    });
  });
});
