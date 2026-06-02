import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ReportContentTypeComponentsPage,
  /* ReportContentTypeDeleteDialog, */
  ReportContentTypeUpdatePage,
} from './report-content-type.page-object';

const expect = chai.expect;

describe('ReportContentType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reportContentTypeComponentsPage: ReportContentTypeComponentsPage;
  let reportContentTypeUpdatePage: ReportContentTypeUpdatePage;
  /* let reportContentTypeDeleteDialog: ReportContentTypeDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ReportContentTypes', async () => {
    await navBarPage.goToEntity('report-content-type');
    reportContentTypeComponentsPage = new ReportContentTypeComponentsPage();
    await browser.wait(ec.visibilityOf(reportContentTypeComponentsPage.title), 5000);
    expect(await reportContentTypeComponentsPage.getTitle()).to.eq('Report Content Types');
    await browser.wait(
      ec.or(ec.visibilityOf(reportContentTypeComponentsPage.entities), ec.visibilityOf(reportContentTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ReportContentType page', async () => {
    await reportContentTypeComponentsPage.clickOnCreateButton();
    reportContentTypeUpdatePage = new ReportContentTypeUpdatePage();
    expect(await reportContentTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Report Content Type');
    await reportContentTypeUpdatePage.cancel();
  });

  /* it('should create and save ReportContentTypes', async () => {
        const nbButtonsBeforeCreate = await reportContentTypeComponentsPage.countDeleteButtons();

        await reportContentTypeComponentsPage.clickOnCreateButton();

        await promise.all([
            reportContentTypeUpdatePage.setReportTypeNameInput('reportTypeName'),
            reportContentTypeUpdatePage.setReportFileExtensionInput('reportFileExtension'),
            reportContentTypeUpdatePage.systemContentTypeSelectLastOption(),
            // reportContentTypeUpdatePage.placeholderSelectLastOption(),
        ]);

        await reportContentTypeUpdatePage.save();
        expect(await reportContentTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await reportContentTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ReportContentType', async () => {
        const nbButtonsBeforeDelete = await reportContentTypeComponentsPage.countDeleteButtons();
        await reportContentTypeComponentsPage.clickOnLastDeleteButton();

        reportContentTypeDeleteDialog = new ReportContentTypeDeleteDialog();
        expect(await reportContentTypeDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Report Content Type?');
        await reportContentTypeDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(reportContentTypeComponentsPage.title), 5000);

        expect(await reportContentTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
