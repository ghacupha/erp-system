import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  BusinessStampComponentsPage,
  /* BusinessStampDeleteDialog, */
  BusinessStampUpdatePage,
} from './business-stamp.page-object';

const expect = chai.expect;

describe('BusinessStamp e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let businessStampComponentsPage: BusinessStampComponentsPage;
  let businessStampUpdatePage: BusinessStampUpdatePage;
  /* let businessStampDeleteDialog: BusinessStampDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load BusinessStamps', async () => {
    await navBarPage.goToEntity('business-stamp');
    businessStampComponentsPage = new BusinessStampComponentsPage();
    await browser.wait(ec.visibilityOf(businessStampComponentsPage.title), 5000);
    expect(await businessStampComponentsPage.getTitle()).to.eq('Business Stamps');
    await browser.wait(
      ec.or(ec.visibilityOf(businessStampComponentsPage.entities), ec.visibilityOf(businessStampComponentsPage.noResult)),
      1000
    );
  });

  it('should load create BusinessStamp page', async () => {
    await businessStampComponentsPage.clickOnCreateButton();
    businessStampUpdatePage = new BusinessStampUpdatePage();
    expect(await businessStampUpdatePage.getPageTitle()).to.eq('Create or edit a Business Stamp');
    await businessStampUpdatePage.cancel();
  });

  /* it('should create and save BusinessStamps', async () => {
        const nbButtonsBeforeCreate = await businessStampComponentsPage.countDeleteButtons();

        await businessStampComponentsPage.clickOnCreateButton();

        await promise.all([
            businessStampUpdatePage.setStampDateInput('2000-12-31'),
            businessStampUpdatePage.setPurposeInput('purpose'),
            businessStampUpdatePage.setDetailsInput('details'),
            businessStampUpdatePage.setRemarksInput('remarks'),
            businessStampUpdatePage.stampHolderSelectLastOption(),
            // businessStampUpdatePage.placeholderSelectLastOption(),
        ]);

        await businessStampUpdatePage.save();
        expect(await businessStampUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await businessStampComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last BusinessStamp', async () => {
        const nbButtonsBeforeDelete = await businessStampComponentsPage.countDeleteButtons();
        await businessStampComponentsPage.clickOnLastDeleteButton();

        businessStampDeleteDialog = new BusinessStampDeleteDialog();
        expect(await businessStampDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Business Stamp?');
        await businessStampDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(businessStampComponentsPage.title), 5000);

        expect(await businessStampComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
