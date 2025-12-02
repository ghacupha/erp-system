import { Component } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LiabilityEnumerationService } from '../service/liability-enumeration.service';
import { AlertService } from 'app/core/util/alert.service';
import { LiabilityEnumerationRequest, LiabilityEnumerationResponse } from '../liability-enumeration.model';

@Component({
  selector: 'jhi-liability-enumeration-update',
  templateUrl: './liability-enumeration-update.component.html',
})
export class LiabilityEnumerationUpdateComponent {
  isSaving = false;

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
