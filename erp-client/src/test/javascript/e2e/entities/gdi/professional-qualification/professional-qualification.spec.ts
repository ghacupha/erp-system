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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ProfessionalQualificationComponentsPage,
  ProfessionalQualificationDeleteDialog,
  ProfessionalQualificationUpdatePage,
} from './professional-qualification.page-object';

const expect = chai.expect;

describe('ProfessionalQualification e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let professionalQualificationComponentsPage: ProfessionalQualificationComponentsPage;
  let professionalQualificationUpdatePage: ProfessionalQualificationUpdatePage;
  let professionalQualificationDeleteDialog: ProfessionalQualificationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProfessionalQualifications', async () => {
    await navBarPage.goToEntity('professional-qualification');
    professionalQualificationComponentsPage = new ProfessionalQualificationComponentsPage();
    await browser.wait(ec.visibilityOf(professionalQualificationComponentsPage.title), 5000);
    expect(await professionalQualificationComponentsPage.getTitle()).to.eq('Professional Qualifications');
    await browser.wait(
      ec.or(
        ec.visibilityOf(professionalQualificationComponentsPage.entities),
        ec.visibilityOf(professionalQualificationComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create ProfessionalQualification page', async () => {
    await professionalQualificationComponentsPage.clickOnCreateButton();
    professionalQualificationUpdatePage = new ProfessionalQualificationUpdatePage();
    expect(await professionalQualificationUpdatePage.getPageTitle()).to.eq('Create or edit a Professional Qualification');
    await professionalQualificationUpdatePage.cancel();
  });

  it('should create and save ProfessionalQualifications', async () => {
    const nbButtonsBeforeCreate = await professionalQualificationComponentsPage.countDeleteButtons();

    await professionalQualificationComponentsPage.clickOnCreateButton();

    await promise.all([
      professionalQualificationUpdatePage.setProfessionalQualificationsCodeInput('professionalQualificationsCode'),
      professionalQualificationUpdatePage.setProfessionalQualificationsTypeInput('professionalQualificationsType'),
      professionalQualificationUpdatePage.setProfessionalQualificationsDetailsInput('professionalQualificationsDetails'),
    ]);

    await professionalQualificationUpdatePage.save();
    expect(await professionalQualificationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await professionalQualificationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ProfessionalQualification', async () => {
    const nbButtonsBeforeDelete = await professionalQualificationComponentsPage.countDeleteButtons();
    await professionalQualificationComponentsPage.clickOnLastDeleteButton();

    professionalQualificationDeleteDialog = new ProfessionalQualificationDeleteDialog();
    expect(await professionalQualificationDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Professional Qualification?'
    );
    await professionalQualificationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(professionalQualificationComponentsPage.title), 5000);

    expect(await professionalQualificationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
