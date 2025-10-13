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
import { finalize, map } from 'rxjs/operators';

import { ILeaseLiability, LeaseLiability } from '../lease-liability.model';
import { LeaseLiabilityService } from '../service/lease-liability.service';
import { LeaseAmortizationCalculationService } from '../../lease-amortization-calculation/service/lease-amortization-calculation.service';
import { IFRS16LeaseContractService } from '../../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import {
  ILeaseAmortizationCalculation,
  LeaseAmortizationCalculation
} from '../../lease-amortization-calculation/lease-amortization-calculation.model';
import { IFRS16LeaseContract, IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingLeaseLiabilityStatus, creatingLeaseLiabilityStatus,
  editingLeaseLiabilityStatus, leaseLiabilitySelectedInstance
} from '../../../store/selectors/lease-liability-workflows-status.selector';

@Component({
  selector: 'jhi-lease-liability-update',
  templateUrl: './lease-liability-update.component.html'
})
export class LeaseLiabilityUpdateComponent implements OnInit {
  isSaving = false;

  leaseAmortizationCalculationsCollection: ILeaseAmortizationCalculation[] = [];
  leaseContractsCollection: IIFRS16LeaseContract[] = [];

  editForm = this.fb.group({
    id: [],
    leaseId: [null, [Validators.required]],
    liabilityAmount: [null, [Validators.required, Validators.min(0)]],
    interestRate: [null, [Validators.required, Validators.min(0)]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    leaseAmortizationCalculation: [],
    leaseContract: [null, Validators.required]
  });

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = { ...new LeaseLiability() };
  selectedLeaseContract = { ...new IFRS16LeaseContract() };
  selectedLeaseCalculation = { ...new LeaseAmortizationCalculation() };

  constructor(
    protected leaseLiabilityService: LeaseLiabilityService,
    protected leaseAmortizationCalculationService: LeaseAmortizationCalculationService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected fiscalMonthService: FiscalMonthService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>
  ) {
    this.store.pipe(select(copyingLeaseLiabilityStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingLeaseLiabilityStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingLeaseLiabilityStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(leaseLiabilitySelectedInstance)).subscribe(copied => {

      this.selectedItem = copied;

      if (copied.leaseContract?.id) {
        this.iFRS16LeaseContractService.find(copied.leaseContract.id).subscribe(leaseContractResponse => {
          if (leaseContractResponse.body) {
            this.selectedLeaseContract = leaseContractResponse.body;
          }
        });
      }

      if (copied.leaseAmortizationCalculation?.id) {
        this.leaseAmortizationCalculationService.find(copied.leaseAmortizationCalculation.id).subscribe(calc => {
          if (calc.body?.id != null) {
            this.leaseAmortizationCalculationService.find(calc.body.id).subscribe(calculationResponse => {
              if (calculationResponse.body) {
                this.selectedLeaseCalculation = calculationResponse.body;
              }
            });
          }
        });

      }
    });
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);

      this.updateDetailsGivenLeaseContract();
      this.updateCalculationsGivenLeaseContract();
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);

      this.updateDetailsGivenLeaseContract();
      this.updateCalculationsGivenLeaseContract();
    }

    if (this.weAreCreating) {

      this.updateDetailsGivenLeaseContract();
      this.updateCalculationsGivenLeaseContract();
    }
  }

  updateDetailsGivenLeaseContract(): void {
    this.editForm.get(['leaseContract'])?.valueChanges.subscribe((leaseContractChange) => {
      this.iFRS16LeaseContractService.find(leaseContractChange.id).subscribe((ifrs16Response) => {
        if (ifrs16Response.body) {
          const ifrs16 = ifrs16Response.body;

          this.editForm.patchValue({
            leaseId: ifrs16.bookingId,
            startDate: ifrs16.commencementDate
          });

          if (ifrs16.lastReportingPeriod) {
            this.fiscalMonthService.find(<number>ifrs16.lastReportingPeriod.id).subscribe(periodResponse => {
              if (periodResponse.body) {
                this.editForm.patchValue({
                  endDate: periodResponse.body.endDate
                });
              }
            });
          }
        }
      });
    });
  }

  updateCalculationsGivenLeaseContract(): void {
    this.editForm.get(['leaseContract'])?.valueChanges.subscribe((leaseContractChange) => {
      this.iFRS16LeaseContractService.find(leaseContractChange.id).subscribe((ifrs16Response) => {
        if (ifrs16Response.body) {
          const ifrs16 = ifrs16Response.body;

          if (ifrs16.id) {
            this.leaseAmortizationCalculationService.queryByLeaseContractId(ifrs16.id).subscribe(calcResponse => {
              if (calcResponse.body) {

                const calculation = calcResponse.body;

                this.editForm.patchValue({
                  // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
                  leaseAmortizationCalculation: calculation,
                  liabilityAmount: calculation.leaseAmount,
                  interestRate: calculation.interestRate
                });
              }
            });
          }
        }
      });
    });
  }

  updateLeaseAmortizationCalculation(value: ILeaseAmortizationCalculation): void {
    this.editForm.patchValue({
      leaseAmortizationCalculation: value
    });
  }

  updateLeaseContractMetadata(value: IIFRS16LeaseContract): void {
    this.editForm.patchValue({
      leaseContract: value
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leaseLiabilityService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leaseLiabilityService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leaseLiabilityService.create(this.copyFromForm()));
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
      interestRate: leaseLiability.interestRate,
      startDate: leaseLiability.startDate,
      endDate: leaseLiability.endDate,
      leaseAmortizationCalculation: leaseLiability.leaseAmortizationCalculation,
      leaseContract: leaseLiability.leaseContract
    });

  }

  protected createFromForm(): ILeaseLiability {
    return {
      ...new LeaseLiability(),
      id: this.editForm.get(['id'])!.value,
      leaseId: this.editForm.get(['leaseId'])!.value,
      liabilityAmount: this.editForm.get(['liabilityAmount'])!.value,
      interestRate: this.editForm.get(['interestRate'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      leaseAmortizationCalculation: this.editForm.get(['leaseAmortizationCalculation'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value
    };
  }

  protected copyFromForm(): ILeaseLiability {
    return {
      ...new LeaseLiability(),
      leaseId: this.editForm.get(['leaseId'])!.value,
      liabilityAmount: this.editForm.get(['liabilityAmount'])!.value,
      interestRate: this.editForm.get(['interestRate'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      leaseAmortizationCalculation: this.editForm.get(['leaseAmortizationCalculation'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value
    };
  }
}
