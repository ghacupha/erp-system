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

describe('AmortizationSequence e2e test', () => {
  const amortizationSequencePageUrl = '/amortization-sequence';
  const amortizationSequencePageUrlPattern = new RegExp('/amortization-sequence(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const amortizationSequenceSample = {
    prepaymentAccountGuid: 'ae26cfca-295b-421b-81d6-361e7418f837',
    recurrenceGuid: 'dd82278f-eae5-48ab-b264-7251e3272ca9',
    sequenceNumber: 15169,
    currentAmortizationDate: '2022-08-01',
    isCommencementSequence: false,
    isTerminalSequence: true,
    amortizationAmount: 50987,
    sequenceGuid: '146a58a3-4714-4ba0-9514-939d0f629458',
  };

  let amortizationSequence: any;
  //let prepaymentAccount: any;
  //let amortizationRecurrence: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/prepayment-accounts',
      body: {"catalogueNumber":"navigate hack","recognitionDate":"2022-05-01","particulars":"navigating","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","prepaymentAmount":15832,"prepaymentGuid":"e445692b-3244-4a7e-9f3d-e4306f33927f"},
    }).then(({ body }) => {
      prepaymentAccount = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/amortization-recurrences',
      body: {"firstAmortizationDate":"2022-08-02","amortizationFrequency":"QUARTERLY","numberOfRecurrences":87427,"notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","particulars":"Bedfordshire Checking redefine","isActive":true,"isOverWritten":false,"timeOfInstallation":"2022-08-02T00:44:49.524Z","recurrenceGuid":"51ac77e3-0792-42b2-910b-308a66582c47","prepaymentAccountGuid":"df9c7f1f-f27d-4a30-9f88-f09ff4f301bb"},
    }).then(({ body }) => {
      amortizationRecurrence = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/amortization-sequences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/amortization-sequences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/amortization-sequences/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/prepayment-accounts', {
      statusCode: 200,
      body: [prepaymentAccount],
    });

    cy.intercept('GET', '/api/amortization-recurrences', {
      statusCode: 200,
      body: [amortizationRecurrence],
    });

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

  });
   */

  afterEach(() => {
    if (amortizationSequence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amortization-sequences/${amortizationSequence.id}`,
      }).then(() => {
        amortizationSequence = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (prepaymentAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-accounts/${prepaymentAccount.id}`,
      }).then(() => {
        prepaymentAccount = undefined;
      });
    }
    if (amortizationRecurrence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amortization-recurrences/${amortizationRecurrence.id}`,
      }).then(() => {
        amortizationRecurrence = undefined;
      });
    }
  });
   */

  it('AmortizationSequences menu should load AmortizationSequences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('amortization-sequence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AmortizationSequence').should('exist');
    cy.url().should('match', amortizationSequencePageUrlPattern);
  });

  describe('AmortizationSequence page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(amortizationSequencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AmortizationSequence page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/amortization-sequence/new$'));
        cy.getEntityCreateUpdateHeading('AmortizationSequence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationSequencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/amortization-sequences',
  
          body: {
            ...amortizationSequenceSample,
            prepaymentAccount: prepaymentAccount,
            amortizationRecurrence: amortizationRecurrence,
          },
        }).then(({ body }) => {
          amortizationSequence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/amortization-sequences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [amortizationSequence],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(amortizationSequencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(amortizationSequencePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AmortizationSequence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('amortizationSequence');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationSequencePageUrlPattern);
      });

      it('edit button click should load edit AmortizationSequence page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AmortizationSequence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationSequencePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AmortizationSequence', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('amortizationSequence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationSequencePageUrlPattern);

        amortizationSequence = undefined;
      });
    });
  });

  describe('new AmortizationSequence page', () => {
    beforeEach(() => {
      cy.visit(`${amortizationSequencePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AmortizationSequence');
    });

    it.skip('should create an instance of AmortizationSequence', () => {
      cy.get(`[data-cy="prepaymentAccountGuid"]`)
        .type('91f08c88-cf71-4266-b4e3-fdc120a8b925')
        .invoke('val')
        .should('match', new RegExp('91f08c88-cf71-4266-b4e3-fdc120a8b925'));

      cy.get(`[data-cy="recurrenceGuid"]`)
        .type('a4ab789f-c1c0-46fe-94bd-447f34b4ba84')
        .invoke('val')
        .should('match', new RegExp('a4ab789f-c1c0-46fe-94bd-447f34b4ba84'));

      cy.get(`[data-cy="sequenceNumber"]`).type('90751').should('have.value', '90751');

      cy.get(`[data-cy="particulars"]`).type('XML circuit input').should('have.value', 'XML circuit input');

      cy.get(`[data-cy="currentAmortizationDate"]`).type('2022-08-01').should('have.value', '2022-08-01');

      cy.get(`[data-cy="previousAmortizationDate"]`).type('2022-08-02').should('have.value', '2022-08-02');

      cy.get(`[data-cy="nextAmortizationDate"]`).type('2022-08-01').should('have.value', '2022-08-01');

      cy.get(`[data-cy="isCommencementSequence"]`).should('not.be.checked');
      cy.get(`[data-cy="isCommencementSequence"]`).click().should('be.checked');

      cy.get(`[data-cy="isTerminalSequence"]`).should('not.be.checked');
      cy.get(`[data-cy="isTerminalSequence"]`).click().should('be.checked');

      cy.get(`[data-cy="amortizationAmount"]`).type('63702').should('have.value', '63702');

      cy.get(`[data-cy="sequenceGuid"]`)
        .type('ba9d0cb1-375c-446a-aa41-fb16064f6083')
        .invoke('val')
        .should('match', new RegExp('ba9d0cb1-375c-446a-aa41-fb16064f6083'));

      cy.get(`[data-cy="prepaymentAccount"]`).select(1);
      cy.get(`[data-cy="amortizationRecurrence"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        amortizationSequence = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', amortizationSequencePageUrlPattern);
    });
  });
});
