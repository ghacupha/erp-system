import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  LeaseLiabilityByAccountReportComponentsPage,
  /* LeaseLiabilityByAccountReportDeleteDialog, */
  LeaseLiabilityByAccountReportUpdatePage,
} from './lease-liability-by-account-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('LeaseLiabilityByAccountReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityByAccountReportComponentsPage: LeaseLiabilityByAccountReportComponentsPage;
  let leaseLiabilityByAccountReportUpdatePage: LeaseLiabilityByAccountReportUpdatePage;
  /* let leaseLiabilityByAccountReportDeleteDialog: LeaseLiabilityByAccountReportDeleteDialog; */
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilityByAccountReports', async () => {
    await navBarPage.goToEntity('lease-liability-by-account-report');
    leaseLiabilityByAccountReportComponentsPage = new LeaseLiabilityByAccountReportComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityByAccountReportComponentsPage.title), 5000);
    expect(await leaseLiabilityByAccountReportComponentsPage.getTitle()).to.eq('Lease Liability By Account Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityByAccountReportComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityByAccountReportComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create LeaseLiabilityByAccountReport page', async () => {
    await leaseLiabilityByAccountReportComponentsPage.clickOnCreateButton();
    leaseLiabilityByAccountReportUpdatePage = new LeaseLiabilityByAccountReportUpdatePage();
    expect(await leaseLiabilityByAccountReportUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Liability By Account Report');
    await leaseLiabilityByAccountReportUpdatePage.cancel();
  });

  /* it('should create and save LeaseLiabilityByAccountReports', async () => {
        const nbButtonsBeforeCreate = await leaseLiabilityByAccountReportComponentsPage.countDeleteButtons();

        await leaseLiabilityByAccountReportComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseLiabilityByAccountReportUpdatePage.setReportIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityByAccountReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            leaseLiabilityByAccountReportUpdatePage.setFileChecksumInput('fileChecksum'),
            leaseLiabilityByAccountReportUpdatePage.getTamperedInput().click(),
            leaseLiabilityByAccountReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityByAccountReportUpdatePage.setReportParametersInput('reportParameters'),
            leaseLiabilityByAccountReportUpdatePage.setReportFileInput(absolutePath),
            leaseLiabilityByAccountReportUpdatePage.requestedBySelectLastOption(),
            leaseLiabilityByAccountReportUpdatePage.leasePeriodSelectLastOption(),
        ]);

        await leaseLiabilityByAccountReportUpdatePage.save();
        expect(await leaseLiabilityByAccountReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseLiabilityByAccountReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseLiabilityByAccountReport', async () => {
        const nbButtonsBeforeDelete = await leaseLiabilityByAccountReportComponentsPage.countDeleteButtons();
        await leaseLiabilityByAccountReportComponentsPage.clickOnLastDeleteButton();

        leaseLiabilityByAccountReportDeleteDialog = new LeaseLiabilityByAccountReportDeleteDialog();
        expect(await leaseLiabilityByAccountReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Liability By Account Report?');
        await leaseLiabilityByAccountReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseLiabilityByAccountReportComponentsPage.title), 5000);

        expect(await leaseLiabilityByAccountReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
