///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
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

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
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
    ]);

    expect(await taxReferenceUpdatePage.getTaxNameInput()).to.eq('taxName', 'Expected TaxName value to be equals to taxName');
    expect(await taxReferenceUpdatePage.getTaxDescriptionInput()).to.eq(
      'taxDescription',
      'Expected TaxDescription value to be equals to taxDescription'
    );
    expect(await taxReferenceUpdatePage.getTaxPercentageInput()).to.eq('5', 'Expected taxPercentage value to be equals to 5');

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

    expect(await taxReferenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
