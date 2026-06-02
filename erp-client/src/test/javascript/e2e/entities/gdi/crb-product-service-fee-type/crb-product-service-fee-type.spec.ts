import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbProductServiceFeeTypeComponentsPage,
  CrbProductServiceFeeTypeDeleteDialog,
  CrbProductServiceFeeTypeUpdatePage,
} from './crb-product-service-fee-type.page-object';

const expect = chai.expect;

describe('CrbProductServiceFeeType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbProductServiceFeeTypeComponentsPage: CrbProductServiceFeeTypeComponentsPage;
  let crbProductServiceFeeTypeUpdatePage: CrbProductServiceFeeTypeUpdatePage;
  let crbProductServiceFeeTypeDeleteDialog: CrbProductServiceFeeTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbProductServiceFeeTypes', async () => {
    await navBarPage.goToEntity('crb-product-service-fee-type');
    crbProductServiceFeeTypeComponentsPage = new CrbProductServiceFeeTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbProductServiceFeeTypeComponentsPage.title), 5000);
    expect(await crbProductServiceFeeTypeComponentsPage.getTitle()).to.eq('Crb Product Service Fee Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(crbProductServiceFeeTypeComponentsPage.entities),
        ec.visibilityOf(crbProductServiceFeeTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CrbProductServiceFeeType page', async () => {
    await crbProductServiceFeeTypeComponentsPage.clickOnCreateButton();
    crbProductServiceFeeTypeUpdatePage = new CrbProductServiceFeeTypeUpdatePage();
    expect(await crbProductServiceFeeTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Product Service Fee Type');
    await crbProductServiceFeeTypeUpdatePage.cancel();
  });

  it('should create and save CrbProductServiceFeeTypes', async () => {
    const nbButtonsBeforeCreate = await crbProductServiceFeeTypeComponentsPage.countDeleteButtons();

    await crbProductServiceFeeTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbProductServiceFeeTypeUpdatePage.setChargeTypeCodeInput('chargeTypeCode'),
      crbProductServiceFeeTypeUpdatePage.setChargeTypeDescriptionInput('chargeTypeDescription'),
      crbProductServiceFeeTypeUpdatePage.setChargeTypeCategoryInput('chargeTypeCategory'),
    ]);

    await crbProductServiceFeeTypeUpdatePage.save();
    expect(await crbProductServiceFeeTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbProductServiceFeeTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbProductServiceFeeType', async () => {
    const nbButtonsBeforeDelete = await crbProductServiceFeeTypeComponentsPage.countDeleteButtons();
    await crbProductServiceFeeTypeComponentsPage.clickOnLastDeleteButton();

    crbProductServiceFeeTypeDeleteDialog = new CrbProductServiceFeeTypeDeleteDialog();
    expect(await crbProductServiceFeeTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Product Service Fee Type?'
    );
    await crbProductServiceFeeTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbProductServiceFeeTypeComponentsPage.title), 5000);

    expect(await crbProductServiceFeeTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
