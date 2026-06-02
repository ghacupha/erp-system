import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ParticularsOfOutletComponentsPage,
  /* ParticularsOfOutletDeleteDialog, */
  ParticularsOfOutletUpdatePage,
} from './particulars-of-outlet.page-object';

const expect = chai.expect;

describe('ParticularsOfOutlet e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let particularsOfOutletComponentsPage: ParticularsOfOutletComponentsPage;
  let particularsOfOutletUpdatePage: ParticularsOfOutletUpdatePage;
  /* let particularsOfOutletDeleteDialog: ParticularsOfOutletDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ParticularsOfOutlets', async () => {
    await navBarPage.goToEntity('particulars-of-outlet');
    particularsOfOutletComponentsPage = new ParticularsOfOutletComponentsPage();
    await browser.wait(ec.visibilityOf(particularsOfOutletComponentsPage.title), 5000);
    expect(await particularsOfOutletComponentsPage.getTitle()).to.eq('Particulars Of Outlets');
    await browser.wait(
      ec.or(ec.visibilityOf(particularsOfOutletComponentsPage.entities), ec.visibilityOf(particularsOfOutletComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ParticularsOfOutlet page', async () => {
    await particularsOfOutletComponentsPage.clickOnCreateButton();
    particularsOfOutletUpdatePage = new ParticularsOfOutletUpdatePage();
    expect(await particularsOfOutletUpdatePage.getPageTitle()).to.eq('Create or edit a Particulars Of Outlet');
    await particularsOfOutletUpdatePage.cancel();
  });

  /* it('should create and save ParticularsOfOutlets', async () => {
        const nbButtonsBeforeCreate = await particularsOfOutletComponentsPage.countDeleteButtons();

        await particularsOfOutletComponentsPage.clickOnCreateButton();

        await promise.all([
            particularsOfOutletUpdatePage.setBusinessReportingDateInput('2000-12-31'),
            particularsOfOutletUpdatePage.setOutletNameInput('outletName'),
            particularsOfOutletUpdatePage.setTownInput('town'),
            particularsOfOutletUpdatePage.setIso6709LatituteInput('5'),
            particularsOfOutletUpdatePage.setIso6709LongitudeInput('5'),
            particularsOfOutletUpdatePage.setCbkApprovalDateInput('2000-12-31'),
            particularsOfOutletUpdatePage.setOutletOpeningDateInput('2000-12-31'),
            particularsOfOutletUpdatePage.setOutletClosureDateInput('2000-12-31'),
            particularsOfOutletUpdatePage.setLicenseFeePayableInput('5'),
            particularsOfOutletUpdatePage.subCountyCodeSelectLastOption(),
            particularsOfOutletUpdatePage.bankCodeSelectLastOption(),
            particularsOfOutletUpdatePage.outletIdSelectLastOption(),
            particularsOfOutletUpdatePage.typeOfOutletSelectLastOption(),
            particularsOfOutletUpdatePage.outletStatusSelectLastOption(),
        ]);

        await particularsOfOutletUpdatePage.save();
        expect(await particularsOfOutletUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await particularsOfOutletComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ParticularsOfOutlet', async () => {
        const nbButtonsBeforeDelete = await particularsOfOutletComponentsPage.countDeleteButtons();
        await particularsOfOutletComponentsPage.clickOnLastDeleteButton();

        particularsOfOutletDeleteDialog = new ParticularsOfOutletDeleteDialog();
        expect(await particularsOfOutletDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Particulars Of Outlet?');
        await particularsOfOutletDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(particularsOfOutletComponentsPage.title), 5000);

        expect(await particularsOfOutletComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
