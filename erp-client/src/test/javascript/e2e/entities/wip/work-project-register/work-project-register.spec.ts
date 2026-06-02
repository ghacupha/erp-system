import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  WorkProjectRegisterComponentsPage,
  /* WorkProjectRegisterDeleteDialog, */
  WorkProjectRegisterUpdatePage,
} from './work-project-register.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('WorkProjectRegister e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workProjectRegisterComponentsPage: WorkProjectRegisterComponentsPage;
  let workProjectRegisterUpdatePage: WorkProjectRegisterUpdatePage;
  /* let workProjectRegisterDeleteDialog: WorkProjectRegisterDeleteDialog; */
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

  it('should load WorkProjectRegisters', async () => {
    await navBarPage.goToEntity('work-project-register');
    workProjectRegisterComponentsPage = new WorkProjectRegisterComponentsPage();
    await browser.wait(ec.visibilityOf(workProjectRegisterComponentsPage.title), 5000);
    expect(await workProjectRegisterComponentsPage.getTitle()).to.eq('Work Project Registers');
    await browser.wait(
      ec.or(ec.visibilityOf(workProjectRegisterComponentsPage.entities), ec.visibilityOf(workProjectRegisterComponentsPage.noResult)),
      1000
    );
  });

  it('should load create WorkProjectRegister page', async () => {
    await workProjectRegisterComponentsPage.clickOnCreateButton();
    workProjectRegisterUpdatePage = new WorkProjectRegisterUpdatePage();
    expect(await workProjectRegisterUpdatePage.getPageTitle()).to.eq('Create or edit a Work Project Register');
    await workProjectRegisterUpdatePage.cancel();
  });

  /* it('should create and save WorkProjectRegisters', async () => {
        const nbButtonsBeforeCreate = await workProjectRegisterComponentsPage.countDeleteButtons();

        await workProjectRegisterComponentsPage.clickOnCreateButton();

        await promise.all([
            workProjectRegisterUpdatePage.setCatalogueNumberInput('catalogueNumber'),
            workProjectRegisterUpdatePage.setProjectTitleInput('projectTitle'),
            workProjectRegisterUpdatePage.setDescriptionInput('description'),
            workProjectRegisterUpdatePage.setDetailsInput(absolutePath),
            workProjectRegisterUpdatePage.setTotalProjectCostInput('5'),
            workProjectRegisterUpdatePage.setAdditionalNotesInput(absolutePath),
            // workProjectRegisterUpdatePage.dealersSelectLastOption(),
            workProjectRegisterUpdatePage.settlementCurrencySelectLastOption(),
            // workProjectRegisterUpdatePage.placeholderSelectLastOption(),
            // workProjectRegisterUpdatePage.businessDocumentSelectLastOption(),
        ]);

        await workProjectRegisterUpdatePage.save();
        expect(await workProjectRegisterUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await workProjectRegisterComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last WorkProjectRegister', async () => {
        const nbButtonsBeforeDelete = await workProjectRegisterComponentsPage.countDeleteButtons();
        await workProjectRegisterComponentsPage.clickOnLastDeleteButton();

        workProjectRegisterDeleteDialog = new WorkProjectRegisterDeleteDialog();
        expect(await workProjectRegisterDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Work Project Register?');
        await workProjectRegisterDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(workProjectRegisterComponentsPage.title), 5000);

        expect(await workProjectRegisterComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
