import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  LeasePaymentComponentsPage,
  /* LeasePaymentDeleteDialog, */
  LeasePaymentUpdatePage,
} from './lease-payment.page-object';

const expect = chai.expect;

describe('LeasePayment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leasePaymentComponentsPage: LeasePaymentComponentsPage;
  let leasePaymentUpdatePage: LeasePaymentUpdatePage;
  /* let leasePaymentDeleteDialog: LeasePaymentDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeasePayments', async () => {
    await navBarPage.goToEntity('lease-payment');
    leasePaymentComponentsPage = new LeasePaymentComponentsPage();
    await browser.wait(ec.visibilityOf(leasePaymentComponentsPage.title), 5000);
    expect(await leasePaymentComponentsPage.getTitle()).to.eq('Lease Payments');
    await browser.wait(
      ec.or(ec.visibilityOf(leasePaymentComponentsPage.entities), ec.visibilityOf(leasePaymentComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeasePayment page', async () => {
    await leasePaymentComponentsPage.clickOnCreateButton();
    leasePaymentUpdatePage = new LeasePaymentUpdatePage();
    expect(await leasePaymentUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Payment');
    await leasePaymentUpdatePage.cancel();
  });

  /* it('should create and save LeasePayments', async () => {
        const nbButtonsBeforeCreate = await leasePaymentComponentsPage.countDeleteButtons();

        await leasePaymentComponentsPage.clickOnCreateButton();

        await promise.all([
            leasePaymentUpdatePage.setPaymentDateInput('2000-12-31'),
            leasePaymentUpdatePage.setPaymentAmountInput('5'),
            leasePaymentUpdatePage.leaseContractSelectLastOption(),
        ]);

        await leasePaymentUpdatePage.save();
        expect(await leasePaymentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leasePaymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeasePayment', async () => {
        const nbButtonsBeforeDelete = await leasePaymentComponentsPage.countDeleteButtons();
        await leasePaymentComponentsPage.clickOnLastDeleteButton();

        leasePaymentDeleteDialog = new LeasePaymentDeleteDialog();
        expect(await leasePaymentDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Payment?');
        await leasePaymentDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leasePaymentComponentsPage.title), 5000);

        expect(await leasePaymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
