import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  FxReceiptPurposeTypeComponentsPage,
  FxReceiptPurposeTypeDeleteDialog,
  FxReceiptPurposeTypeUpdatePage,
} from './fx-receipt-purpose-type.page-object';

const expect = chai.expect;

describe('FxReceiptPurposeType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fxReceiptPurposeTypeComponentsPage: FxReceiptPurposeTypeComponentsPage;
  let fxReceiptPurposeTypeUpdatePage: FxReceiptPurposeTypeUpdatePage;
  let fxReceiptPurposeTypeDeleteDialog: FxReceiptPurposeTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FxReceiptPurposeTypes', async () => {
    await navBarPage.goToEntity('fx-receipt-purpose-type');
    fxReceiptPurposeTypeComponentsPage = new FxReceiptPurposeTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fxReceiptPurposeTypeComponentsPage.title), 5000);
    expect(await fxReceiptPurposeTypeComponentsPage.getTitle()).to.eq('Fx Receipt Purpose Types');
    await browser.wait(
      ec.or(ec.visibilityOf(fxReceiptPurposeTypeComponentsPage.entities), ec.visibilityOf(fxReceiptPurposeTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FxReceiptPurposeType page', async () => {
    await fxReceiptPurposeTypeComponentsPage.clickOnCreateButton();
    fxReceiptPurposeTypeUpdatePage = new FxReceiptPurposeTypeUpdatePage();
    expect(await fxReceiptPurposeTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Fx Receipt Purpose Type');
    await fxReceiptPurposeTypeUpdatePage.cancel();
  });

  it('should create and save FxReceiptPurposeTypes', async () => {
    const nbButtonsBeforeCreate = await fxReceiptPurposeTypeComponentsPage.countDeleteButtons();

    await fxReceiptPurposeTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fxReceiptPurposeTypeUpdatePage.setItemCodeInput('itemCode'),
      fxReceiptPurposeTypeUpdatePage.setAttribute1ReceiptPaymentPurposeCodeInput('attribute1ReceiptPaymentPurposeCode'),
      fxReceiptPurposeTypeUpdatePage.setAttribute1ReceiptPaymentPurposeTypeInput('attribute1ReceiptPaymentPurposeType'),
      fxReceiptPurposeTypeUpdatePage.setAttribute2ReceiptPaymentPurposeCodeInput('attribute2ReceiptPaymentPurposeCode'),
      fxReceiptPurposeTypeUpdatePage.setAttribute2ReceiptPaymentPurposeDescriptionInput('attribute2ReceiptPaymentPurposeDescription'),
      fxReceiptPurposeTypeUpdatePage.setAttribute3ReceiptPaymentPurposeCodeInput('attribute3ReceiptPaymentPurposeCode'),
      fxReceiptPurposeTypeUpdatePage.setAttribute3ReceiptPaymentPurposeDescriptionInput('attribute3ReceiptPaymentPurposeDescription'),
      fxReceiptPurposeTypeUpdatePage.setAttribute4ReceiptPaymentPurposeCodeInput('attribute4ReceiptPaymentPurposeCode'),
      fxReceiptPurposeTypeUpdatePage.setAttribute4ReceiptPaymentPurposeDescriptionInput('attribute4ReceiptPaymentPurposeDescription'),
      fxReceiptPurposeTypeUpdatePage.setAttribute5ReceiptPaymentPurposeCodeInput('attribute5ReceiptPaymentPurposeCode'),
      fxReceiptPurposeTypeUpdatePage.setAttribute5ReceiptPaymentPurposeDescriptionInput('attribute5ReceiptPaymentPurposeDescription'),
      fxReceiptPurposeTypeUpdatePage.setLastChildInput('lastChild'),
    ]);

    await fxReceiptPurposeTypeUpdatePage.save();
    expect(await fxReceiptPurposeTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fxReceiptPurposeTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FxReceiptPurposeType', async () => {
    const nbButtonsBeforeDelete = await fxReceiptPurposeTypeComponentsPage.countDeleteButtons();
    await fxReceiptPurposeTypeComponentsPage.clickOnLastDeleteButton();

    fxReceiptPurposeTypeDeleteDialog = new FxReceiptPurposeTypeDeleteDialog();
    expect(await fxReceiptPurposeTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fx Receipt Purpose Type?');
    await fxReceiptPurposeTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fxReceiptPurposeTypeComponentsPage.title), 5000);

    expect(await fxReceiptPurposeTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
