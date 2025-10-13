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

import { ContractMetadataComponentsPage, ContractMetadataDeleteDialog, ContractMetadataUpdatePage } from './contract-metadata.page-object';

const expect = chai.expect;

describe('ContractMetadata e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contractMetadataComponentsPage: ContractMetadataComponentsPage;
  let contractMetadataUpdatePage: ContractMetadataUpdatePage;
  let contractMetadataDeleteDialog: ContractMetadataDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ContractMetadata', async () => {
    await navBarPage.goToEntity('contract-metadata');
    contractMetadataComponentsPage = new ContractMetadataComponentsPage();
    await browser.wait(ec.visibilityOf(contractMetadataComponentsPage.title), 5000);
    expect(await contractMetadataComponentsPage.getTitle()).to.eq('Contract Metadata');
    await browser.wait(
      ec.or(ec.visibilityOf(contractMetadataComponentsPage.entities), ec.visibilityOf(contractMetadataComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ContractMetadata page', async () => {
    await contractMetadataComponentsPage.clickOnCreateButton();
    contractMetadataUpdatePage = new ContractMetadataUpdatePage();
    expect(await contractMetadataUpdatePage.getPageTitle()).to.eq('Create or edit a Contract Metadata');
    await contractMetadataUpdatePage.cancel();
  });

  it('should create and save ContractMetadata', async () => {
    const nbButtonsBeforeCreate = await contractMetadataComponentsPage.countDeleteButtons();

    await contractMetadataComponentsPage.clickOnCreateButton();

    await promise.all([
      contractMetadataUpdatePage.setDescriptionInput('description'),
      contractMetadataUpdatePage.typeOfContractSelectLastOption(),
      contractMetadataUpdatePage.contractStatusSelectLastOption(),
      contractMetadataUpdatePage.setStartDateInput('2000-12-31'),
      contractMetadataUpdatePage.setTerminationDateInput('2000-12-31'),
      contractMetadataUpdatePage.setCommentsAndAttachmentInput('commentsAndAttachment'),
      contractMetadataUpdatePage.setContractTitleInput('contractTitle'),
      contractMetadataUpdatePage.setContractIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      contractMetadataUpdatePage.setContractIdentifierShortInput('contractIdentifierShort'),
      // contractMetadataUpdatePage.relatedContractsSelectLastOption(),
      contractMetadataUpdatePage.departmentSelectLastOption(),
      contractMetadataUpdatePage.contractPartnerSelectLastOption(),
      contractMetadataUpdatePage.responsiblePersonSelectLastOption(),
      // contractMetadataUpdatePage.signatorySelectLastOption(),
      contractMetadataUpdatePage.securityClearanceSelectLastOption(),
      // contractMetadataUpdatePage.placeholderSelectLastOption(),
      // contractMetadataUpdatePage.contractDocumentFileSelectLastOption(),
      // contractMetadataUpdatePage.contractMappingsSelectLastOption(),
    ]);

    await contractMetadataUpdatePage.save();
    expect(await contractMetadataUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await contractMetadataComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ContractMetadata', async () => {
    const nbButtonsBeforeDelete = await contractMetadataComponentsPage.countDeleteButtons();
    await contractMetadataComponentsPage.clickOnLastDeleteButton();

    contractMetadataDeleteDialog = new ContractMetadataDeleteDialog();
    expect(await contractMetadataDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Contract Metadata?');
    await contractMetadataDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(contractMetadataComponentsPage.title), 5000);

    expect(await contractMetadataComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
