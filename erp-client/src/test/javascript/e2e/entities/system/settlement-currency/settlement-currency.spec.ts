import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  SettlementCurrencyComponentsPage,
  SettlementCurrencyDeleteDialog,
  SettlementCurrencyUpdatePage,
} from './settlement-currency.page-object';

const expect = chai.expect;

describe('SettlementCurrency e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let settlementCurrencyComponentsPage: SettlementCurrencyComponentsPage;
  let settlementCurrencyUpdatePage: SettlementCurrencyUpdatePage;
  let settlementCurrencyDeleteDialog: SettlementCurrencyDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SettlementCurrencies', async () => {
    await navBarPage.goToEntity('settlement-currency');
    settlementCurrencyComponentsPage = new SettlementCurrencyComponentsPage();
    await browser.wait(ec.visibilityOf(settlementCurrencyComponentsPage.title), 5000);
    expect(await settlementCurrencyComponentsPage.getTitle()).to.eq('Settlement Currencies');
    await browser.wait(
      ec.or(ec.visibilityOf(settlementCurrencyComponentsPage.entities), ec.visibilityOf(settlementCurrencyComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SettlementCurrency page', async () => {
    await settlementCurrencyComponentsPage.clickOnCreateButton();
    settlementCurrencyUpdatePage = new SettlementCurrencyUpdatePage();
    expect(await settlementCurrencyUpdatePage.getPageTitle()).to.eq('Create or edit a Settlement Currency');
    await settlementCurrencyUpdatePage.cancel();
  });

  it('should create and save SettlementCurrencies', async () => {
    const nbButtonsBeforeCreate = await settlementCurrencyComponentsPage.countDeleteButtons();

    await settlementCurrencyComponentsPage.clickOnCreateButton();

    await promise.all([
      settlementCurrencyUpdatePage.setIso4217CurrencyCodeInput('iso4217CurrencyCode'),
      settlementCurrencyUpdatePage.setCurrencyNameInput('currencyName'),
      settlementCurrencyUpdatePage.setCountryInput('country'),
      settlementCurrencyUpdatePage.setNumericCodeInput('numericCode'),
      settlementCurrencyUpdatePage.setMinorUnitInput('minorUnit'),
      settlementCurrencyUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      settlementCurrencyUpdatePage.setCompilationTokenInput('compilationToken'),
      // settlementCurrencyUpdatePage.placeholderSelectLastOption(),
    ]);

    await settlementCurrencyUpdatePage.save();
    expect(await settlementCurrencyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await settlementCurrencyComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SettlementCurrency', async () => {
    const nbButtonsBeforeDelete = await settlementCurrencyComponentsPage.countDeleteButtons();
    await settlementCurrencyComponentsPage.clickOnLastDeleteButton();

    settlementCurrencyDeleteDialog = new SettlementCurrencyDeleteDialog();
    expect(await settlementCurrencyDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Settlement Currency?');
    await settlementCurrencyDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(settlementCurrencyComponentsPage.title), 5000);

    expect(await settlementCurrencyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
