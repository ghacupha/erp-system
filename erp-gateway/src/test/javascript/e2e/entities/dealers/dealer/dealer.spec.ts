import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { DealerComponentsPage, DealerDeleteDialog, DealerUpdatePage } from './dealer.page-object';

const expect = chai.expect;

describe('Dealer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let dealerComponentsPage: DealerComponentsPage;
  let dealerUpdatePage: DealerUpdatePage;
  let dealerDeleteDialog: DealerDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Dealers', async () => {
    await navBarPage.goToEntity('dealer');
    dealerComponentsPage = new DealerComponentsPage();
    await browser.wait(ec.visibilityOf(dealerComponentsPage.title), 5000);
    expect(await dealerComponentsPage.getTitle()).to.eq('Dealers');
    await browser.wait(ec.or(ec.visibilityOf(dealerComponentsPage.entities), ec.visibilityOf(dealerComponentsPage.noResult)), 1000);
  });

  it('should load create Dealer page', async () => {
    await dealerComponentsPage.clickOnCreateButton();
    dealerUpdatePage = new DealerUpdatePage();
    expect(await dealerUpdatePage.getPageTitle()).to.eq('Create or edit a Dealer');
    await dealerUpdatePage.cancel();
  });

  it('should create and save Dealers', async () => {
    const nbButtonsBeforeCreate = await dealerComponentsPage.countDeleteButtons();

    await dealerComponentsPage.clickOnCreateButton();

    await promise.all([
      dealerUpdatePage.setDealerNameInput('dealerName'),
      dealerUpdatePage.setTaxNumberInput('taxNumber'),
      dealerUpdatePage.setPostalAddressInput('postalAddress'),
      dealerUpdatePage.setPhysicalAddressInput('physicalAddress'),
      dealerUpdatePage.setAccountNameInput('accountName'),
      dealerUpdatePage.setAccountNumberInput('accountNumber'),
      dealerUpdatePage.setBankersNameInput('bankersName'),
      dealerUpdatePage.setBankersBranchInput('bankersBranch'),
      dealerUpdatePage.setBankersSwiftCodeInput('bankersSwiftCode'),
      // dealerUpdatePage.paymentSelectLastOption(),
    ]);

    expect(await dealerUpdatePage.getDealerNameInput()).to.eq('dealerName', 'Expected DealerName value to be equals to dealerName');
    expect(await dealerUpdatePage.getTaxNumberInput()).to.eq('taxNumber', 'Expected TaxNumber value to be equals to taxNumber');
    expect(await dealerUpdatePage.getPostalAddressInput()).to.eq(
      'postalAddress',
      'Expected PostalAddress value to be equals to postalAddress'
    );
    expect(await dealerUpdatePage.getPhysicalAddressInput()).to.eq(
      'physicalAddress',
      'Expected PhysicalAddress value to be equals to physicalAddress'
    );
    expect(await dealerUpdatePage.getAccountNameInput()).to.eq('accountName', 'Expected AccountName value to be equals to accountName');
    expect(await dealerUpdatePage.getAccountNumberInput()).to.eq(
      'accountNumber',
      'Expected AccountNumber value to be equals to accountNumber'
    );
    expect(await dealerUpdatePage.getBankersNameInput()).to.eq('bankersName', 'Expected BankersName value to be equals to bankersName');
    expect(await dealerUpdatePage.getBankersBranchInput()).to.eq(
      'bankersBranch',
      'Expected BankersBranch value to be equals to bankersBranch'
    );
    expect(await dealerUpdatePage.getBankersSwiftCodeInput()).to.eq(
      'bankersSwiftCode',
      'Expected BankersSwiftCode value to be equals to bankersSwiftCode'
    );

    await dealerUpdatePage.save();
    expect(await dealerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await dealerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Dealer', async () => {
    const nbButtonsBeforeDelete = await dealerComponentsPage.countDeleteButtons();
    await dealerComponentsPage.clickOnLastDeleteButton();

    dealerDeleteDialog = new DealerDeleteDialog();
    expect(await dealerDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Dealer?');
    await dealerDeleteDialog.clickOnConfirmButton();

    expect(await dealerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
