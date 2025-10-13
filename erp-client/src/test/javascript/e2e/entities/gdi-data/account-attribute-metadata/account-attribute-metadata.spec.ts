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

import {
  AccountAttributeMetadataComponentsPage,
  AccountAttributeMetadataDeleteDialog,
  AccountAttributeMetadataUpdatePage,
} from './account-attribute-metadata.page-object';

const expect = chai.expect;

describe('AccountAttributeMetadata e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let accountAttributeMetadataComponentsPage: AccountAttributeMetadataComponentsPage;
  let accountAttributeMetadataUpdatePage: AccountAttributeMetadataUpdatePage;
  let accountAttributeMetadataDeleteDialog: AccountAttributeMetadataDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AccountAttributeMetadata', async () => {
    await navBarPage.goToEntity('account-attribute-metadata');
    accountAttributeMetadataComponentsPage = new AccountAttributeMetadataComponentsPage();
    await browser.wait(ec.visibilityOf(accountAttributeMetadataComponentsPage.title), 5000);
    expect(await accountAttributeMetadataComponentsPage.getTitle()).to.eq('Account Attribute Metadata');
    await browser.wait(
      ec.or(
        ec.visibilityOf(accountAttributeMetadataComponentsPage.entities),
        ec.visibilityOf(accountAttributeMetadataComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create AccountAttributeMetadata page', async () => {
    await accountAttributeMetadataComponentsPage.clickOnCreateButton();
    accountAttributeMetadataUpdatePage = new AccountAttributeMetadataUpdatePage();
    expect(await accountAttributeMetadataUpdatePage.getPageTitle()).to.eq('Create or edit a Account Attribute Metadata');
    await accountAttributeMetadataUpdatePage.cancel();
  });

  it('should create and save AccountAttributeMetadata', async () => {
    const nbButtonsBeforeCreate = await accountAttributeMetadataComponentsPage.countDeleteButtons();

    await accountAttributeMetadataComponentsPage.clickOnCreateButton();

    await promise.all([
      accountAttributeMetadataUpdatePage.setPrecedenceInput('5'),
      accountAttributeMetadataUpdatePage.setColumnNameInput('columnName'),
      accountAttributeMetadataUpdatePage.setShortNameInput('shortName'),
      accountAttributeMetadataUpdatePage.setDetailedDefinitionInput('detailedDefinition'),
      accountAttributeMetadataUpdatePage.setDataTypeInput('dataType'),
      accountAttributeMetadataUpdatePage.setLengthInput('5'),
      accountAttributeMetadataUpdatePage.setColumnIndexInput('columnIndex'),
      accountAttributeMetadataUpdatePage.mandatoryFieldFlagSelectLastOption(),
      accountAttributeMetadataUpdatePage.setBusinessValidationInput('businessValidation'),
      accountAttributeMetadataUpdatePage.setTechnicalValidationInput('technicalValidation'),
      accountAttributeMetadataUpdatePage.setDbColumnNameInput('dbColumnName'),
      accountAttributeMetadataUpdatePage.setMetadataVersionInput('5'),
      accountAttributeMetadataUpdatePage.standardInputTemplateSelectLastOption(),
    ]);

    await accountAttributeMetadataUpdatePage.save();
    expect(await accountAttributeMetadataUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await accountAttributeMetadataComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AccountAttributeMetadata', async () => {
    const nbButtonsBeforeDelete = await accountAttributeMetadataComponentsPage.countDeleteButtons();
    await accountAttributeMetadataComponentsPage.clickOnLastDeleteButton();

    accountAttributeMetadataDeleteDialog = new AccountAttributeMetadataDeleteDialog();
    expect(await accountAttributeMetadataDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Account Attribute Metadata?'
    );
    await accountAttributeMetadataDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(accountAttributeMetadataComponentsPage.title), 5000);

    expect(await accountAttributeMetadataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
