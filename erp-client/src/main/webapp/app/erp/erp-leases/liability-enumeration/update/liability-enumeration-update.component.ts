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
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LiabilityEnumerationService } from '../service/liability-enumeration.service';
import { AlertService } from 'app/core/util/alert.service';
import { LiabilityEnumerationRequest, LiabilityEnumerationResponse } from '../liability-enumeration.model';
import { IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ILeasePaymentUploadRecord } from '../../lease-payment-upload/lease-payment-upload.model';

@Component({
  selector: 'jhi-liability-enumeration-update',
  templateUrl: './liability-enumeration-update.component.html',
})
export class LiabilityEnumerationUpdateComponent {
  isSaving = false;
  selectedLeaseContract?: IIFRS16LeaseContract;
  selectedLeasePaymentUpload?: ILeasePaymentUploadRecord;

  editForm = this.fb.group({
    leaseContractId: [null, [Validators.required]],
    leasePaymentUploadId: [null, [Validators.required]],
    interestRate: [null, [Validators.required]],
    timeGranularity: ['MONTHLY', [Validators.required]],
    active: [true],
  });

  constructor(
    protected liabilityEnumerationService: LiabilityEnumerationService,
    protected alertService: AlertService,
    protected fb: FormBuilder,
    protected router: Router,
    protected activatedRoute: ActivatedRoute
  ) {}

  previousState(): void {
    this.router.navigate(['../'], { relativeTo: this.activatedRoute });
  }

  save(): void {
    this.isSaving = true;
    const request = this.createFromForm();
    this.liabilityEnumerationService.enumerate(request).subscribe({
      next: (res: HttpResponse<LiabilityEnumerationResponse>) => this.onSaveSuccess(res.body),
      error: (err: HttpErrorResponse) => this.onSaveError(err),
    });
  }

  onLeaseContractSelected(contract?: IIFRS16LeaseContract): void {
    this.selectedLeaseContract = contract ?? {};
    this.editForm.get('leaseContractId')?.setValue(contract?.id ?? null);
    if (!contract?.id || this.selectedLeasePaymentUpload?.leaseContract?.id !== contract.id) {
      this.selectedLeasePaymentUpload = undefined;
      this.editForm.get('leasePaymentUploadId')?.reset();
    }
  }

  onLeasePaymentUploadSelected(upload?: ILeasePaymentUploadRecord): void {
    this.selectedLeasePaymentUpload = upload ?? {};
    this.editForm.get('leasePaymentUploadId')?.setValue(upload?.id ?? null);
    if (upload?.leaseContract?.id && !this.editForm.get('leaseContractId')?.value) {
      this.editForm.get('leaseContractId')?.setValue(upload.leaseContract.id);
    }
  }

  protected onSaveSuccess(response?: LiabilityEnumerationResponse | null): void {
    this.isSaving = false;
    if (response?.liabilityEnumerationId) {
      this.router.navigate(['../'], { relativeTo: this.activatedRoute });
    }
  }

  protected onSaveError(error: HttpErrorResponse): void {
    this.isSaving = false;
    this.alertService.addHttpErrorResponse(error);
  }

  protected createFromForm(): LiabilityEnumerationRequest {
    return {
      leaseContractId: this.editForm.get(['leaseContractId'])!.value,
      leasePaymentUploadId: this.editForm.get(['leasePaymentUploadId'])!.value,
      interestRate: this.editForm.get(['interestRate'])!.value,
      timeGranularity: this.editForm.get(['timeGranularity'])!.value,
      active: this.editForm.get(['active'])!.value,
    } as LiabilityEnumerationRequest;
  }
}
