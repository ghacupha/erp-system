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

import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';

import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import {
  ILeasePaymentUploadRecord,
  ILeasePaymentUploadRequest,
  ILeasePaymentUploadResponse,
} from './lease-payment-upload.model';
import { LeasePaymentUploadService } from './service/lease-payment-upload.service';

@Component({
  selector: 'jhi-lease-payment-upload',
  templateUrl: './lease-payment-upload.component.html',
})
export class LeasePaymentUploadComponent implements OnInit {
  isUploading = false;
  isLoadingUploads = false;
  uploads: ILeasePaymentUploadRecord[] = [];
  totalItems = 0;
  itemsPerPage = 5;
  page = 1;
  predicate = 'createdAt';
  ascending = false;
  uploadResponse?: ILeasePaymentUploadResponse | null;
  uploadError?: string | null;
  selectedFile?: File | null;
  selectedFileName?: string | null;

  editForm = this.fb.group({
    leaseContract: [null, Validators.required],
    launchBatchImmediately: [true],
  });

  constructor(private fb: FormBuilder, private uploadService: LeasePaymentUploadService) {}

  ngOnInit(): void {
    this.loadUploads();
  }

  onLeaseContractSelected(leaseContract: IIFRS16LeaseContract | null): void {
    this.editForm.patchValue({ leaseContract });
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
      this.uploadError = 'A lease contract selection is required.';
      return;
    }

    this.isUploading = true;
    this.uploadService
      .upload(request, this.selectedFile)
      .pipe(finalize(() => (this.isUploading = false)))
      .subscribe({
        next: response => {
          this.uploadResponse = response.body ?? null;
          this.loadUploads();
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

  deactivate(upload: ILeasePaymentUploadRecord): void {
    if (!upload.id) {
      return;
    }
    this.isLoadingUploads = true;
    this.uploadService
      .deactivate(upload.id)
      .pipe(finalize(() => (this.isLoadingUploads = false)))
      .subscribe({
        next: res => {
          if (res.body?.id) {
            this.uploads = this.uploads.map(existing => (existing.id === res.body!.id ? res.body! : existing));
          }
        },
        error: err => {
          this.uploadError = err?.error?.message ?? 'Failed to deactivate the upload.';
        },
      });
  }

  activate(upload: ILeasePaymentUploadRecord): void {
    if (!upload.id) {
      return;
    }
    this.isLoadingUploads = true;
    this.uploadService
      .activate(upload.id)
      .pipe(finalize(() => (this.isLoadingUploads = false)))
      .subscribe({
        next: res => {
          if (res.body?.id) {
            this.uploads = this.uploads.map(existing => (existing.id === res.body!.id ? res.body! : existing));
          }
        },
        error: err => {
          this.uploadError = err?.error?.message ?? 'Failed to activate the upload.';
        },
      });
  }

  trackById(_index: number, item: ILeasePaymentUploadRecord): number | undefined {
    return item.id;
  }

  loadUploads(page?: number): void {
    this.isLoadingUploads = true;
    const pageToLoad = page ?? this.page ?? 1;
    this.uploadService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .pipe(finalize(() => (this.isLoadingUploads = false)))
      .subscribe({
        next: response => {
          this.uploads = response.body ?? [];
          this.totalItems = Number(response.headers?.get('X-Total-Count') ?? this.uploads.length);
          this.page = pageToLoad;
        },
        error: err => {
          this.uploadError = err?.error?.message ?? 'Failed to load existing uploads.';
        },
      });
  }

  private sort(): string[] {
    return [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
  }

  private buildRequest(): ILeasePaymentUploadRequest | null {
    const leaseContract: IIFRS16LeaseContract | null = this.editForm.get('leaseContract')!.value;
    if (!leaseContract?.id) {
      return null;
    }

    return {
      leaseContractId: leaseContract.id,
      launchBatchImmediately: this.editForm.get('launchBatchImmediately')!.value ?? true,
    };
  }
}
