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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { IFRS16LeaseContractService } from '../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import {
  RouDepreciationScheduleRow,
  RouDepreciationScheduleViewService,
} from './rou-depreciation-schedule-view.service';

@Component({
  selector: 'jhi-rou-depreciation-schedule-view',
  templateUrl: './rou-depreciation-schedule-view.component.html',
})
export class RouDepreciationScheduleViewComponent implements OnInit, OnDestroy {
  leaseContracts: IIFRS16LeaseContract[] = [];
  selectedContractId?: number;
  scheduleRows: RouDepreciationScheduleRow[] = [];
  loading = false;
  loadError?: string;

  private routeSubscription?: Subscription;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private leaseContractService: IFRS16LeaseContractService,
    private scheduleService: RouDepreciationScheduleViewService
  ) {}

  ngOnInit(): void {
    this.loadLeaseContracts();
    this.routeSubscription = this.activatedRoute.paramMap.subscribe(params => {
      const idParam = params.get('leaseContractId');
      if (!idParam) {
        this.selectedContractId = undefined;
        this.scheduleRows = [];
        return;
      }
      const parsedId = Number(idParam);
      if (Number.isNaN(parsedId)) {
        this.selectedContractId = undefined;
        this.scheduleRows = [];
        this.loadError = 'The provided lease contract identifier is invalid.';
        return;
      }
      if (this.selectedContractId !== parsedId) {
        this.selectedContractId = parsedId;
        this.fetchSchedule(parsedId);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSubscription?.unsubscribe();
  }

  trackContractById(_index: number, contract: IIFRS16LeaseContract): number | undefined {
    return contract.id;
  }

  trackRowById(_index: number, row: RouDepreciationScheduleRow): number | string | undefined {
    return row.entryId ?? row.sequenceNumber ?? row.periodCode;
  }

  onContractChange(event: Event): void {
    const select = event.target as HTMLSelectElement | null;
    const id = select?.value ? Number(select.value) : NaN;
    if (!Number.isNaN(id)) {
      void this.router.navigate(['/rou-depreciation-schedule-view', id]);
    }
  }

  get initialAmount(): number {
    return this.scheduleRows.length > 0 ? this.scheduleRows[0].initialAmount ?? 0 : 0;
  }

  get totalDepreciation(): number {
    return this.scheduleRows.reduce((sum, row) => sum + (row.depreciationAmount ?? 0), 0);
  }

  get closingBalance(): number {
    if (this.scheduleRows.length === 0) {
      return 0;
    }
    return this.scheduleRows[this.scheduleRows.length - 1].outstandingAmount ?? 0;
  }

  formatDate(value?: dayjs.Dayjs): string {
    return value ? value.format('DD MMM YYYY') : '';
  }

  private loadLeaseContracts(): void {
    this.leaseContractService
      .query({ sort: ['bookingId,asc'], size: 50 })
      .pipe(catchError(() => of({ body: [] })))
      .subscribe(response => {
        this.leaseContracts = response.body ?? [];
        if (this.selectedContractId) {
          const exists = this.leaseContracts.some(contract => contract.id === this.selectedContractId);
          if (!exists) {
            this.loadLeaseContractPlaceholder(this.selectedContractId);
          }
        }
      });
  }

  private loadLeaseContractPlaceholder(contractId: number): void {
    this.leaseContractService
      .find(contractId)
      .pipe(catchError(() => of({ body: undefined })))
      .subscribe(contractResponse => {
        const contract = contractResponse.body;
        if (contract) {
          this.leaseContracts = [...this.leaseContracts, contract];
        }
      });
  }

  private fetchSchedule(contractId: number): void {
    this.loading = true;
    this.loadError = undefined;
    this.scheduleService
      .loadSchedule(contractId)
      .pipe(
        catchError(() => {
          this.loadError = 'Unable to load depreciation schedule for the selected lease contract.';
          this.loading = false;
          return of([] as RouDepreciationScheduleRow[]);
        })
      )
      .subscribe(rows => {
        this.scheduleRows = rows;
        this.loading = false;
      });
  }
}
