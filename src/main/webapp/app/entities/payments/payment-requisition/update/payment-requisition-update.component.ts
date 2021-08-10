import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaymentRequisition, PaymentRequisition } from '../payment-requisition.model';
import { PaymentRequisitionService } from '../service/payment-requisition.service';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { DealerService } from 'app/entities/dealers/dealer/service/dealer.service';

@Component({
  selector: 'gha-payment-requisition-update',
  templateUrl: './payment-requisition-update.component.html',
})
export class PaymentRequisitionUpdateComponent implements OnInit {
  isSaving = false;

  dealersSharedCollection: IDealer[] = [];

  editForm = this.fb.group({
    id: [],
    invoicedAmount: [],
    disbursementCost: [],
    vatableAmount: [],
    dealer: [],
  });

  constructor(
    protected paymentRequisitionService: PaymentRequisitionService,
    protected dealerService: DealerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentRequisition }) => {
      this.updateForm(paymentRequisition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentRequisition = this.createFromForm();
    if (paymentRequisition.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentRequisitionService.update(paymentRequisition));
    } else {
      this.subscribeToSaveResponse(this.paymentRequisitionService.create(paymentRequisition));
    }
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentRequisition>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(paymentRequisition: IPaymentRequisition): void {
    this.editForm.patchValue({
      id: paymentRequisition.id,
      invoicedAmount: paymentRequisition.invoicedAmount,
      disbursementCost: paymentRequisition.disbursementCost,
      vatableAmount: paymentRequisition.vatableAmount,
      dealer: paymentRequisition.dealer,
    });

    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      paymentRequisition.dealer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));
  }

  protected createFromForm(): IPaymentRequisition {
    return {
      ...new PaymentRequisition(),
      id: this.editForm.get(['id'])!.value,
      invoicedAmount: this.editForm.get(['invoicedAmount'])!.value,
      disbursementCost: this.editForm.get(['disbursementCost'])!.value,
      vatableAmount: this.editForm.get(['vatableAmount'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
    };
  }
}
