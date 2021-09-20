import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SignedPaymentComponentsPage, SignedPaymentDeleteDialog, SignedPaymentUpdatePage } from './signed-payment.page-object';

const expect = chai.expect;

describe('SignedPayment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let signedPaymentComponentsPage: SignedPaymentComponentsPage;
  let signedPaymentUpdatePage: SignedPaymentUpdatePage;
  let signedPaymentDeleteDialog: SignedPaymentDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SignedPayments', async () => {
    await navBarPage.goToEntity('signed-payment');
    signedPaymentComponentsPage = new SignedPaymentComponentsPage();
    await browser.wait(ec.visibilityOf(signedPaymentComponentsPage.title), 5000);
    expect(await signedPaymentComponentsPage.getTitle()).to.eq('Signed Payments');
    await browser.wait(
      ec.or(ec.visibilityOf(signedPaymentComponentsPage.entities), ec.visibilityOf(signedPaymentComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SignedPayment page', async () => {
    await signedPaymentComponentsPage.clickOnCreateButton();
    signedPaymentUpdatePage = new SignedPaymentUpdatePage();
    expect(await signedPaymentUpdatePage.getPageTitle()).to.eq('Create or edit a Signed Payment');
    await signedPaymentUpdatePage.cancel();
  });

  it('should create and save SignedPayments', async () => {
    const nbButtonsBeforeCreate = await signedPaymentComponentsPage.countDeleteButtons();

    await signedPaymentComponentsPage.clickOnCreateButton();

    await promise.all([
      signedPaymentUpdatePage.paymentCategorySelectLastOption(),
      signedPaymentUpdatePage.setTransactionNumberInput('transactionNumber'),
      signedPaymentUpdatePage.setTransactionDateInput('2000-12-31'),
      signedPaymentUpdatePage.transactionCurrencySelectLastOption(),
      signedPaymentUpdatePage.setTransactionAmountInput('5'),
      signedPaymentUpdatePage.setBeneficiaryInput('beneficiary'),
      // signedPaymentUpdatePage.paymentLabelSelectLastOption(),
      // signedPaymentUpdatePage.placeholderSelectLastOption(),
    ]);

    await signedPaymentUpdatePage.save();
    expect(await signedPaymentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await signedPaymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SignedPayment', async () => {
    const nbButtonsBeforeDelete = await signedPaymentComponentsPage.countDeleteButtons();
    await signedPaymentComponentsPage.clickOnLastDeleteButton();

    signedPaymentDeleteDialog = new SignedPaymentDeleteDialog();
    expect(await signedPaymentDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Signed Payment?');
    await signedPaymentDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(signedPaymentComponentsPage.title), 5000);

    expect(await signedPaymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
