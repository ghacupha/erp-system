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
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentOutstandingOverviewReport } from '../prepayment-outstanding-overview-report.model';
import * as dayjs from 'dayjs';
import { Subject } from 'rxjs';
import { DATE_FORMAT } from '../../../../config/input.constants';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import { PrepaymentOutstandingOverviewReportService } from '../service/prepayment-outstanding-overview-report.service';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'jhi-prepayment-outstanding-overview-report-detail',
  templateUrl: './prepayment-outstanding-overview-report-detail.component.html',
})
export class PrepaymentOutstandingOverviewReportDetailComponent implements OnInit {
  prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport | null = null;

  selectedReportDate: dayjs.Dayjs = dayjs();

  reportDateControlInput$ = new Subject<dayjs.Dayjs>();
  selectedNavDate = dayjs().format(DATE_FORMAT);

  isLoading = false;

  constructor(
    protected store: Store<State>,
    protected prepaymentOutstandingOverviewReportService: PrepaymentOutstandingOverviewReportService,
    protected activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.activatedRoute.data.subscribe(({ prepaymentOutstandingOverviewReport }) => {
      this.prepaymentOutstandingOverviewReport = prepaymentOutstandingOverviewReport;
    });

    this.reportDateControlInput$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
    ).subscribe(() => this.reset(this.selectedReportDate));
  }

  onDateInputChange(): void {
    this.isLoading = true;
    this.reportDateControlInput$.next(this.selectedReportDate);
    this.selectedNavDate = this.selectedReportDate.format(DATE_FORMAT);
    this.isLoading = false;
  }

  reset(reportDate: dayjs.Dayjs): void {
    this.isLoading = true;
    this.prepaymentOutstandingOverviewReportService.findByDate(reportDate.format(DATE_FORMAT))
      .subscribe(res => {
        this.prepaymentOutstandingOverviewReport = res.body;
      });
    this.isLoading = false;
  }

  previousState(): void {
    window.history.back();
  }
}
