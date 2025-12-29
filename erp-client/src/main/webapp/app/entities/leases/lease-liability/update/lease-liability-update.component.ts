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
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeaseLiability, LeaseLiability } from '../lease-liability.model';
import { LeaseLiabilityService } from '../service/lease-liability.service';
import { ILeaseAmortizationCalculation } from 'app/entities/leases/lease-amortization-calculation/lease-amortization-calculation.model';
import { LeaseAmortizationCalculationService } from 'app/entities/leases/lease-amortization-calculation/service/lease-amortization-calculation.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';

@Component({
  selector: 'jhi-lease-liability-update',
  templateUrl: './lease-liability-update.component.html',
})
export class LeaseLiabilityUpdateComponent implements OnInit {
  isSaving = false;

  leaseAmortizationCalculationsCollection: ILeaseAmortizationCalculation[] = [];
  leaseContractsCollection: IIFRS16LeaseContract[] = [];

  editForm = this.fb.group({
    id: [],
    leaseId: [null, [Validators.required]],
    liabilityAmount: [null, [Validators.required, Validators.min(0)]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    interestRate: [null, [Validators.required, Validators.min(0)]],
    hasBeenAmortised: [null, [Validators.required]],
    hasBeenFullyAmortised: [null, [Validators.required]],
    leaseAmortizationCalculation: [],
    leaseContract: [null, Validators.required],
  });

  constructor(
    protected leaseLiabilityService: LeaseLiabilityService,
    protected leaseAmortizationCalculationService: LeaseAmortizationCalculationService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiability }) => {
      this.updateForm(leaseLiability);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseLiability = this.createFromForm();
    if (leaseLiability.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseLiabilityService.update(leaseLiability));
    } else {
      this.subscribeToSaveResponse(this.leaseLiabilityService.create(leaseLiability));
    }
  }

  trackLeaseAmortizationCalculationById(index: number, item: ILeaseAmortizationCalculation): number {
    return item.id!;
  }

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseLiability>>): void {
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

  protected updateForm(leaseLiability: ILeaseLiability): void {
    this.editForm.patchValue({
      id: leaseLiability.id,
      leaseId: leaseLiability.leaseId,
      liabilityAmount: leaseLiability.liabilityAmount,
      startDate: leaseLiability.startDate,
      endDate: leaseLiability.endDate,
      interestRate: leaseLiability.interestRate,
      hasBeenAmortised: leaseLiability.hasBeenAmortised,
      hasBeenFullyAmortised: leaseLiability.hasBeenFullyAmortised,
      leaseAmortizationCalculation: leaseLiability.leaseAmortizationCalculation,
      leaseContract: leaseLiability.leaseContract,
    });

    this.leaseAmortizationCalculationsCollection =
      this.leaseAmortizationCalculationService.addLeaseAmortizationCalculationToCollectionIfMissing(
        this.leaseAmortizationCalculationsCollection,
        leaseLiability.leaseAmortizationCalculation
      );
    this.leaseContractsCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.leaseContractsCollection,
      leaseLiability.leaseContract
    );
  }

  protected loadRelationshipsOptions(): void {
    this.leaseAmortizationCalculationService
      .query({ 'leaseLiabilityId.specified': 'false' })
      .pipe(map((res: HttpResponse<ILeaseAmortizationCalculation[]>) => res.body ?? []))
      .pipe(
        map((leaseAmortizationCalculations: ILeaseAmortizationCalculation[]) =>
          this.leaseAmortizationCalculationService.addLeaseAmortizationCalculationToCollectionIfMissing(
            leaseAmortizationCalculations,
            this.editForm.get('leaseAmortizationCalculation')!.value
          )
        )
      )
      .subscribe(
        (leaseAmortizationCalculations: ILeaseAmortizationCalculation[]) =>
          (this.leaseAmortizationCalculationsCollection = leaseAmortizationCalculations)
      );

    this.iFRS16LeaseContractService
      .query({ 'leaseLiabilityId.specified': 'false' })
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

  protected createFromForm(): ILeaseLiability {
    return {
      ...new LeaseLiability(),
      id: this.editForm.get(['id'])!.value,
      leaseId: this.editForm.get(['leaseId'])!.value,
      liabilityAmount: this.editForm.get(['liabilityAmount'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      interestRate: this.editForm.get(['interestRate'])!.value,
      hasBeenAmortised: this.editForm.get(['hasBeenAmortised'])!.value,
      hasBeenFullyAmortised: this.editForm.get(['hasBeenFullyAmortised'])!.value,
      leaseAmortizationCalculation: this.editForm.get(['leaseAmortizationCalculation'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
    };
  }
}
