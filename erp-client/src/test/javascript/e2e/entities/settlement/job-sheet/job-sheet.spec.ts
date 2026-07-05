import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  JobSheetComponentsPage,
  /* JobSheetDeleteDialog, */
  JobSheetUpdatePage,
} from './job-sheet.page-object';

const expect = chai.expect;

describe('JobSheet e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let jobSheetComponentsPage: JobSheetComponentsPage;
  let jobSheetUpdatePage: JobSheetUpdatePage;
  /* let jobSheetDeleteDialog: JobSheetDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load JobSheets', async () => {
    await navBarPage.goToEntity('job-sheet');
    jobSheetComponentsPage = new JobSheetComponentsPage();
    await browser.wait(ec.visibilityOf(jobSheetComponentsPage.title), 5000);
    expect(await jobSheetComponentsPage.getTitle()).to.eq('Job Sheets');
    await browser.wait(ec.or(ec.visibilityOf(jobSheetComponentsPage.entities), ec.visibilityOf(jobSheetComponentsPage.noResult)), 1000);
  });

  it('should load create JobSheet page', async () => {
    await jobSheetComponentsPage.clickOnCreateButton();
    jobSheetUpdatePage = new JobSheetUpdatePage();
    expect(await jobSheetUpdatePage.getPageTitle()).to.eq('Create or edit a Job Sheet');
    await jobSheetUpdatePage.cancel();
  });

  /* it('should create and save JobSheets', async () => {
        const nbButtonsBeforeCreate = await jobSheetComponentsPage.countDeleteButtons();

        await jobSheetComponentsPage.clickOnCreateButton();

        await promise.all([
            jobSheetUpdatePage.setSerialNumberInput('serialNumber'),
            jobSheetUpdatePage.setJobSheetDateInput('2000-12-31'),
            jobSheetUpdatePage.setDetailsInput('details'),
            jobSheetUpdatePage.setRemarksInput('remarks'),
            jobSheetUpdatePage.billerSelectLastOption(),
            // jobSheetUpdatePage.signatoriesSelectLastOption(),
            jobSheetUpdatePage.contactPersonSelectLastOption(),
            // jobSheetUpdatePage.businessStampsSelectLastOption(),
            // jobSheetUpdatePage.placeholderSelectLastOption(),
            // jobSheetUpdatePage.paymentLabelSelectLastOption(),
            // jobSheetUpdatePage.businessDocumentSelectLastOption(),
        ]);

        await jobSheetUpdatePage.save();
        expect(await jobSheetUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await jobSheetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last JobSheet', async () => {
        const nbButtonsBeforeDelete = await jobSheetComponentsPage.countDeleteButtons();
        await jobSheetComponentsPage.clickOnLastDeleteButton();

        jobSheetDeleteDialog = new JobSheetDeleteDialog();
        expect(await jobSheetDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Job Sheet?');
        await jobSheetDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(jobSheetComponentsPage.title), 5000);

        expect(await jobSheetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
