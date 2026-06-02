import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LoanProductTypeComponentsPage, LoanProductTypeDeleteDialog, LoanProductTypeUpdatePage } from './loan-product-type.page-object';

const expect = chai.expect;

describe('LoanProductType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanProductTypeComponentsPage: LoanProductTypeComponentsPage;
  let loanProductTypeUpdatePage: LoanProductTypeUpdatePage;
  let loanProductTypeDeleteDialog: LoanProductTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanProductTypes', async () => {
    await navBarPage.goToEntity('loan-product-type');
    loanProductTypeComponentsPage = new LoanProductTypeComponentsPage();
    await browser.wait(ec.visibilityOf(loanProductTypeComponentsPage.title), 5000);
    expect(await loanProductTypeComponentsPage.getTitle()).to.eq('Loan Product Types');
    await browser.wait(
      ec.or(ec.visibilityOf(loanProductTypeComponentsPage.entities), ec.visibilityOf(loanProductTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanProductType page', async () => {
    await loanProductTypeComponentsPage.clickOnCreateButton();
    loanProductTypeUpdatePage = new LoanProductTypeUpdatePage();
    expect(await loanProductTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Product Type');
    await loanProductTypeUpdatePage.cancel();
  });

  it('should create and save LoanProductTypes', async () => {
    const nbButtonsBeforeCreate = await loanProductTypeComponentsPage.countDeleteButtons();

    await loanProductTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      loanProductTypeUpdatePage.setProductCodeInput('productCode'),
      loanProductTypeUpdatePage.setProductTypeInput('productType'),
      loanProductTypeUpdatePage.setProductTypeDescriptionInput('productTypeDescription'),
    ]);

    await loanProductTypeUpdatePage.save();
    expect(await loanProductTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanProductTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanProductType', async () => {
    const nbButtonsBeforeDelete = await loanProductTypeComponentsPage.countDeleteButtons();
    await loanProductTypeComponentsPage.clickOnLastDeleteButton();

    loanProductTypeDeleteDialog = new LoanProductTypeDeleteDialog();
    expect(await loanProductTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Loan Product Type?');
    await loanProductTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanProductTypeComponentsPage.title), 5000);

    expect(await loanProductTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
