import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDealer, Dealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { PaymentLabelService } from 'app/entities/payment-label/service/payment-label.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-dealer-update',
  templateUrl: './dealer-update.component.html',
})
export class DealerUpdateComponent implements OnInit {
  isSaving = false;

  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  dealersSharedCollection: IDealer[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    dealerName: [null, [Validators.required]],
    taxNumber: [],
    postalAddress: [],
    physicalAddress: [],
    accountName: [],
    accountNumber: [],
    bankersName: [],
    bankersBranch: [],
    bankersSwiftCode: [],
    paymentLabels: [],
    dealerGroup: [],
    placeholders: [],
  });

  constructor(
    protected dealerService: DealerService,
    protected paymentLabelService: PaymentLabelService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealer }) => {
      this.updateForm(dealer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dealer = this.createFromForm();
    if (dealer.id !== undefined) {
      this.subscribeToSaveResponse(this.dealerService.update(dealer));
    } else {
      this.subscribeToSaveResponse(this.dealerService.create(dealer));
    }
  }

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  getSelectedPaymentLabel(option: IPaymentLabel, selectedVals?: IPaymentLabel[]): IPaymentLabel {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDealer>>): void {
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

  protected updateForm(dealer: IDealer): void {
    this.editForm.patchValue({
      id: dealer.id,
      dealerName: dealer.dealerName,
      taxNumber: dealer.taxNumber,
      postalAddress: dealer.postalAddress,
      physicalAddress: dealer.physicalAddress,
      accountName: dealer.accountName,
      accountNumber: dealer.accountNumber,
      bankersName: dealer.bankersName,
      bankersBranch: dealer.bankersBranch,
      bankersSwiftCode: dealer.bankersSwiftCode,
      paymentLabels: dealer.paymentLabels,
      dealerGroup: dealer.dealerGroup,
      placeholders: dealer.placeholders,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(dealer.paymentLabels ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(this.dealersSharedCollection, dealer.dealerGroup);
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(dealer.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentLabelService
      .query()
      .pipe(map((res: HttpResponse<IPaymentLabel[]>) => res.body ?? []))
      .pipe(
        map((paymentLabels: IPaymentLabel[]) =>
          this.paymentLabelService.addPaymentLabelToCollectionIfMissing(paymentLabels, ...(this.editForm.get('paymentLabels')!.value ?? []))
        )
      )
      .subscribe((paymentLabels: IPaymentLabel[]) => (this.paymentLabelsSharedCollection = paymentLabels));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealerGroup')!.value))
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IDealer {
    return {
      ...new Dealer(),
      id: this.editForm.get(['id'])!.value,
      dealerName: this.editForm.get(['dealerName'])!.value,
      taxNumber: this.editForm.get(['taxNumber'])!.value,
      postalAddress: this.editForm.get(['postalAddress'])!.value,
      physicalAddress: this.editForm.get(['physicalAddress'])!.value,
      accountName: this.editForm.get(['accountName'])!.value,
      accountNumber: this.editForm.get(['accountNumber'])!.value,
      bankersName: this.editForm.get(['bankersName'])!.value,
      bankersBranch: this.editForm.get(['bankersBranch'])!.value,
      bankersSwiftCode: this.editForm.get(['bankersSwiftCode'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      dealerGroup: this.editForm.get(['dealerGroup'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
