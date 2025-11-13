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

import { ILeaseLiabilityScheduleItem, LeaseLiabilityScheduleItem } from '../lease-liability-schedule-item.model';
import { LeaseLiabilityScheduleItemService } from '../service/lease-liability-schedule-item.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { ILeaseAmortizationSchedule } from 'app/entities/leases/lease-amortization-schedule/lease-amortization-schedule.model';
import { LeaseAmortizationScheduleService } from 'app/entities/leases/lease-amortization-schedule/service/lease-amortization-schedule.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ILeaseLiability } from 'app/entities/leases/lease-liability/lease-liability.model';
import { LeaseLiabilityService } from 'app/entities/leases/lease-liability/service/lease-liability.service';
import { ILeaseRepaymentPeriod } from 'app/entities/leases/lease-repayment-period/lease-repayment-period.model';
import { LeaseRepaymentPeriodService } from 'app/entities/leases/lease-repayment-period/service/lease-repayment-period.service';

@Component({
  selector: 'jhi-lease-liability-schedule-item-update',
  templateUrl: './lease-liability-schedule-item-update.component.html',
})
export class LeaseLiabilityScheduleItemUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  leaseAmortizationSchedulesSharedCollection: ILeaseAmortizationSchedule[] = [];
  iFRS16LeaseContractsSharedCollection: IIFRS16LeaseContract[] = [];
  leaseLiabilitiesSharedCollection: ILeaseLiability[] = [];
  leaseRepaymentPeriodsSharedCollection: ILeaseRepaymentPeriod[] = [];

  editForm = this.fb.group({
    id: [],
    sequenceNumber: [],
    openingBalance: [],
    cashPayment: [],
    principalPayment: [],
    interestPayment: [],
    outstandingBalance: [],
    interestPayableOpening: [],
    interestAccrued: [],
    interestPayableClosing: [],
    placeholders: [],
    universallyUniqueMappings: [],
    leaseAmortizationSchedule: [],
    leaseContract: [null, Validators.required],
    leaseLiability: [null, Validators.required],
    leasePeriod: [null, Validators.required],
  });

  constructor(
    protected leaseLiabilityScheduleItemService: LeaseLiabilityScheduleItemService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected leaseAmortizationScheduleService: LeaseAmortizationScheduleService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected leaseLiabilityService: LeaseLiabilityService,
    protected leaseRepaymentPeriodService: LeaseRepaymentPeriodService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityScheduleItem }) => {
      this.updateForm(leaseLiabilityScheduleItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseLiabilityScheduleItem = this.createFromForm();
    if (leaseLiabilityScheduleItem.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseLiabilityScheduleItemService.update(leaseLiabilityScheduleItem));
    } else {
      this.subscribeToSaveResponse(this.leaseLiabilityScheduleItemService.create(leaseLiabilityScheduleItem));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackLeaseAmortizationScheduleById(index: number, item: ILeaseAmortizationSchedule): number {
    return item.id!;
  }

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  trackLeaseLiabilityById(index: number, item: ILeaseLiability): number {
    return item.id!;
  }

  trackLeaseRepaymentPeriodById(index: number, item: ILeaseRepaymentPeriod): number {
    return item.id!;
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

  getSelectedUniversallyUniqueMapping(
    option: IUniversallyUniqueMapping,
    selectedVals?: IUniversallyUniqueMapping[]
  ): IUniversallyUniqueMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseLiabilityScheduleItem>>): void {
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

  protected updateForm(leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem): void {
    this.editForm.patchValue({
      id: leaseLiabilityScheduleItem.id,
      sequenceNumber: leaseLiabilityScheduleItem.sequenceNumber,
      openingBalance: leaseLiabilityScheduleItem.openingBalance,
      cashPayment: leaseLiabilityScheduleItem.cashPayment,
      principalPayment: leaseLiabilityScheduleItem.principalPayment,
      interestPayment: leaseLiabilityScheduleItem.interestPayment,
      outstandingBalance: leaseLiabilityScheduleItem.outstandingBalance,
      interestPayableOpening: leaseLiabilityScheduleItem.interestPayableOpening,
      interestAccrued: leaseLiabilityScheduleItem.interestAccrued,
      interestPayableClosing: leaseLiabilityScheduleItem.interestPayableClosing,
      placeholders: leaseLiabilityScheduleItem.placeholders,
      universallyUniqueMappings: leaseLiabilityScheduleItem.universallyUniqueMappings,
      leaseAmortizationSchedule: leaseLiabilityScheduleItem.leaseAmortizationSchedule,
      leaseContract: leaseLiabilityScheduleItem.leaseContract,
      leaseLiability: leaseLiabilityScheduleItem.leaseLiability,
      leasePeriod: leaseLiabilityScheduleItem.leasePeriod,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(leaseLiabilityScheduleItem.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(leaseLiabilityScheduleItem.universallyUniqueMappings ?? [])
    );
    this.leaseAmortizationSchedulesSharedCollection =
      this.leaseAmortizationScheduleService.addLeaseAmortizationScheduleToCollectionIfMissing(
        this.leaseAmortizationSchedulesSharedCollection,
        leaseLiabilityScheduleItem.leaseAmortizationSchedule
      );
    this.iFRS16LeaseContractsSharedCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.iFRS16LeaseContractsSharedCollection,
      leaseLiabilityScheduleItem.leaseContract
    );
    this.leaseLiabilitiesSharedCollection = this.leaseLiabilityService.addLeaseLiabilityToCollectionIfMissing(
      this.leaseLiabilitiesSharedCollection,
      leaseLiabilityScheduleItem.leaseLiability
    );
    this.leaseRepaymentPeriodsSharedCollection = this.leaseRepaymentPeriodService.addLeaseRepaymentPeriodToCollectionIfMissing(
      this.leaseRepaymentPeriodsSharedCollection,
      leaseLiabilityScheduleItem.leasePeriod
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('universallyUniqueMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.leaseAmortizationScheduleService
      .query()
      .pipe(map((res: HttpResponse<ILeaseAmortizationSchedule[]>) => res.body ?? []))
      .pipe(
        map((leaseAmortizationSchedules: ILeaseAmortizationSchedule[]) =>
          this.leaseAmortizationScheduleService.addLeaseAmortizationScheduleToCollectionIfMissing(
            leaseAmortizationSchedules,
            this.editForm.get('leaseAmortizationSchedule')!.value
          )
        )
      )
      .subscribe(
        (leaseAmortizationSchedules: ILeaseAmortizationSchedule[]) =>
          (this.leaseAmortizationSchedulesSharedCollection = leaseAmortizationSchedules)
      );

    this.iFRS16LeaseContractService
      .query()
      .pipe(map((res: HttpResponse<IIFRS16LeaseContract[]>) => res.body ?? []))
      .pipe(
        map((iFRS16LeaseContracts: IIFRS16LeaseContract[]) =>
          this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
            iFRS16LeaseContracts,
            this.editForm.get('leaseContract')!.value
          )
        )
      )
      .subscribe((iFRS16LeaseContracts: IIFRS16LeaseContract[]) => (this.iFRS16LeaseContractsSharedCollection = iFRS16LeaseContracts));

    this.leaseLiabilityService
      .query()
      .pipe(map((res: HttpResponse<ILeaseLiability[]>) => res.body ?? []))
      .pipe(
        map((leaseLiabilities: ILeaseLiability[]) =>
          this.leaseLiabilityService.addLeaseLiabilityToCollectionIfMissing(leaseLiabilities, this.editForm.get('leaseLiability')!.value)
        )
      )
      .subscribe((leaseLiabilities: ILeaseLiability[]) => (this.leaseLiabilitiesSharedCollection = leaseLiabilities));

    this.leaseRepaymentPeriodService
      .query()
      .pipe(map((res: HttpResponse<ILeaseRepaymentPeriod[]>) => res.body ?? []))
      .pipe(
        map((leaseRepaymentPeriods: ILeaseRepaymentPeriod[]) =>
          this.leaseRepaymentPeriodService.addLeaseRepaymentPeriodToCollectionIfMissing(
            leaseRepaymentPeriods,
            this.editForm.get('leasePeriod')!.value
          )
        )
      )
      .subscribe((leaseRepaymentPeriods: ILeaseRepaymentPeriod[]) => (this.leaseRepaymentPeriodsSharedCollection = leaseRepaymentPeriods));
  }

  protected createFromForm(): ILeaseLiabilityScheduleItem {
    return {
      ...new LeaseLiabilityScheduleItem(),
      id: this.editForm.get(['id'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      openingBalance: this.editForm.get(['openingBalance'])!.value,
      cashPayment: this.editForm.get(['cashPayment'])!.value,
      principalPayment: this.editForm.get(['principalPayment'])!.value,
      interestPayment: this.editForm.get(['interestPayment'])!.value,
      outstandingBalance: this.editForm.get(['outstandingBalance'])!.value,
      interestPayableOpening: this.editForm.get(['interestPayableOpening'])!.value,
      interestAccrued: this.editForm.get(['interestAccrued'])!.value,
      interestPayableClosing: this.editForm.get(['interestPayableClosing'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
      leaseAmortizationSchedule: this.editForm.get(['leaseAmortizationSchedule'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      leaseLiability: this.editForm.get(['leaseLiability'])!.value,
      leasePeriod: this.editForm.get(['leasePeriod'])!.value,
    };
  }
}
