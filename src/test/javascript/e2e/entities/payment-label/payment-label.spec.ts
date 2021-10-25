import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PaymentLabelComponentsPage, PaymentLabelDeleteDialog, PaymentLabelUpdatePage } from './payment-label.page-object';

const expect = chai.expect;

describe('PaymentLabel e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentLabelComponentsPage: PaymentLabelComponentsPage;
  let paymentLabelUpdatePage: PaymentLabelUpdatePage;
  let paymentLabelDeleteDialog: PaymentLabelDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PaymentLabels', async () => {
    await navBarPage.goToEntity('payment-label');
    paymentLabelComponentsPage = new PaymentLabelComponentsPage();
    await browser.wait(ec.visibilityOf(paymentLabelComponentsPage.title), 5000);
    expect(await paymentLabelComponentsPage.getTitle()).to.eq('Payment Labels');
    await browser.wait(
      ec.or(ec.visibilityOf(paymentLabelComponentsPage.entities), ec.visibilityOf(paymentLabelComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PaymentLabel page', async () => {
    await paymentLabelComponentsPage.clickOnCreateButton();
    paymentLabelUpdatePage = new PaymentLabelUpdatePage();
    expect(await paymentLabelUpdatePage.getPageTitle()).to.eq('Create or edit a Payment Label');
    await paymentLabelUpdatePage.cancel();
  });

  it('should create and save PaymentLabels', async () => {
    const nbButtonsBeforeCreate = await paymentLabelComponentsPage.countDeleteButtons();

    await paymentLabelComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentLabelUpdatePage.setDescriptionInput('description'),
      paymentLabelUpdatePage.setCommentsInput('comments'),
      paymentLabelUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      paymentLabelUpdatePage.setCompilationTokenInput('compilationToken'),
      paymentLabelUpdatePage.containingPaymentLabelSelectLastOption(),
      // paymentLabelUpdatePage.placeholderSelectLastOption(),
    ]);

    await paymentLabelUpdatePage.save();
    expect(await paymentLabelUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentLabelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last PaymentLabel', async () => {
    const nbButtonsBeforeDelete = await paymentLabelComponentsPage.countDeleteButtons();
    await paymentLabelComponentsPage.clickOnLastDeleteButton();

    paymentLabelDeleteDialog = new PaymentLabelDeleteDialog();
    expect(await paymentLabelDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Payment Label?');
    await paymentLabelDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(paymentLabelComponentsPage.title), 5000);

    expect(await paymentLabelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
