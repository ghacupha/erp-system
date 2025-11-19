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

import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';

import { ILeaseLiability } from '../lease-liability/lease-liability.model';
import {
  ILeaseLiabilityScheduleUploadRequest,
  ILeaseLiabilityScheduleUploadResponse,
} from './lease-liability-schedule-upload.model';
import { LeaseLiabilityScheduleUploadService } from './service/lease-liability-schedule-upload.service';

@Component({
  selector: 'jhi-lease-liability-schedule-upload',
  templateUrl: './lease-liability-schedule-upload.component.html',
})
export class LeaseLiabilityScheduleUploadComponent {
  isUploading = false;
  uploadResponse?: ILeaseLiabilityScheduleUploadResponse | null;
  uploadError?: string | null;
  selectedFile?: File | null;
  selectedFileName?: string | null;

  editForm = this.fb.group({
    leaseLiability: [null, Validators.required],
    launchBatchImmediately: [true],
  });

  constructor(private fb: FormBuilder, private uploadService: LeaseLiabilityScheduleUploadService) {}

  onLeaseLiabilitySelected(leaseLiability: ILeaseLiability | null): void {
    this.editForm.patchValue({ leaseLiability });
  }

  onFileSelected(event: Event): void {
    const element = event.target as HTMLInputElement;
    if (element.files && element.files.length > 0) {
      this.selectedFile = element.files[0];
      this.selectedFileName = this.selectedFile.name;
    } else {
      this.selectedFile = null;
      this.selectedFileName = null;
    }
  }

  submitUpload(): void {
    this.uploadError = null;
    this.uploadResponse = null;
    if (!this.selectedFile) {
      this.uploadError = 'Please select a CSV file before submitting.';
      return;
    }

    const request = this.buildRequest();
    if (!request) {
      this.uploadError = 'A lease liability selection is required.';
      return;
    }

    this.isUploading = true;
    this.uploadService
      .upload(request, this.selectedFile)
      .pipe(finalize(() => (this.isUploading = false)))
      .subscribe({
        next: response => {
          this.uploadResponse = response.body ?? null;
        },
        error: err => {
          this.uploadError = err?.error?.message ?? 'The CSV upload failed. Please try again.';
        },
      });
  }

  clearUploadResponse(): void {
    this.uploadResponse = null;
    this.uploadError = null;
  }

  private buildRequest(): ILeaseLiabilityScheduleUploadRequest | null {
    const leaseLiability: ILeaseLiability | null = this.editForm.get('leaseLiability')!.value;
    if (!leaseLiability?.id) {
      return null;
    }

    return {
      leaseLiabilityId: leaseLiability.id,
      launchBatchImmediately: this.editForm.get('launchBatchImmediately')!.value ?? true,
    };
  }
}
