import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PaymentRequisitionComponentsPage,
  PaymentRequisitionDeleteDialog,
  PaymentRequisitionUpdatePage,
} from './payment-requisition.page-object';

const expect = chai.expect;

describe('PaymentRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentRequisitionComponentsPage: PaymentRequisitionComponentsPage;
  let paymentRequisitionUpdatePage: PaymentRequisitionUpdatePage;
  let paymentRequisitionDeleteDialog: PaymentRequisitionDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PaymentRequisitions', async () => {
    await navBarPage.goToEntity('payment-requisition');
    paymentRequisitionComponentsPage = new PaymentRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(paymentRequisitionComponentsPage.title), 5000);
    expect(await paymentRequisitionComponentsPage.getTitle()).to.eq('Payment Requisitions');
    await browser.wait(
      ec.or(ec.visibilityOf(paymentRequisitionComponentsPage.entities), ec.visibilityOf(paymentRequisitionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PaymentRequisition page', async () => {
    await paymentRequisitionComponentsPage.clickOnCreateButton();
    paymentRequisitionUpdatePage = new PaymentRequisitionUpdatePage();
    expect(await paymentRequisitionUpdatePage.getPageTitle()).to.eq('Create or edit a Payment Requisition');
    await paymentRequisitionUpdatePage.cancel();
  });

  it('should create and save PaymentRequisitions', async () => {
    const nbButtonsBeforeCreate = await paymentRequisitionComponentsPage.countDeleteButtons();

    await paymentRequisitionComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentRequisitionUpdatePage.setInvoicedAmountInput('5'),
      paymentRequisitionUpdatePage.setDisbursementCostInput('5'),
      paymentRequisitionUpdatePage.setVatableAmountInput('5'),
      paymentRequisitionUpdatePage.dealerSelectLastOption(),
      // paymentRequisitionUpdatePage.placeholderSelectLastOption(),
    ]);

    await paymentRequisitionUpdatePage.save();
    expect(await paymentRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentRequisitionComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PaymentRequisition', async () => {
    const nbButtonsBeforeDelete = await paymentRequisitionComponentsPage.countDeleteButtons();
    await paymentRequisitionComponentsPage.clickOnLastDeleteButton();

    paymentRequisitionDeleteDialog = new PaymentRequisitionDeleteDialog();
    expect(await paymentRequisitionDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Payment Requisition?');
    await paymentRequisitionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(paymentRequisitionComponentsPage.title), 5000);

    expect(await paymentRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
