import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  DeliveryNoteComponentsPage,
  /* DeliveryNoteDeleteDialog, */
  DeliveryNoteUpdatePage,
} from './delivery-note.page-object';

const expect = chai.expect;

describe('DeliveryNote e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let deliveryNoteComponentsPage: DeliveryNoteComponentsPage;
  let deliveryNoteUpdatePage: DeliveryNoteUpdatePage;
  /* let deliveryNoteDeleteDialog: DeliveryNoteDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DeliveryNotes', async () => {
    await navBarPage.goToEntity('delivery-note');
    deliveryNoteComponentsPage = new DeliveryNoteComponentsPage();
    await browser.wait(ec.visibilityOf(deliveryNoteComponentsPage.title), 5000);
    expect(await deliveryNoteComponentsPage.getTitle()).to.eq('Delivery Notes');
    await browser.wait(
      ec.or(ec.visibilityOf(deliveryNoteComponentsPage.entities), ec.visibilityOf(deliveryNoteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DeliveryNote page', async () => {
    await deliveryNoteComponentsPage.clickOnCreateButton();
    deliveryNoteUpdatePage = new DeliveryNoteUpdatePage();
    expect(await deliveryNoteUpdatePage.getPageTitle()).to.eq('Create or edit a Delivery Note');
    await deliveryNoteUpdatePage.cancel();
  });

  /* it('should create and save DeliveryNotes', async () => {
        const nbButtonsBeforeCreate = await deliveryNoteComponentsPage.countDeleteButtons();

        await deliveryNoteComponentsPage.clickOnCreateButton();

        await promise.all([
            deliveryNoteUpdatePage.setDeliveryNoteNumberInput('deliveryNoteNumber'),
            deliveryNoteUpdatePage.setDocumentDateInput('2000-12-31'),
            deliveryNoteUpdatePage.setDescriptionInput('description'),
            deliveryNoteUpdatePage.setSerialNumberInput('serialNumber'),
            deliveryNoteUpdatePage.setQuantityInput('5'),
            deliveryNoteUpdatePage.setRemarksInput('remarks'),
            // deliveryNoteUpdatePage.placeholderSelectLastOption(),
            deliveryNoteUpdatePage.receivedBySelectLastOption(),
            // deliveryNoteUpdatePage.deliveryStampsSelectLastOption(),
            deliveryNoteUpdatePage.purchaseOrderSelectLastOption(),
            deliveryNoteUpdatePage.supplierSelectLastOption(),
            // deliveryNoteUpdatePage.signatoriesSelectLastOption(),
            // deliveryNoteUpdatePage.otherPurchaseOrdersSelectLastOption(),
            // deliveryNoteUpdatePage.businessDocumentSelectLastOption(),
        ]);

        await deliveryNoteUpdatePage.save();
        expect(await deliveryNoteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await deliveryNoteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last DeliveryNote', async () => {
        const nbButtonsBeforeDelete = await deliveryNoteComponentsPage.countDeleteButtons();
        await deliveryNoteComponentsPage.clickOnLastDeleteButton();

        deliveryNoteDeleteDialog = new DeliveryNoteDeleteDialog();
        expect(await deliveryNoteDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Delivery Note?');
        await deliveryNoteDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(deliveryNoteComponentsPage.title), 5000);

        expect(await deliveryNoteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
