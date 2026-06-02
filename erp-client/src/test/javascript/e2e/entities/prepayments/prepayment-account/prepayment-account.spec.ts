import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PrepaymentAccountComponentsPage,
  PrepaymentAccountDeleteDialog,
  PrepaymentAccountUpdatePage,
} from './prepayment-account.page-object';

const expect = chai.expect;

describe('PrepaymentAccount e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentAccountComponentsPage: PrepaymentAccountComponentsPage;
  let prepaymentAccountUpdatePage: PrepaymentAccountUpdatePage;
  let prepaymentAccountDeleteDialog: PrepaymentAccountDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentAccounts', async () => {
    await navBarPage.goToEntity('prepayment-account');
    prepaymentAccountComponentsPage = new PrepaymentAccountComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentAccountComponentsPage.title), 5000);
    expect(await prepaymentAccountComponentsPage.getTitle()).to.eq('Prepayment Accounts');
    await browser.wait(
      ec.or(ec.visibilityOf(prepaymentAccountComponentsPage.entities), ec.visibilityOf(prepaymentAccountComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PrepaymentAccount page', async () => {
    await prepaymentAccountComponentsPage.clickOnCreateButton();
    prepaymentAccountUpdatePage = new PrepaymentAccountUpdatePage();
    expect(await prepaymentAccountUpdatePage.getPageTitle()).to.eq('Create or edit a Prepayment Account');
    await prepaymentAccountUpdatePage.cancel();
  });

  it('should create and save PrepaymentAccounts', async () => {
    const nbButtonsBeforeCreate = await prepaymentAccountComponentsPage.countDeleteButtons();

    await prepaymentAccountComponentsPage.clickOnCreateButton();

    await promise.all([
      prepaymentAccountUpdatePage.setCatalogueNumberInput('catalogueNumber'),
      prepaymentAccountUpdatePage.setRecognitionDateInput('2000-12-31'),
      prepaymentAccountUpdatePage.setParticularsInput('particulars'),
      prepaymentAccountUpdatePage.setNotesInput('notes'),
      prepaymentAccountUpdatePage.setPrepaymentAmountInput('5'),
      prepaymentAccountUpdatePage.setPrepaymentGuidInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      prepaymentAccountUpdatePage.settlementCurrencySelectLastOption(),
      prepaymentAccountUpdatePage.prepaymentTransactionSelectLastOption(),
      prepaymentAccountUpdatePage.serviceOutletSelectLastOption(),
      prepaymentAccountUpdatePage.dealerSelectLastOption(),
      prepaymentAccountUpdatePage.debitAccountSelectLastOption(),
      prepaymentAccountUpdatePage.transferAccountSelectLastOption(),
      // prepaymentAccountUpdatePage.placeholderSelectLastOption(),
      // prepaymentAccountUpdatePage.generalParametersSelectLastOption(),
      // prepaymentAccountUpdatePage.prepaymentParametersSelectLastOption(),
      // prepaymentAccountUpdatePage.businessDocumentSelectLastOption(),
      prepaymentAccountUpdatePage.createdBySelectLastOption(),
    ]);

    await prepaymentAccountUpdatePage.save();
    expect(await prepaymentAccountUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await prepaymentAccountComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PrepaymentAccount', async () => {
    const nbButtonsBeforeDelete = await prepaymentAccountComponentsPage.countDeleteButtons();
    await prepaymentAccountComponentsPage.clickOnLastDeleteButton();

    prepaymentAccountDeleteDialog = new PrepaymentAccountDeleteDialog();
    expect(await prepaymentAccountDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Prepayment Account?');
    await prepaymentAccountDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(prepaymentAccountComponentsPage.title), 5000);

    expect(await prepaymentAccountComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
