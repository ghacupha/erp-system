import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbAgentServiceTypeComponentsPage,
  CrbAgentServiceTypeDeleteDialog,
  CrbAgentServiceTypeUpdatePage,
} from './crb-agent-service-type.page-object';

const expect = chai.expect;

describe('CrbAgentServiceType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbAgentServiceTypeComponentsPage: CrbAgentServiceTypeComponentsPage;
  let crbAgentServiceTypeUpdatePage: CrbAgentServiceTypeUpdatePage;
  let crbAgentServiceTypeDeleteDialog: CrbAgentServiceTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbAgentServiceTypes', async () => {
    await navBarPage.goToEntity('crb-agent-service-type');
    crbAgentServiceTypeComponentsPage = new CrbAgentServiceTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbAgentServiceTypeComponentsPage.title), 5000);
    expect(await crbAgentServiceTypeComponentsPage.getTitle()).to.eq('Crb Agent Service Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbAgentServiceTypeComponentsPage.entities), ec.visibilityOf(crbAgentServiceTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbAgentServiceType page', async () => {
    await crbAgentServiceTypeComponentsPage.clickOnCreateButton();
    crbAgentServiceTypeUpdatePage = new CrbAgentServiceTypeUpdatePage();
    expect(await crbAgentServiceTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Agent Service Type');
    await crbAgentServiceTypeUpdatePage.cancel();
  });

  it('should create and save CrbAgentServiceTypes', async () => {
    const nbButtonsBeforeCreate = await crbAgentServiceTypeComponentsPage.countDeleteButtons();

    await crbAgentServiceTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbAgentServiceTypeUpdatePage.setAgentServiceTypeCodeInput('agentServiceTypeCode'),
      crbAgentServiceTypeUpdatePage.setAgentServiceTypeDetailsInput('agentServiceTypeDetails'),
    ]);

    await crbAgentServiceTypeUpdatePage.save();
    expect(await crbAgentServiceTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbAgentServiceTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbAgentServiceType', async () => {
    const nbButtonsBeforeDelete = await crbAgentServiceTypeComponentsPage.countDeleteButtons();
    await crbAgentServiceTypeComponentsPage.clickOnLastDeleteButton();

    crbAgentServiceTypeDeleteDialog = new CrbAgentServiceTypeDeleteDialog();
    expect(await crbAgentServiceTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Agent Service Type?');
    await crbAgentServiceTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbAgentServiceTypeComponentsPage.title), 5000);

    expect(await crbAgentServiceTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
