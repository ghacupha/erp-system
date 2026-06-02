import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { MerchantTypeComponentsPage, MerchantTypeDeleteDialog, MerchantTypeUpdatePage } from './merchant-type.page-object';

const expect = chai.expect;

describe('MerchantType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let merchantTypeComponentsPage: MerchantTypeComponentsPage;
  let merchantTypeUpdatePage: MerchantTypeUpdatePage;
  let merchantTypeDeleteDialog: MerchantTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MerchantTypes', async () => {
    await navBarPage.goToEntity('merchant-type');
    merchantTypeComponentsPage = new MerchantTypeComponentsPage();
    await browser.wait(ec.visibilityOf(merchantTypeComponentsPage.title), 5000);
    expect(await merchantTypeComponentsPage.getTitle()).to.eq('Merchant Types');
    await browser.wait(
      ec.or(ec.visibilityOf(merchantTypeComponentsPage.entities), ec.visibilityOf(merchantTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create MerchantType page', async () => {
    await merchantTypeComponentsPage.clickOnCreateButton();
    merchantTypeUpdatePage = new MerchantTypeUpdatePage();
    expect(await merchantTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Merchant Type');
    await merchantTypeUpdatePage.cancel();
  });

  it('should create and save MerchantTypes', async () => {
    const nbButtonsBeforeCreate = await merchantTypeComponentsPage.countDeleteButtons();

    await merchantTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      merchantTypeUpdatePage.setMerchantTypeCodeInput('merchantTypeCode'),
      merchantTypeUpdatePage.setMerchantTypeInput('merchantType'),
      merchantTypeUpdatePage.setMerchantTypeDetailsInput('merchantTypeDetails'),
    ]);

    await merchantTypeUpdatePage.save();
    expect(await merchantTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await merchantTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last MerchantType', async () => {
    const nbButtonsBeforeDelete = await merchantTypeComponentsPage.countDeleteButtons();
    await merchantTypeComponentsPage.clickOnLastDeleteButton();

    merchantTypeDeleteDialog = new MerchantTypeDeleteDialog();
    expect(await merchantTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Merchant Type?');
    await merchantTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(merchantTypeComponentsPage.title), 5000);

    expect(await merchantTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
