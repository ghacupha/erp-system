import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SystemModuleComponentsPage, SystemModuleDeleteDialog, SystemModuleUpdatePage } from './system-module.page-object';

const expect = chai.expect;

describe('SystemModule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let systemModuleComponentsPage: SystemModuleComponentsPage;
  let systemModuleUpdatePage: SystemModuleUpdatePage;
  let systemModuleDeleteDialog: SystemModuleDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SystemModules', async () => {
    await navBarPage.goToEntity('system-module');
    systemModuleComponentsPage = new SystemModuleComponentsPage();
    await browser.wait(ec.visibilityOf(systemModuleComponentsPage.title), 5000);
    expect(await systemModuleComponentsPage.getTitle()).to.eq('System Modules');
    await browser.wait(
      ec.or(ec.visibilityOf(systemModuleComponentsPage.entities), ec.visibilityOf(systemModuleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SystemModule page', async () => {
    await systemModuleComponentsPage.clickOnCreateButton();
    systemModuleUpdatePage = new SystemModuleUpdatePage();
    expect(await systemModuleUpdatePage.getPageTitle()).to.eq('Create or edit a System Module');
    await systemModuleUpdatePage.cancel();
  });

  it('should create and save SystemModules', async () => {
    const nbButtonsBeforeCreate = await systemModuleComponentsPage.countDeleteButtons();

    await systemModuleComponentsPage.clickOnCreateButton();

    await promise.all([systemModuleUpdatePage.setModuleNameInput('moduleName')]);

    await systemModuleUpdatePage.save();
    expect(await systemModuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await systemModuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SystemModule', async () => {
    const nbButtonsBeforeDelete = await systemModuleComponentsPage.countDeleteButtons();
    await systemModuleComponentsPage.clickOnLastDeleteButton();

    systemModuleDeleteDialog = new SystemModuleDeleteDialog();
    expect(await systemModuleDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this System Module?');
    await systemModuleDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(systemModuleComponentsPage.title), 5000);

    expect(await systemModuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
