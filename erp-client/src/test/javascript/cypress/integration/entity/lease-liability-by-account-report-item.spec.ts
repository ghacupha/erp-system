import { entityItemSelector } from '../../support/commands';
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('LeaseLiabilityByAccountReportItem e2e test', () => {
  const leaseLiabilityByAccountReportItemPageUrl = '/lease-liability-by-account-report-item';
  const leaseLiabilityByAccountReportItemPageUrlPattern = new RegExp('/lease-liability-by-account-report-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityByAccountReportItemSample = {};

  let leaseLiabilityByAccountReportItem: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-by-account-report-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-by-account-report-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-by-account-report-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (leaseLiabilityByAccountReportItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-by-account-report-items/${leaseLiabilityByAccountReportItem.id}`,
      }).then(() => {
        leaseLiabilityByAccountReportItem = undefined;
      });
    }
  });

  it('LeaseLiabilityByAccountReportItems menu should load LeaseLiabilityByAccountReportItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-by-account-report-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityByAccountReportItem').should('exist');
    cy.url().should('match', leaseLiabilityByAccountReportItemPageUrlPattern);
  });

  describe('LeaseLiabilityByAccountReportItem page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(leaseLiabilityByAccountReportItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiabilityByAccountReportItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityByAccountReportItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityByAccountReportItemPageUrlPattern);
      });
    });
  });
});
