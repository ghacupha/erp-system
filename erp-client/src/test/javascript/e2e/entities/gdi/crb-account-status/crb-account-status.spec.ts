import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CrbAccountStatusComponentsPage, CrbAccountStatusDeleteDialog, CrbAccountStatusUpdatePage } from './crb-account-status.page-object';

const expect = chai.expect;

describe('CrbAccountStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbAccountStatusComponentsPage: CrbAccountStatusComponentsPage;
  let crbAccountStatusUpdatePage: CrbAccountStatusUpdatePage;
  let crbAccountStatusDeleteDialog: CrbAccountStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbAccountStatuses', async () => {
    await navBarPage.goToEntity('crb-account-status');
    crbAccountStatusComponentsPage = new CrbAccountStatusComponentsPage();
    await browser.wait(ec.visibilityOf(crbAccountStatusComponentsPage.title), 5000);
    expect(await crbAccountStatusComponentsPage.getTitle()).to.eq('Crb Account Statuses');
    await browser.wait(
      ec.or(ec.visibilityOf(crbAccountStatusComponentsPage.entities), ec.visibilityOf(crbAccountStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbAccountStatus page', async () => {
    await crbAccountStatusComponentsPage.clickOnCreateButton();
    crbAccountStatusUpdatePage = new CrbAccountStatusUpdatePage();
    expect(await crbAccountStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Account Status');
    await crbAccountStatusUpdatePage.cancel();
  });

  it('should create and save CrbAccountStatuses', async () => {
    const nbButtonsBeforeCreate = await crbAccountStatusComponentsPage.countDeleteButtons();

    await crbAccountStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      crbAccountStatusUpdatePage.setAccountStatusTypeCodeInput('accountStatusTypeCode'),
      crbAccountStatusUpdatePage.setAccountStatusTypeInput('accountStatusType'),
      crbAccountStatusUpdatePage.setAccountStatusTypeDetailsInput('accountStatusTypeDetails'),
    ]);

    await crbAccountStatusUpdatePage.save();
    expect(await crbAccountStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbAccountStatusComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbAccountStatus', async () => {
    const nbButtonsBeforeDelete = await crbAccountStatusComponentsPage.countDeleteButtons();
    await crbAccountStatusComponentsPage.clickOnLastDeleteButton();

    crbAccountStatusDeleteDialog = new CrbAccountStatusDeleteDialog();
    expect(await crbAccountStatusDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Account Status?');
    await crbAccountStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbAccountStatusComponentsPage.title), 5000);

    expect(await crbAccountStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
