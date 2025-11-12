///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { ILeaseAmortizationSchedule, LeaseAmortizationSchedule } from '../lease-amortization-schedule.model';
import { LeaseAmortizationScheduleService } from '../service/lease-amortization-schedule.service';
import { ILeaseLiability } from 'app/entities/leases/lease-liability/lease-liability.model';
import { LeaseLiabilityService } from 'app/entities/leases/lease-liability/service/lease-liability.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';

@Component({
  selector: 'jhi-lease-amortization-schedule-update',
  templateUrl: './lease-amortization-schedule-update.component.html',
})
export class LeaseAmortizationScheduleUpdateComponent implements OnInit {
  isSaving = false;

  leaseLiabilitiesSharedCollection: ILeaseLiability[] = [];
  leaseContractsCollection: IIFRS16LeaseContract[] = [];

  editForm = this.fb.group({
    id: [],
    identifier: [null, [Validators.required]],
    leaseLiability: [null, Validators.required],
    leaseContract: [null, Validators.required],
  });

  constructor(
    protected leaseAmortizationScheduleService: LeaseAmortizationScheduleService,
    protected leaseLiabilityService: LeaseLiabilityService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseAmortizationSchedule }) => {
      this.updateForm(leaseAmortizationSchedule);

      this.loadRelationshipsOptions();
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

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
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

    this.leaseLiabilitiesSharedCollection = this.leaseLiabilityService.addLeaseLiabilityToCollectionIfMissing(
      this.leaseLiabilitiesSharedCollection,
      leaseAmortizationSchedule.leaseLiability
    );
    this.leaseContractsCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.leaseContractsCollection,
      leaseAmortizationSchedule.leaseContract
    );
  }

  protected loadRelationshipsOptions(): void {
    this.leaseLiabilityService
      .query()
      .pipe(map((res: HttpResponse<ILeaseLiability[]>) => res.body ?? []))
      .pipe(
        map((leaseLiabilities: ILeaseLiability[]) =>
          this.leaseLiabilityService.addLeaseLiabilityToCollectionIfMissing(leaseLiabilities, this.editForm.get('leaseLiability')!.value)
        )
      )
      .subscribe((leaseLiabilities: ILeaseLiability[]) => (this.leaseLiabilitiesSharedCollection = leaseLiabilities));

    this.iFRS16LeaseContractService
      .query({ 'leaseAmortizationScheduleId.specified': 'false' })
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
