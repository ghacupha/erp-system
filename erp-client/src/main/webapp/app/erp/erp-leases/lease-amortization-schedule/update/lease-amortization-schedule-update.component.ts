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

import { ILeaseAmortizationSchedule, LeaseAmortizationSchedule } from '../lease-amortization-schedule.model';
import { LeaseAmortizationScheduleService } from '../service/lease-amortization-schedule.service';
import { ILeaseLiability } from '../../lease-liability/lease-liability.model';
import { IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { LeaseLiabilityService } from '../../lease-liability/service/lease-liability.service';
import { uuidv7 } from 'uuidv7';

@Component({
  selector: 'jhi-lease-amortization-schedule-update',
  templateUrl: './lease-amortization-schedule-update.component.html',
})
export class LeaseAmortizationScheduleUpdateComponent implements OnInit {
  isSaving = false;

  leaseLiabilitiesSharedCollection: ILeaseLiability[] = [];

  editForm = this.fb.group({
    id: [],
    identifier: [null, [Validators.required]],
    leaseLiability: [null, Validators.required],
    leaseContract: [null, Validators.required],
  });

  constructor(
    protected leaseAmortizationScheduleService: LeaseAmortizationScheduleService,
    protected leaseLiabilityService: LeaseLiabilityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseAmortizationSchedule }) => {

      if (leaseAmortizationSchedule.id === undefined) {
        leaseAmortizationSchedule.identifier = uuidv7();
      }

      this.updateForm(leaseAmortizationSchedule);

    });
  }

  updateLeaseLiability(value: ILeaseLiability): void {
    this.editForm.patchValue({
      leaseLiability: value
    });
  }

  updateLeaseContract(value: IIFRS16LeaseContract): void {
    this.editForm.patchValue({
      leaseContract: value
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseAmortizationSchedule = this.createFromForm();
    if (leaseAmortizationSchedule.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseAmortizationScheduleService.update(leaseAmortizationSchedule));
    } else {
      this.subscribeToSaveResponse(this.leaseAmortizationScheduleService.create(leaseAmortizationSchedule));
    }
  }

  trackLeaseLiabilityById(index: number, item: ILeaseLiability): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseAmortizationSchedule>>): void {
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

  protected updateForm(leaseAmortizationSchedule: ILeaseAmortizationSchedule): void {
    this.editForm.patchValue({
      id: leaseAmortizationSchedule.id,
      identifier: leaseAmortizationSchedule.identifier,
      leaseLiability: leaseAmortizationSchedule.leaseLiability,
      leaseContract: leaseAmortizationSchedule.leaseContract,
    });

  }

  protected createFromForm(): ILeaseAmortizationSchedule {
    return {
      ...new LeaseAmortizationSchedule(),
      id: this.editForm.get(['id'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      leaseLiability: this.editForm.get(['leaseLiability'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
    };
  }
}
