import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  IFRS16LeaseContractComponentsPage,
  /* IFRS16LeaseContractDeleteDialog, */
  IFRS16LeaseContractUpdatePage,
} from './ifrs-16-lease-contract.page-object';

const expect = chai.expect;

describe('IFRS16LeaseContract e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let iFRS16LeaseContractComponentsPage: IFRS16LeaseContractComponentsPage;
  let iFRS16LeaseContractUpdatePage: IFRS16LeaseContractUpdatePage;
  /* let iFRS16LeaseContractDeleteDialog: IFRS16LeaseContractDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IFRS16LeaseContracts', async () => {
    await navBarPage.goToEntity('ifrs-16-lease-contract');
    iFRS16LeaseContractComponentsPage = new IFRS16LeaseContractComponentsPage();
    await browser.wait(ec.visibilityOf(iFRS16LeaseContractComponentsPage.title), 5000);
    expect(await iFRS16LeaseContractComponentsPage.getTitle()).to.eq('IFRS 16 Lease Contracts');
    await browser.wait(
      ec.or(ec.visibilityOf(iFRS16LeaseContractComponentsPage.entities), ec.visibilityOf(iFRS16LeaseContractComponentsPage.noResult)),
      1000
    );
  });

  it('should load create IFRS16LeaseContract page', async () => {
    await iFRS16LeaseContractComponentsPage.clickOnCreateButton();
    iFRS16LeaseContractUpdatePage = new IFRS16LeaseContractUpdatePage();
    expect(await iFRS16LeaseContractUpdatePage.getPageTitle()).to.eq('Create or edit a IFRS 16 Lease Contract');
    await iFRS16LeaseContractUpdatePage.cancel();
  });

  /* it('should create and save IFRS16LeaseContracts', async () => {
        const nbButtonsBeforeCreate = await iFRS16LeaseContractComponentsPage.countDeleteButtons();

        await iFRS16LeaseContractComponentsPage.clickOnCreateButton();

        await promise.all([
            iFRS16LeaseContractUpdatePage.setBookingIdInput('bookingId'),
            iFRS16LeaseContractUpdatePage.setLeaseTitleInput('leaseTitle'),
            iFRS16LeaseContractUpdatePage.setShortTitleInput('shortTitle'),
            iFRS16LeaseContractUpdatePage.setDescriptionInput('description'),
            iFRS16LeaseContractUpdatePage.setInceptionDateInput('2000-12-31'),
            iFRS16LeaseContractUpdatePage.setCommencementDateInput('2000-12-31'),
            iFRS16LeaseContractUpdatePage.setSerialNumberInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            iFRS16LeaseContractUpdatePage.superintendentServiceOutletSelectLastOption(),
            iFRS16LeaseContractUpdatePage.mainDealerSelectLastOption(),
            iFRS16LeaseContractUpdatePage.firstReportingPeriodSelectLastOption(),
            iFRS16LeaseContractUpdatePage.lastReportingPeriodSelectLastOption(),
            iFRS16LeaseContractUpdatePage.leaseContractDocumentSelectLastOption(),
            iFRS16LeaseContractUpdatePage.leaseContractCalculationsSelectLastOption(),
            iFRS16LeaseContractUpdatePage.leaseTemplateSelectLastOption(),
        ]);

        await iFRS16LeaseContractUpdatePage.save();
        expect(await iFRS16LeaseContractUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await iFRS16LeaseContractComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last IFRS16LeaseContract', async () => {
        const nbButtonsBeforeDelete = await iFRS16LeaseContractComponentsPage.countDeleteButtons();
        await iFRS16LeaseContractComponentsPage.clickOnLastDeleteButton();

        iFRS16LeaseContractDeleteDialog = new IFRS16LeaseContractDeleteDialog();
        expect(await iFRS16LeaseContractDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this IFRS 16 Lease Contract?');
        await iFRS16LeaseContractDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(iFRS16LeaseContractComponentsPage.title), 5000);

        expect(await iFRS16LeaseContractComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
