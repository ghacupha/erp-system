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

import { element, by, ElementFinder } from 'protractor';

export class AcademicQualificationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-academic-qualification div table .btn-danger'));
  title = element.all(by.css('jhi-academic-qualification div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class AcademicQualificationUpdatePage {
  pageTitle = element(by.id('jhi-academic-qualification-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  academicQualificationsCodeInput = element(by.id('field_academicQualificationsCode'));
  academicQualificationTypeInput = element(by.id('field_academicQualificationType'));
  academicQualificationTypeDetailInput = element(by.id('field_academicQualificationTypeDetail'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAcademicQualificationsCodeInput(academicQualificationsCode: string): Promise<void> {
    await this.academicQualificationsCodeInput.sendKeys(academicQualificationsCode);
  }

  async getAcademicQualificationsCodeInput(): Promise<string> {
    return await this.academicQualificationsCodeInput.getAttribute('value');
  }

  async setAcademicQualificationTypeInput(academicQualificationType: string): Promise<void> {
    await this.academicQualificationTypeInput.sendKeys(academicQualificationType);
  }

  async getAcademicQualificationTypeInput(): Promise<string> {
    return await this.academicQualificationTypeInput.getAttribute('value');
  }

  async setAcademicQualificationTypeDetailInput(academicQualificationTypeDetail: string): Promise<void> {
    await this.academicQualificationTypeDetailInput.sendKeys(academicQualificationTypeDetail);
  }

  async getAcademicQualificationTypeDetailInput(): Promise<string> {
    return await this.academicQualificationTypeDetailInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class AcademicQualificationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-academicQualification-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-academicQualification'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
