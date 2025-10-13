///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { finalize } from 'rxjs/operators';

import { ILeaseAmortizationCalculation, LeaseAmortizationCalculation } from '../lease-amortization-calculation.model';
import { LeaseAmortizationCalculationService } from '../service/lease-amortization-calculation.service';
import { IFRS16LeaseContract, IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { select, Store } from '@ngrx/store';
import {
  copyingIFRS16LeaseContractStatus, creatingIFRS16LeaseContractStatus,
  editingIFRS16LeaseContractStatus, ifrs16LeaseContractUpdateSelectedInstance
} from '../../../store/selectors/ifrs16-lease-model-workflows-status.selector';
import {
  copyingLeaseAmortizationCalculationStatus, creatingLeaseAmortizationCalculationStatus,
  editingLeaseAmortizationCalculationStatus, leaseAmortizationCalculationSelectedInstance
} from '../../../store/selectors/lease-amortization-calculation-workflows-status.selector';
import { State } from '../../../store/global-store.definition';

@Component({
  selector: 'jhi-lease-amortization-calculation-update',
  templateUrl: './lease-amortization-calculation-update.component.html'
})
export class LeaseAmortizationCalculationUpdateComponent implements OnInit {
  isSaving = false;

  iFRS16LeaseContractsCollection: IIFRS16LeaseContract[] = [];

  editForm = this.fb.group({
    id: [],
    interestRate: [],
    periodicity: [],
    leaseAmount: [],
    numberOfPeriods: [],
    leaseContract: [null, Validators.required]
  });

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = { ...new LeaseAmortizationCalculation() };
  selectedLeaseContract = { ...new IFRS16LeaseContract() };

  constructor(
    protected leaseAmortizationCalculationService: LeaseAmortizationCalculationService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>
  ) {
    this.store.pipe(select(copyingLeaseAmortizationCalculationStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingLeaseAmortizationCalculationStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingLeaseAmortizationCalculationStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(leaseAmortizationCalculationSelectedInstance)).subscribe(copied => {
      this.selectedItem = copied;
      if (this.selectedItem.leaseContract?.id) {
        this.iFRS16LeaseContractService.find(this.selectedItem.leaseContract.id).subscribe(leaseContractResponse => {
          if (leaseContractResponse.body) {
            this.selectedLeaseContract = leaseContractResponse.body;
          }
        });
      }
    });
  }

  ngOnInit(): void {
    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);

      this.updateDetailsGivenLeaseContract();
    }

    if (this.weAreCreating) {

      this.updateDetailsGivenLeaseContract();
    }
  }

  updateDetailsGivenLeaseContract(): void {
    this.editForm.get(['leaseContract'])?.valueChanges.subscribe((leaseContractChange) => {
      this.iFRS16LeaseContractService.find(leaseContractChange.id).subscribe((ifrs16Response) => {
        if (ifrs16Response.body) {
          const ifrs16 = ifrs16Response.body;

          this.editForm.patchValue({
            // TODO
          });
        }
      });
    });
  }

  updateIFRS16LeaseContract(value: IIFRS16LeaseContract): void {
    this.editForm.patchValue({
      leaseContract: value
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leaseAmortizationCalculationService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leaseAmortizationCalculationService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leaseAmortizationCalculationService.create(this.copyFromForm()));
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
      leaseContract: leaseAmortizationCalculation.leaseContract
    });

  }

  protected createFromForm(): ILeaseAmortizationCalculation {
    return {
      ...new LeaseAmortizationCalculation(),
      id: this.editForm.get(['id'])!.value,
      interestRate: this.editForm.get(['interestRate'])!.value,
      periodicity: this.editForm.get(['periodicity'])!.value,
      leaseAmount: this.editForm.get(['leaseAmount'])!.value,
      numberOfPeriods: this.editForm.get(['numberOfPeriods'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value
    };
  }

  protected copyFromForm(): ILeaseAmortizationCalculation {
    return {
      ...new LeaseAmortizationCalculation(),
      interestRate: this.editForm.get(['interestRate'])!.value,
      periodicity: this.editForm.get(['periodicity'])!.value,
      leaseAmount: this.editForm.get(['leaseAmount'])!.value,
      numberOfPeriods: this.editForm.get(['numberOfPeriods'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value
    };
  }
}
