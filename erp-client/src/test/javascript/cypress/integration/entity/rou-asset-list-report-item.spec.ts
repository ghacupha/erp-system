import { entityItemSelector } from '../../support/commands';
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('RouAssetListReportItem e2e test', () => {
  const rouAssetListReportItemPageUrl = '/rou-asset-list-report-item';
  const rouAssetListReportItemPageUrlPattern = new RegExp('/rou-asset-list-report-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouAssetListReportItemSample = {};

  let rouAssetListReportItem: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-asset-list-report-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-asset-list-report-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-asset-list-report-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rouAssetListReportItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-asset-list-report-items/${rouAssetListReportItem.id}`,
      }).then(() => {
        rouAssetListReportItem = undefined;
      });
    }
  });

  it('RouAssetListReportItems menu should load RouAssetListReportItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-asset-list-report-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouAssetListReportItem').should('exist');
    cy.url().should('match', rouAssetListReportItemPageUrlPattern);
  });

  describe('RouAssetListReportItem page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(rouAssetListReportItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RouAssetListReportItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouAssetListReportItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetListReportItemPageUrlPattern);
      });
    });
  });
});
