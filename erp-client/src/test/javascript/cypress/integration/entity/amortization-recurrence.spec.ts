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

describe('AmortizationRecurrence e2e test', () => {
  const amortizationRecurrencePageUrl = '/amortization-recurrence';
  const amortizationRecurrencePageUrlPattern = new RegExp('/amortization-recurrence(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const amortizationRecurrenceSample = {
    firstAmortizationDate: '2022-08-02',
    amortizationFrequency: 'TRIMESTERS',
    numberOfRecurrences: 45323,
    timeOfInstallation: '2022-08-02T00:22:08.449Z',
    recurrenceGuid: 'eb5152b9-7f5f-406f-8a0b-7c4e7d17e544',
    prepaymentAccountGuid: '886bb355-2be9-4dbb-95ab-e1b15c1d6c98',
  };

  let amortizationRecurrence: any;
  let depreciationMethod: any;
  let prepaymentAccount: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/depreciation-methods',
      body: { depreciationMethodName: 'Plaza', description: 'Investor Rial', remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' },
    }).then(({ body }) => {
      depreciationMethod = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/prepayment-accounts',
      body: {
        catalogueNumber: 'synthesizing Cambridgeshire turquoise',
        recognitionDate: '2022-04-30',
        particulars: 'Profit-focused payment bypass',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        prepaymentAmount: 66512,
        prepaymentGuid: '15bf5acc-0e6c-45e4-9c1c-715bad5b9169',
      },
    }).then(({ body }) => {
      prepaymentAccount = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/amortization-recurrences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/amortization-recurrences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/amortization-recurrences/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/prepayment-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/depreciation-methods', {
      statusCode: 200,
      body: [depreciationMethod],
    });

    cy.intercept('GET', '/api/prepayment-accounts', {
      statusCode: 200,
      body: [prepaymentAccount],
    });
  });

  afterEach(() => {
    if (amortizationRecurrence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amortization-recurrences/${amortizationRecurrence.id}`,
      }).then(() => {
        amortizationRecurrence = undefined;
      });
    }
  });

  afterEach(() => {
    if (depreciationMethod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-methods/${depreciationMethod.id}`,
      }).then(() => {
        depreciationMethod = undefined;
      });
    }
    if (prepaymentAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-accounts/${prepaymentAccount.id}`,
      }).then(() => {
        prepaymentAccount = undefined;
      });
    }
  });

  it('AmortizationRecurrences menu should load AmortizationRecurrences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('amortization-recurrence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AmortizationRecurrence').should('exist');
    cy.url().should('match', amortizationRecurrencePageUrlPattern);
  });

  describe('AmortizationRecurrence page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(amortizationRecurrencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AmortizationRecurrence page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/amortization-recurrence/new$'));
        cy.getEntityCreateUpdateHeading('AmortizationRecurrence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationRecurrencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/amortization-recurrences',

          body: {
            ...amortizationRecurrenceSample,
            depreciationMethod: depreciationMethod,
            prepaymentAccount: prepaymentAccount,
          },
        }).then(({ body }) => {
          amortizationRecurrence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/amortization-recurrences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [amortizationRecurrence],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(amortizationRecurrencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AmortizationRecurrence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('amortizationRecurrence');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationRecurrencePageUrlPattern);
      });

      it('edit button click should load edit AmortizationRecurrence page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AmortizationRecurrence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationRecurrencePageUrlPattern);
      });

      it('last delete button click should delete instance of AmortizationRecurrence', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('amortizationRecurrence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationRecurrencePageUrlPattern);

        amortizationRecurrence = undefined;
      });
    });
  });

  describe('new AmortizationRecurrence page', () => {
    beforeEach(() => {
      cy.visit(`${amortizationRecurrencePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AmortizationRecurrence');
    });

    it('should create an instance of AmortizationRecurrence', () => {
      cy.get(`[data-cy="firstAmortizationDate"]`).type('2022-08-02').should('have.value', '2022-08-02');

      cy.get(`[data-cy="amortizationFrequency"]`).select('BI_MONTHLY');

      cy.get(`[data-cy="numberOfRecurrences"]`).type('42986').should('have.value', '42986');

      cy.setFieldImageAsBytesOfEntity('notes', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="particulars"]`).type('Stream plum').should('have.value', 'Stream plum');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click().should('be.checked');

      cy.get(`[data-cy="isOverWritten"]`).should('not.be.checked');
      cy.get(`[data-cy="isOverWritten"]`).click().should('be.checked');

      cy.get(`[data-cy="timeOfInstallation"]`).type('2022-08-01T19:04').should('have.value', '2022-08-01T19:04');

      cy.get(`[data-cy="recurrenceGuid"]`)
        .type('02ef87c1-8cbd-4987-9ed7-4ad8d28fbb06')
        .invoke('val')
        .should('match', new RegExp('02ef87c1-8cbd-4987-9ed7-4ad8d28fbb06'));

      cy.get(`[data-cy="prepaymentAccountGuid"]`)
        .type('dcde04e1-d0d5-4ed2-809f-81d8ba22f2c6')
        .invoke('val')
        .should('match', new RegExp('dcde04e1-d0d5-4ed2-809f-81d8ba22f2c6'));

      cy.get(`[data-cy="depreciationMethod"]`).select(1);
      cy.get(`[data-cy="prepaymentAccount"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        amortizationRecurrence = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', amortizationRecurrencePageUrlPattern);
    });
  });
});
