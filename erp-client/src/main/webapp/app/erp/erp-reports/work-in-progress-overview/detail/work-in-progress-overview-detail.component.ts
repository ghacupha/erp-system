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
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressOverview } from '../work-in-progress-overview.model';
import * as dayjs from 'dayjs';
import { DATE_FORMAT } from '../../../../config/input.constants';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { WorkInProgressOverviewService } from '../service/work-in-progress-overview.service';
import { select, Store } from '@ngrx/store';
import { wipOverviewNavigationReportDateState } from '../../../store/selectors/report-navigation-profile-status.selector';
import { State } from '../../../store/global-store.definition';

@Component({
  selector: 'jhi-work-in-progress-overview-detail',
  templateUrl: './work-in-progress-overview-detail.component.html',
})
export class WorkInProgressOverviewDetailComponent implements OnInit {
  workInProgressOverview: IWorkInProgressOverview | null = null;

  selectedReportDate: dayjs.Dayjs = dayjs();

  reportDateControlInput$ = new Subject<dayjs.Dayjs>();
  selectedNavDate = dayjs().format(DATE_FORMAT);

  isLoading = false;

  constructor(
    protected store: Store<State>,
    protected workInProgressOverviewService: WorkInProgressOverviewService,
    protected activatedRoute: ActivatedRoute) {
    this.store.pipe(select(wipOverviewNavigationReportDateState)).subscribe(reportDate => this.selectedReportDate = dayjs(reportDate));
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressOverview }) => {
      this.workInProgressOverview = workInProgressOverview;
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
    this.workInProgressOverviewService.findByDate(reportDate.format(DATE_FORMAT))
      .subscribe(res => {
        this.workInProgressOverview = res.body;
      });
    this.isLoading = false;
  }

  previousState(): void {
    window.history.back();
  }
}
