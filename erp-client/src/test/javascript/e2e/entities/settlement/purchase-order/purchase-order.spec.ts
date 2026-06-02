import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PurchaseOrderComponentsPage,
  /* PurchaseOrderDeleteDialog, */
  PurchaseOrderUpdatePage,
} from './purchase-order.page-object';

const expect = chai.expect;

describe('PurchaseOrder e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let purchaseOrderComponentsPage: PurchaseOrderComponentsPage;
  let purchaseOrderUpdatePage: PurchaseOrderUpdatePage;
  /* let purchaseOrderDeleteDialog: PurchaseOrderDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PurchaseOrders', async () => {
    await navBarPage.goToEntity('purchase-order');
    purchaseOrderComponentsPage = new PurchaseOrderComponentsPage();
    await browser.wait(ec.visibilityOf(purchaseOrderComponentsPage.title), 5000);
    expect(await purchaseOrderComponentsPage.getTitle()).to.eq('Purchase Orders');
    await browser.wait(
      ec.or(ec.visibilityOf(purchaseOrderComponentsPage.entities), ec.visibilityOf(purchaseOrderComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PurchaseOrder page', async () => {
    await purchaseOrderComponentsPage.clickOnCreateButton();
    purchaseOrderUpdatePage = new PurchaseOrderUpdatePage();
    expect(await purchaseOrderUpdatePage.getPageTitle()).to.eq('Create or edit a Purchase Order');
    await purchaseOrderUpdatePage.cancel();
  });

  /* it('should create and save PurchaseOrders', async () => {
        const nbButtonsBeforeCreate = await purchaseOrderComponentsPage.countDeleteButtons();

        await purchaseOrderComponentsPage.clickOnCreateButton();

        await promise.all([
            purchaseOrderUpdatePage.setPurchaseOrderNumberInput('purchaseOrderNumber'),
            purchaseOrderUpdatePage.setPurchaseOrderDateInput('2000-12-31'),
            purchaseOrderUpdatePage.setPurchaseOrderAmountInput('5'),
            purchaseOrderUpdatePage.setDescriptionInput('description'),
            purchaseOrderUpdatePage.setNotesInput('notes'),
            purchaseOrderUpdatePage.setFileUploadTokenInput('fileUploadToken'),
            purchaseOrderUpdatePage.setCompilationTokenInput('compilationToken'),
            purchaseOrderUpdatePage.setRemarksInput('remarks'),
            purchaseOrderUpdatePage.settlementCurrencySelectLastOption(),
            // purchaseOrderUpdatePage.placeholderSelectLastOption(),
            // purchaseOrderUpdatePage.signatoriesSelectLastOption(),
            purchaseOrderUpdatePage.vendorSelectLastOption(),
            // purchaseOrderUpdatePage.businessDocumentSelectLastOption(),
        ]);

        await purchaseOrderUpdatePage.save();
        expect(await purchaseOrderUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await purchaseOrderComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last PurchaseOrder', async () => {
        const nbButtonsBeforeDelete = await purchaseOrderComponentsPage.countDeleteButtons();
        await purchaseOrderComponentsPage.clickOnLastDeleteButton();

        purchaseOrderDeleteDialog = new PurchaseOrderDeleteDialog();
        expect(await purchaseOrderDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Purchase Order?');
        await purchaseOrderDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(purchaseOrderComponentsPage.title), 5000);

        expect(await purchaseOrderComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
