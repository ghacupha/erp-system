import { entityItemSelector } from '../../support/commands';
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('LeaseLiabilityReportItem e2e test', () => {
  const leaseLiabilityReportItemPageUrl = '/lease-liability-report-item';
  const leaseLiabilityReportItemPageUrlPattern = new RegExp('/lease-liability-report-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityReportItemSample = {};

  let leaseLiabilityReportItem: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-report-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-report-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-report-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (leaseLiabilityReportItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-report-items/${leaseLiabilityReportItem.id}`,
      }).then(() => {
        leaseLiabilityReportItem = undefined;
      });
    }
  });

  it('LeaseLiabilityReportItems menu should load LeaseLiabilityReportItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-report-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityReportItem').should('exist');
    cy.url().should('match', leaseLiabilityReportItemPageUrlPattern);
  });

  describe('LeaseLiabilityReportItem page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(leaseLiabilityReportItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiabilityReportItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityReportItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityReportItemPageUrlPattern);
      });
    });
  });
});
