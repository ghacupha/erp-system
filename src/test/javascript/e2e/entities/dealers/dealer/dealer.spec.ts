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
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
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
      dealerUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      dealerUpdatePage.setCompilationTokenInput('compilationToken'),
      // dealerUpdatePage.paymentLabelSelectLastOption(),
      dealerUpdatePage.dealerGroupSelectLastOption(),
      // dealerUpdatePage.placeholderSelectLastOption(),
    ]);

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
    await browser.wait(ec.visibilityOf(dealerComponentsPage.title), 5000);

    expect(await dealerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
