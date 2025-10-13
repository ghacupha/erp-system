///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { TaxReferenceComponentsPage, TaxReferenceDeleteDialog, TaxReferenceUpdatePage } from './tax-reference.page-object';

const expect = chai.expect;

describe('TaxReference e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let taxReferenceComponentsPage: TaxReferenceComponentsPage;
  let taxReferenceUpdatePage: TaxReferenceUpdatePage;
  let taxReferenceDeleteDialog: TaxReferenceDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TaxReferences', async () => {
    await navBarPage.goToEntity('tax-reference');
    taxReferenceComponentsPage = new TaxReferenceComponentsPage();
    await browser.wait(ec.visibilityOf(taxReferenceComponentsPage.title), 5000);
    expect(await taxReferenceComponentsPage.getTitle()).to.eq('Tax References');
    await browser.wait(
      ec.or(ec.visibilityOf(taxReferenceComponentsPage.entities), ec.visibilityOf(taxReferenceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TaxReference page', async () => {
    await taxReferenceComponentsPage.clickOnCreateButton();
    taxReferenceUpdatePage = new TaxReferenceUpdatePage();
    expect(await taxReferenceUpdatePage.getPageTitle()).to.eq('Create or edit a Tax Reference');
    await taxReferenceUpdatePage.cancel();
  });

  it('should create and save TaxReferences', async () => {
    const nbButtonsBeforeCreate = await taxReferenceComponentsPage.countDeleteButtons();

    await taxReferenceComponentsPage.clickOnCreateButton();

    await promise.all([
      taxReferenceUpdatePage.setTaxNameInput('taxName'),
      taxReferenceUpdatePage.setTaxDescriptionInput('taxDescription'),
      taxReferenceUpdatePage.setTaxPercentageInput('5'),
      taxReferenceUpdatePage.taxReferenceTypeSelectLastOption(),
      taxReferenceUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      taxReferenceUpdatePage.setCompilationTokenInput('compilationToken'),
      // taxReferenceUpdatePage.placeholderSelectLastOption(),
    ]);

    await taxReferenceUpdatePage.save();
    expect(await taxReferenceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await taxReferenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last TaxReference', async () => {
    const nbButtonsBeforeDelete = await taxReferenceComponentsPage.countDeleteButtons();
    await taxReferenceComponentsPage.clickOnLastDeleteButton();

    taxReferenceDeleteDialog = new TaxReferenceDeleteDialog();
    expect(await taxReferenceDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Tax Reference?');
    await taxReferenceDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(taxReferenceComponentsPage.title), 5000);

    expect(await taxReferenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
