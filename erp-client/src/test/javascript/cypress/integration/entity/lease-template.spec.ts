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

describe('LeaseTemplate e2e test', () => {
  const leaseTemplatePageUrl = '/lease-template';
  const leaseTemplatePageUrlPattern = new RegExp('/lease-template(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseTemplateSample = { templateTitle: 'compress Sports' };

  let leaseTemplate: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-templates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-templates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-templates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (leaseTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-templates/${leaseTemplate.id}`,
      }).then(() => {
        leaseTemplate = undefined;
      });
    }
  });

  it('LeaseTemplates menu should load LeaseTemplates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-template');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseTemplate').should('exist');
    cy.url().should('match', leaseTemplatePageUrlPattern);
  });

  describe('LeaseTemplate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseTemplatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseTemplate page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-template/new$'));
        cy.getEntityCreateUpdateHeading('LeaseTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseTemplatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-templates',
          body: leaseTemplateSample,
        }).then(({ body }) => {
          leaseTemplate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-templates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseTemplate],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseTemplatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LeaseTemplate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseTemplate');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseTemplatePageUrlPattern);
      });

      it('edit button click should load edit LeaseTemplate page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseTemplatePageUrlPattern);
      });

      it('last delete button click should delete instance of LeaseTemplate', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseTemplate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseTemplatePageUrlPattern);

        leaseTemplate = undefined;
      });
    });
  });

  describe('new LeaseTemplate page', () => {
    beforeEach(() => {
      cy.visit(`${leaseTemplatePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseTemplate');
    });

    it('should create an instance of LeaseTemplate', () => {
      cy.get(`[data-cy="templateTitle"]`).type('Borders').should('have.value', 'Borders');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseTemplate = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseTemplatePageUrlPattern);
    });
  });
});
