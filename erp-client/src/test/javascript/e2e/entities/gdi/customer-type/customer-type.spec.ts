import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CustomerTypeComponentsPage, CustomerTypeDeleteDialog, CustomerTypeUpdatePage } from './customer-type.page-object';

const expect = chai.expect;

describe('CustomerType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let customerTypeComponentsPage: CustomerTypeComponentsPage;
  let customerTypeUpdatePage: CustomerTypeUpdatePage;
  let customerTypeDeleteDialog: CustomerTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CustomerTypes', async () => {
    await navBarPage.goToEntity('customer-type');
    customerTypeComponentsPage = new CustomerTypeComponentsPage();
    await browser.wait(ec.visibilityOf(customerTypeComponentsPage.title), 5000);
    expect(await customerTypeComponentsPage.getTitle()).to.eq('Customer Types');
    await browser.wait(
      ec.or(ec.visibilityOf(customerTypeComponentsPage.entities), ec.visibilityOf(customerTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CustomerType page', async () => {
    await customerTypeComponentsPage.clickOnCreateButton();
    customerTypeUpdatePage = new CustomerTypeUpdatePage();
    expect(await customerTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Customer Type');
    await customerTypeUpdatePage.cancel();
  });

  it('should create and save CustomerTypes', async () => {
    const nbButtonsBeforeCreate = await customerTypeComponentsPage.countDeleteButtons();

    await customerTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      customerTypeUpdatePage.setCustomerTypeCodeInput('customerTypeCode'),
      customerTypeUpdatePage.setCustomerTypeInput('customerType'),
      customerTypeUpdatePage.setCustomerTypeDescriptionInput('customerTypeDescription'),
    ]);

    await customerTypeUpdatePage.save();
    expect(await customerTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await customerTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CustomerType', async () => {
    const nbButtonsBeforeDelete = await customerTypeComponentsPage.countDeleteButtons();
    await customerTypeComponentsPage.clickOnLastDeleteButton();

    customerTypeDeleteDialog = new CustomerTypeDeleteDialog();
    expect(await customerTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Customer Type?');
    await customerTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(customerTypeComponentsPage.title), 5000);

    expect(await customerTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
