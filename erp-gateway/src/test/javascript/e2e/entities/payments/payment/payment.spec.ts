import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PaymentComponentsPage,
  /* PaymentDeleteDialog, */
  PaymentUpdatePage,
} from './payment.page-object';

const expect = chai.expect;

describe('Payment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentComponentsPage: PaymentComponentsPage;
  let paymentUpdatePage: PaymentUpdatePage;
  /* let paymentDeleteDialog: PaymentDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Payments', async () => {
    await navBarPage.goToEntity('payment');
    paymentComponentsPage = new PaymentComponentsPage();
    await browser.wait(ec.visibilityOf(paymentComponentsPage.title), 5000);
    expect(await paymentComponentsPage.getTitle()).to.eq('Payments');
    await browser.wait(ec.or(ec.visibilityOf(paymentComponentsPage.entities), ec.visibilityOf(paymentComponentsPage.noResult)), 1000);
  });

  it('should load create Payment page', async () => {
    await paymentComponentsPage.clickOnCreateButton();
    paymentUpdatePage = new PaymentUpdatePage();
    expect(await paymentUpdatePage.getPageTitle()).to.eq('Create or edit a Payment');
    await paymentUpdatePage.cancel();
  });

  /* it('should create and save Payments', async () => {
        const nbButtonsBeforeCreate = await paymentComponentsPage.countDeleteButtons();

        await paymentComponentsPage.clickOnCreateButton();

        await promise.all([
            paymentUpdatePage.setPaymentNumberInput('paymentNumber'),
            paymentUpdatePage.setPaymentDateInput('2000-12-31'),
            paymentUpdatePage.setPaymentAmountInput('5'),
            paymentUpdatePage.setDealerNameInput('dealerName'),
            paymentUpdatePage.setPaymentCategoryInput('paymentCategory'),
            paymentUpdatePage.paymentCalculationSelectLastOption(),
            paymentUpdatePage.paymentRequisitionSelectLastOption(),
            paymentUpdatePage.taxRuleSelectLastOption(),
        ]);

        expect(await paymentUpdatePage.getPaymentNumberInput()).to.eq('paymentNumber', 'Expected PaymentNumber value to be equals to paymentNumber');
        expect(await paymentUpdatePage.getPaymentDateInput()).to.eq('2000-12-31', 'Expected paymentDate value to be equals to 2000-12-31');
        expect(await paymentUpdatePage.getPaymentAmountInput()).to.eq('5', 'Expected paymentAmount value to be equals to 5');
        expect(await paymentUpdatePage.getDealerNameInput()).to.eq('dealerName', 'Expected DealerName value to be equals to dealerName');
        expect(await paymentUpdatePage.getPaymentCategoryInput()).to.eq('paymentCategory', 'Expected PaymentCategory value to be equals to paymentCategory');

        await paymentUpdatePage.save();
        expect(await paymentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Payment', async () => {
        const nbButtonsBeforeDelete = await paymentComponentsPage.countDeleteButtons();
        await paymentComponentsPage.clickOnLastDeleteButton();

        paymentDeleteDialog = new PaymentDeleteDialog();
        expect(await paymentDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Payment?');
        await paymentDeleteDialog.clickOnConfirmButton();

        expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
