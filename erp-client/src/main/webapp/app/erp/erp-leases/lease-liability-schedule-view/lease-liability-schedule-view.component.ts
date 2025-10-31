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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription, forkJoin, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { LeaseLiabilityService } from '../lease-liability/service/lease-liability.service';
import { LeaseLiabilityScheduleItemService } from '../lease-liability-schedule-item/service/lease-liability-schedule-item.service';
import { LeaseRepaymentPeriodService } from '../lease-repayment-period/service/lease-repayment-period.service';
import { IFRS16LeaseContractService } from '../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ILeaseLiability } from '../lease-liability/lease-liability.model';
import { ILeaseLiabilityScheduleItem } from '../lease-liability-schedule-item/lease-liability-schedule-item.model';
import { ILeaseRepaymentPeriod } from '../lease-repayment-period/lease-repayment-period.model';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';

interface LeaseLiabilityScheduleSummary {
  cashTotal: number;
  principalTotal: number;
  interestTotal: number;
  outstandingTotal: number;
  interestPayableTotal: number;
}

@Component({
  selector: 'jhi-lease-liability-schedule-view',
  templateUrl: './lease-liability-schedule-view.component.html',
})
export class LeaseLiabilityScheduleViewComponent implements OnInit, OnDestroy {
  leaseContract: IIFRS16LeaseContract | null = null;
  leaseLiability: ILeaseLiability | null = null;
  reportingPeriods: ILeaseRepaymentPeriod[] = [];
  activePeriod: ILeaseRepaymentPeriod | null = null;
  scheduleItems: ILeaseLiabilityScheduleItem[] = [];
  filteredItems: ILeaseLiabilityScheduleItem[] = [];
  summary: LeaseLiabilityScheduleSummary = this.createEmptySummary();
  loading = false;
  loadError?: string;

  private paramSubscription?: Subscription;

  constructor(
    private activatedRoute: ActivatedRoute,
    private leaseLiabilityService: LeaseLiabilityService,
    private leaseLiabilityScheduleItemService: LeaseLiabilityScheduleItemService,
    private leaseRepaymentPeriodService: LeaseRepaymentPeriodService,
    private leaseContractService: IFRS16LeaseContractService
  ) {}

  ngOnInit(): void {
    this.paramSubscription = this.activatedRoute.paramMap
      .pipe(
        switchMap(params => {
          const contractIdParam = params.get('contractId');
          if (!contractIdParam) {
            return of(null);
          }
          const contractId = Number(contractIdParam);
          if (Number.isNaN(contractId)) {
            return of(null);
          }
          this.loading = true;
          this.loadError = undefined;
          return forkJoin({
            contract: this.leaseContractService.find(contractId),
            liability: this.leaseLiabilityService.query({ 'leaseContractId.equals': contractId, size: 1 }),
            periods: this.leaseRepaymentPeriodService.query({
              'leaseContractId.equals': contractId,
              sort: ['sequenceNumber,asc'],
              size: 500,
            }),
            scheduleItems: this.leaseLiabilityScheduleItemService.query({
              'leaseContractId.equals': contractId,
              sort: ['sequenceNumber,asc'],
              size: 1000,
            }),
          });
        })
      )
      .subscribe({
        next: responses => {
          if (!responses) {
            this.loading = false;
            return;
          }
          this.leaseContract = responses.contract.body ?? null;
          this.leaseLiability = this.extractFirst(responses.liability);
          this.reportingPeriods = this.preparePeriods(responses.periods);
          this.scheduleItems = this.prepareScheduleItems(responses.scheduleItems);
          this.setDefaultActivePeriod();
          this.loading = false;
        },
        error: () => {
          this.loading = false;
          this.loadError = 'Unable to load lease liability dashboard data.';
        },
      });
  }

  ngOnDestroy(): void {
    if (this.paramSubscription) {
      this.paramSubscription.unsubscribe();
    }
  }

  get initialLiability(): number {
    return this.leaseLiability?.liabilityAmount ?? 0;
  }

  get startDate(): dayjs.Dayjs | undefined {
    return this.leaseLiability?.startDate ?? undefined;
  }

  get closeDate(): dayjs.Dayjs | undefined {
    if (this.activePeriod?.endDate) {
      return this.activePeriod.endDate;
    }
    return this.reportingPeriods.length > 0
      ? this.reportingPeriods[this.reportingPeriods.length - 1].endDate
      : this.leaseLiability?.endDate ?? undefined;
  }

  onPeriodChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement | null;
    const periodId = selectElement?.value;
    if (!periodId) {
      return;
    }
    const selected = this.reportingPeriods.find(period => `${period.id}` === periodId) ?? null;
    this.setActivePeriod(selected);
  }

  paymentDaysFromPrevious(index: number): number | null {
    if (index === 0) {
      return null;
    }
    const currentStart = this.filteredItems[index].leasePeriod?.startDate;
    const previousEnd = this.filteredItems[index - 1].leasePeriod?.endDate ?? this.filteredItems[index - 1].leasePeriod?.startDate;
    if (!currentStart || !previousEnd) {
      return null;
    }
    return currentStart.diff(previousEnd, 'day');
  }

  trackBySequence(_index: number, item: ILeaseLiabilityScheduleItem): number | string | undefined {
    return item.id ?? item.sequenceNumber ?? undefined;
  }

  private setDefaultActivePeriod(): void {
    if (this.reportingPeriods.length === 0) {
      this.setActivePeriod(null);
      return;
    }
    this.setActivePeriod(this.reportingPeriods[this.reportingPeriods.length - 1]);
  }

  private setActivePeriod(period: ILeaseRepaymentPeriod | null): void {
    this.activePeriod = period;
    this.filteredItems = this.filterItemsForPeriod(period);
    this.summary = this.computeSummary(this.filteredItems);
  }

  private filterItemsForPeriod(period: ILeaseRepaymentPeriod | null): ILeaseLiabilityScheduleItem[] {
    if (!period) {
      return [...this.scheduleItems];
    }
    return this.scheduleItems.filter(item => item.leasePeriod && item.leasePeriod.id === period.id);
  }

  private computeSummary(items: ILeaseLiabilityScheduleItem[]): LeaseLiabilityScheduleSummary {
    return items.reduce(
      (accumulator, item) => ({
        cashTotal: accumulator.cashTotal + (item.cashPayment ?? 0),
        principalTotal: accumulator.principalTotal + (item.principalPayment ?? 0),
        interestTotal: accumulator.interestTotal + (item.interestPayment ?? 0),
        outstandingTotal: accumulator.outstandingTotal + (item.outstandingBalance ?? 0),
        interestPayableTotal: accumulator.interestPayableTotal + (item.interestPayableClosing ?? 0),
      }),
      this.createEmptySummary()
    );
  }

  private extractFirst(response: HttpResponse<ILeaseLiability[]>): ILeaseLiability | null {
    const body = response.body ?? [];
    if (body.length === 0) {
      return null;
    }
    return body[0];
  }

  private preparePeriods(response: HttpResponse<ILeaseRepaymentPeriod[]>): ILeaseRepaymentPeriod[] {
    const periods = (response.body ?? []).map(period => ({
      ...period,
      startDate: period.startDate ? dayjs(period.startDate) : undefined,
      endDate: period.endDate ? dayjs(period.endDate) : undefined,
    }));
    return periods.sort((left, right) => {
      const leftSeq = left.sequenceNumber ?? 0;
      const rightSeq = right.sequenceNumber ?? 0;
      if (leftSeq === rightSeq) {
        const leftEnd = left.endDate ? left.endDate.valueOf() : 0;
        const rightEnd = right.endDate ? right.endDate.valueOf() : 0;
        return leftEnd - rightEnd;
      }
      return leftSeq - rightSeq;
    });
  }

  private prepareScheduleItems(response: HttpResponse<ILeaseLiabilityScheduleItem[]>): ILeaseLiabilityScheduleItem[] {
    const items = (response.body ?? []).map(item => ({
      ...item,
      leasePeriod: item.leasePeriod
        ? {
            ...item.leasePeriod,
            startDate: item.leasePeriod.startDate ? dayjs(item.leasePeriod.startDate) : undefined,
            endDate: item.leasePeriod.endDate ? dayjs(item.leasePeriod.endDate) : undefined,
          }
        : undefined,
    }));
    return items.sort((left, right) => {
      const leftSeq = left.sequenceNumber ?? 0;
      const rightSeq = right.sequenceNumber ?? 0;
      return leftSeq - rightSeq;
    });
  }

  private createEmptySummary(): LeaseLiabilityScheduleSummary {
    return {
      cashTotal: 0,
      principalTotal: 0,
      interestTotal: 0,
      outstandingTotal: 0,
      interestPayableTotal: 0,
    };
  }
}
