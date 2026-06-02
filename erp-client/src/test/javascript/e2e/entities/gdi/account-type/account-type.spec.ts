import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { AccountTypeComponentsPage, AccountTypeDeleteDialog, AccountTypeUpdatePage } from './account-type.page-object';

const expect = chai.expect;

describe('AccountType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let accountTypeComponentsPage: AccountTypeComponentsPage;
  let accountTypeUpdatePage: AccountTypeUpdatePage;
  let accountTypeDeleteDialog: AccountTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AccountTypes', async () => {
    await navBarPage.goToEntity('account-type');
    accountTypeComponentsPage = new AccountTypeComponentsPage();
    await browser.wait(ec.visibilityOf(accountTypeComponentsPage.title), 5000);
    expect(await accountTypeComponentsPage.getTitle()).to.eq('Account Types');
    await browser.wait(
      ec.or(ec.visibilityOf(accountTypeComponentsPage.entities), ec.visibilityOf(accountTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AccountType page', async () => {
    await accountTypeComponentsPage.clickOnCreateButton();
    accountTypeUpdatePage = new AccountTypeUpdatePage();
    expect(await accountTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Account Type');
    await accountTypeUpdatePage.cancel();
  });

  it('should create and save AccountTypes', async () => {
    const nbButtonsBeforeCreate = await accountTypeComponentsPage.countDeleteButtons();

    await accountTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      accountTypeUpdatePage.setAccountTypeCodeInput('accountTypeCode'),
      accountTypeUpdatePage.setAccountTypeInput('accountType'),
      accountTypeUpdatePage.setDescriptionInput('description'),
    ]);

    await accountTypeUpdatePage.save();
    expect(await accountTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await accountTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last AccountType', async () => {
    const nbButtonsBeforeDelete = await accountTypeComponentsPage.countDeleteButtons();
    await accountTypeComponentsPage.clickOnLastDeleteButton();

    accountTypeDeleteDialog = new AccountTypeDeleteDialog();
    expect(await accountTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Account Type?');
    await accountTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(accountTypeComponentsPage.title), 5000);

    expect(await accountTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
