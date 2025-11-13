///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  NbvCompilationJobComponentsPage,
  NbvCompilationJobDeleteDialog,
  NbvCompilationJobUpdatePage,
} from './nbv-compilation-job.page-object';

const expect = chai.expect;

describe('NbvCompilationJob e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nbvCompilationJobComponentsPage: NbvCompilationJobComponentsPage;
  let nbvCompilationJobUpdatePage: NbvCompilationJobUpdatePage;
  let nbvCompilationJobDeleteDialog: NbvCompilationJobDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NbvCompilationJobs', async () => {
    await navBarPage.goToEntity('nbv-compilation-job');
    nbvCompilationJobComponentsPage = new NbvCompilationJobComponentsPage();
    await browser.wait(ec.visibilityOf(nbvCompilationJobComponentsPage.title), 5000);
    expect(await nbvCompilationJobComponentsPage.getTitle()).to.eq('Nbv Compilation Jobs');
    await browser.wait(
      ec.or(ec.visibilityOf(nbvCompilationJobComponentsPage.entities), ec.visibilityOf(nbvCompilationJobComponentsPage.noResult)),
      1000
    );
  });

  it('should load create NbvCompilationJob page', async () => {
    await nbvCompilationJobComponentsPage.clickOnCreateButton();
    nbvCompilationJobUpdatePage = new NbvCompilationJobUpdatePage();
    expect(await nbvCompilationJobUpdatePage.getPageTitle()).to.eq('Create or edit a Nbv Compilation Job');
    await nbvCompilationJobUpdatePage.cancel();
  });

  it('should create and save NbvCompilationJobs', async () => {
    const nbButtonsBeforeCreate = await nbvCompilationJobComponentsPage.countDeleteButtons();

    await nbvCompilationJobComponentsPage.clickOnCreateButton();

    await promise.all([
      nbvCompilationJobUpdatePage.setCompilationJobIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      nbvCompilationJobUpdatePage.setCompilationJobTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      nbvCompilationJobUpdatePage.setEntitiesAffectedInput('5'),
      nbvCompilationJobUpdatePage.compilationStatusSelectLastOption(),
      nbvCompilationJobUpdatePage.setJobTitleInput('jobTitle'),
      nbvCompilationJobUpdatePage.setNumberOfBatchesInput('5'),
      nbvCompilationJobUpdatePage.setNumberOfProcessedBatchesInput('5'),
      nbvCompilationJobUpdatePage.setProcessingTimeInput('PT12S'),
      nbvCompilationJobUpdatePage.activePeriodSelectLastOption(),
      nbvCompilationJobUpdatePage.initiatedBySelectLastOption(),
    ]);

    await nbvCompilationJobUpdatePage.save();
    expect(await nbvCompilationJobUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await nbvCompilationJobComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last NbvCompilationJob', async () => {
    const nbButtonsBeforeDelete = await nbvCompilationJobComponentsPage.countDeleteButtons();
    await nbvCompilationJobComponentsPage.clickOnLastDeleteButton();

    nbvCompilationJobDeleteDialog = new NbvCompilationJobDeleteDialog();
    expect(await nbvCompilationJobDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Nbv Compilation Job?');
    await nbvCompilationJobDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(nbvCompilationJobComponentsPage.title), 5000);

    expect(await nbvCompilationJobComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
