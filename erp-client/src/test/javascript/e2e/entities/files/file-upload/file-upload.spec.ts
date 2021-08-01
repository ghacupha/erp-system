import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FileUploadComponentsPage, FileUploadDeleteDialog, FileUploadUpdatePage } from './file-upload.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('FileUpload e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fileUploadComponentsPage: FileUploadComponentsPage;
  let fileUploadUpdatePage: FileUploadUpdatePage;
  let fileUploadDeleteDialog: FileUploadDeleteDialog;
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

  it('should load FileUploads', async () => {
    await navBarPage.goToEntity('file-upload');
    fileUploadComponentsPage = new FileUploadComponentsPage();
    await browser.wait(ec.visibilityOf(fileUploadComponentsPage.title), 5000);
    expect(await fileUploadComponentsPage.getTitle()).to.eq('File Uploads');
    await browser.wait(ec.or(ec.visibilityOf(fileUploadComponentsPage.entities), ec.visibilityOf(fileUploadComponentsPage.noResult)), 1000);
  });

  it('should load create FileUpload page', async () => {
    await fileUploadComponentsPage.clickOnCreateButton();
    fileUploadUpdatePage = new FileUploadUpdatePage();
    expect(await fileUploadUpdatePage.getPageTitle()).to.eq('Create or edit a File Upload');
    await fileUploadUpdatePage.cancel();
  });

  it('should create and save FileUploads', async () => {
    const nbButtonsBeforeCreate = await fileUploadComponentsPage.countDeleteButtons();

    await fileUploadComponentsPage.clickOnCreateButton();

    await promise.all([
      fileUploadUpdatePage.setDescriptionInput('description'),
      fileUploadUpdatePage.setFileNameInput('fileName'),
      fileUploadUpdatePage.setPeriodFromInput('2000-12-31'),
      fileUploadUpdatePage.setPeriodToInput('2000-12-31'),
      fileUploadUpdatePage.setFileTypeIdInput('5'),
      fileUploadUpdatePage.setDataFileInput(absolutePath),
      fileUploadUpdatePage.getUploadSuccessfulInput().click(),
      fileUploadUpdatePage.getUploadProcessedInput().click(),
      fileUploadUpdatePage.setUploadTokenInput('uploadToken'),
    ]);

    await fileUploadUpdatePage.save();
    expect(await fileUploadUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fileUploadComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FileUpload', async () => {
    const nbButtonsBeforeDelete = await fileUploadComponentsPage.countDeleteButtons();
    await fileUploadComponentsPage.clickOnLastDeleteButton();

    fileUploadDeleteDialog = new FileUploadDeleteDialog();
    expect(await fileUploadDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this File Upload?');
    await fileUploadDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fileUploadComponentsPage.title), 5000);

    expect(await fileUploadComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
