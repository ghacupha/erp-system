import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AccountStatusTypeComponentsPage,
  AccountStatusTypeDeleteDialog,
  AccountStatusTypeUpdatePage,
} from './account-status-type.page-object';

const expect = chai.expect;

describe('AccountStatusType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let accountStatusTypeComponentsPage: AccountStatusTypeComponentsPage;
  let accountStatusTypeUpdatePage: AccountStatusTypeUpdatePage;
  let accountStatusTypeDeleteDialog: AccountStatusTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AccountStatusTypes', async () => {
    await navBarPage.goToEntity('account-status-type');
    accountStatusTypeComponentsPage = new AccountStatusTypeComponentsPage();
    await browser.wait(ec.visibilityOf(accountStatusTypeComponentsPage.title), 5000);
    expect(await accountStatusTypeComponentsPage.getTitle()).to.eq('Account Status Types');
    await browser.wait(
      ec.or(ec.visibilityOf(accountStatusTypeComponentsPage.entities), ec.visibilityOf(accountStatusTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AccountStatusType page', async () => {
    await accountStatusTypeComponentsPage.clickOnCreateButton();
    accountStatusTypeUpdatePage = new AccountStatusTypeUpdatePage();
    expect(await accountStatusTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Account Status Type');
    await accountStatusTypeUpdatePage.cancel();
  });

  it('should create and save AccountStatusTypes', async () => {
    const nbButtonsBeforeCreate = await accountStatusTypeComponentsPage.countDeleteButtons();

    await accountStatusTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      accountStatusTypeUpdatePage.setAccountStatusCodeInput('accountStatusCode'),
      accountStatusTypeUpdatePage.accountStatusTypeSelectLastOption(),
      accountStatusTypeUpdatePage.setAccountStatusDescriptionInput('accountStatusDescription'),
    ]);

    await accountStatusTypeUpdatePage.save();
    expect(await accountStatusTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await accountStatusTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AccountStatusType', async () => {
    const nbButtonsBeforeDelete = await accountStatusTypeComponentsPage.countDeleteButtons();
    await accountStatusTypeComponentsPage.clickOnLastDeleteButton();

    accountStatusTypeDeleteDialog = new AccountStatusTypeDeleteDialog();
    expect(await accountStatusTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Account Status Type?');
    await accountStatusTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(accountStatusTypeComponentsPage.title), 5000);

    expect(await accountStatusTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
