import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeaseAmortizationCalculation, LeaseAmortizationCalculation } from '../lease-amortization-calculation.model';
import { LeaseAmortizationCalculationService } from '../service/lease-amortization-calculation.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';

@Component({
  selector: 'jhi-lease-amortization-calculation-update',
  templateUrl: './lease-amortization-calculation-update.component.html',
})
export class LeaseAmortizationCalculationUpdateComponent implements OnInit {
  isSaving = false;

  leaseContractsCollection: IIFRS16LeaseContract[] = [];

  editForm = this.fb.group({
    id: [],
    interestRate: [],
    periodicity: [],
    leaseAmount: [],
    numberOfPeriods: [],
    leaseContract: [null, Validators.required],
  });

  constructor(
    protected leaseAmortizationCalculationService: LeaseAmortizationCalculationService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseAmortizationCalculation }) => {
      this.updateForm(leaseAmortizationCalculation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseAmortizationCalculation = this.createFromForm();
    if (leaseAmortizationCalculation.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseAmortizationCalculationService.update(leaseAmortizationCalculation));
    } else {
      this.subscribeToSaveResponse(this.leaseAmortizationCalculationService.create(leaseAmortizationCalculation));
    }
  }

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseAmortizationCalculation>>): void {
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

  protected updateForm(leaseAmortizationCalculation: ILeaseAmortizationCalculation): void {
    this.editForm.patchValue({
      id: leaseAmortizationCalculation.id,
      interestRate: leaseAmortizationCalculation.interestRate,
      periodicity: leaseAmortizationCalculation.periodicity,
      leaseAmount: leaseAmortizationCalculation.leaseAmount,
      numberOfPeriods: leaseAmortizationCalculation.numberOfPeriods,
      leaseContract: leaseAmortizationCalculation.leaseContract,
    });

    this.leaseContractsCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.leaseContractsCollection,
      leaseAmortizationCalculation.leaseContract
    );
  }

  protected loadRelationshipsOptions(): void {
    this.iFRS16LeaseContractService
      .query({ 'leaseAmortizationCalculationId.specified': 'false' })
      .pipe(map((res: HttpResponse<IIFRS16LeaseContract[]>) => res.body ?? []))
      .pipe(
        map((iFRS16LeaseContracts: IIFRS16LeaseContract[]) =>
          this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
            iFRS16LeaseContracts,
            this.editForm.get('leaseContract')!.value
          )
        )
      )
      .subscribe((iFRS16LeaseContracts: IIFRS16LeaseContract[]) => (this.leaseContractsCollection = iFRS16LeaseContracts));
  }

  protected createFromForm(): ILeaseAmortizationCalculation {
    return {
      ...new LeaseAmortizationCalculation(),
      id: this.editForm.get(['id'])!.value,
      interestRate: this.editForm.get(['interestRate'])!.value,
      periodicity: this.editForm.get(['periodicity'])!.value,
      leaseAmount: this.editForm.get(['leaseAmount'])!.value,
      numberOfPeriods: this.editForm.get(['numberOfPeriods'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
    };
  }
}
