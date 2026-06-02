import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CurrencyAuthenticityFlagComponentsPage,
  CurrencyAuthenticityFlagDeleteDialog,
  CurrencyAuthenticityFlagUpdatePage,
} from './currency-authenticity-flag.page-object';

const expect = chai.expect;

describe('CurrencyAuthenticityFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let currencyAuthenticityFlagComponentsPage: CurrencyAuthenticityFlagComponentsPage;
  let currencyAuthenticityFlagUpdatePage: CurrencyAuthenticityFlagUpdatePage;
  let currencyAuthenticityFlagDeleteDialog: CurrencyAuthenticityFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CurrencyAuthenticityFlags', async () => {
    await navBarPage.goToEntity('currency-authenticity-flag');
    currencyAuthenticityFlagComponentsPage = new CurrencyAuthenticityFlagComponentsPage();
    await browser.wait(ec.visibilityOf(currencyAuthenticityFlagComponentsPage.title), 5000);
    expect(await currencyAuthenticityFlagComponentsPage.getTitle()).to.eq('Currency Authenticity Flags');
    await browser.wait(
      ec.or(
        ec.visibilityOf(currencyAuthenticityFlagComponentsPage.entities),
        ec.visibilityOf(currencyAuthenticityFlagComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CurrencyAuthenticityFlag page', async () => {
    await currencyAuthenticityFlagComponentsPage.clickOnCreateButton();
    currencyAuthenticityFlagUpdatePage = new CurrencyAuthenticityFlagUpdatePage();
    expect(await currencyAuthenticityFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Currency Authenticity Flag');
    await currencyAuthenticityFlagUpdatePage.cancel();
  });

  it('should create and save CurrencyAuthenticityFlags', async () => {
    const nbButtonsBeforeCreate = await currencyAuthenticityFlagComponentsPage.countDeleteButtons();

    await currencyAuthenticityFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      currencyAuthenticityFlagUpdatePage.currencyAuthenticityFlagSelectLastOption(),
      currencyAuthenticityFlagUpdatePage.currencyAuthenticityTypeSelectLastOption(),
      currencyAuthenticityFlagUpdatePage.setCurrencyAuthenticityTypeDetailsInput('currencyAuthenticityTypeDetails'),
    ]);

    await currencyAuthenticityFlagUpdatePage.save();
    expect(await currencyAuthenticityFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await currencyAuthenticityFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CurrencyAuthenticityFlag', async () => {
    const nbButtonsBeforeDelete = await currencyAuthenticityFlagComponentsPage.countDeleteButtons();
    await currencyAuthenticityFlagComponentsPage.clickOnLastDeleteButton();

    currencyAuthenticityFlagDeleteDialog = new CurrencyAuthenticityFlagDeleteDialog();
    expect(await currencyAuthenticityFlagDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Currency Authenticity Flag?'
    );
    await currencyAuthenticityFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(currencyAuthenticityFlagComponentsPage.title), 5000);

    expect(await currencyAuthenticityFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
