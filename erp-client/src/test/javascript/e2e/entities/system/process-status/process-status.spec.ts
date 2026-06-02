import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ProcessStatusComponentsPage, ProcessStatusDeleteDialog, ProcessStatusUpdatePage } from './process-status.page-object';

const expect = chai.expect;

describe('ProcessStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let processStatusComponentsPage: ProcessStatusComponentsPage;
  let processStatusUpdatePage: ProcessStatusUpdatePage;
  let processStatusDeleteDialog: ProcessStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProcessStatuses', async () => {
    await navBarPage.goToEntity('process-status');
    processStatusComponentsPage = new ProcessStatusComponentsPage();
    await browser.wait(ec.visibilityOf(processStatusComponentsPage.title), 5000);
    expect(await processStatusComponentsPage.getTitle()).to.eq('Process Statuses');
    await browser.wait(
      ec.or(ec.visibilityOf(processStatusComponentsPage.entities), ec.visibilityOf(processStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ProcessStatus page', async () => {
    await processStatusComponentsPage.clickOnCreateButton();
    processStatusUpdatePage = new ProcessStatusUpdatePage();
    expect(await processStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Process Status');
    await processStatusUpdatePage.cancel();
  });

  it('should create and save ProcessStatuses', async () => {
    const nbButtonsBeforeCreate = await processStatusComponentsPage.countDeleteButtons();

    await processStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      processStatusUpdatePage.setStatusCodeInput('statusCode'),
      processStatusUpdatePage.setDescriptionInput('description'),
      // processStatusUpdatePage.placeholderSelectLastOption(),
      // processStatusUpdatePage.parametersSelectLastOption(),
    ]);

    await processStatusUpdatePage.save();
    expect(await processStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await processStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ProcessStatus', async () => {
    const nbButtonsBeforeDelete = await processStatusComponentsPage.countDeleteButtons();
    await processStatusComponentsPage.clickOnLastDeleteButton();

    processStatusDeleteDialog = new ProcessStatusDeleteDialog();
    expect(await processStatusDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Process Status?');
    await processStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(processStatusComponentsPage.title), 5000);

    expect(await processStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
