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

import { Component } from '@angular/core';
import { ActivatedRoute,  Router } from '@angular/router';

import { EventManager } from 'app/core/util/event-manager.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs';
import { DATE_FORMAT } from '../../../config/input.constants';
import { select, Store } from '@ngrx/store';
import { wipOverviewReportNavigationInitiatedFromReportDateModal } from '../../store/actions/report-navigation-profile-status.actions';
import { State } from '../../store/global-store.definition';
import {
  wipOverviewNavigationPathState,
  wipOverviewNavigationReportDateState, wipOverviewNavigationReportTitleState
} from '../../store/selectors/report-navigation-profile-status.selector';
import { Subject } from 'rxjs';

@Component({
  selector: 'jhi-report-date-parameter',
  templateUrl: './report-date-parameter.component.html',
})
export class ReportDateParameterComponent {
  isSaving = false;

  reportDate: dayjs.Dayjs = dayjs();

  reportPath = '';

  reportTitle = 'Report Parameter Selection';

  reportDateControlInput$ = new Subject<dayjs.Dayjs>();

  constructor(
    protected eventManager: EventManager,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected store: Store<State>,
    protected router: Router,
  ) {

    this.store.pipe(select(wipOverviewNavigationPathState)).subscribe(reportPath => this.reportPath = reportPath);
    this.store.pipe(select(wipOverviewNavigationReportDateState)).subscribe(reportDate => this.reportDate = dayjs(reportDate));
    this.store.pipe(select(wipOverviewNavigationReportTitleState)).subscribe(reportTitle => this.reportTitle = reportTitle);
  }

  onDateInputChange(): void {
    this.reportDateControlInput$.next(this.reportDate);
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmReportDate(): void {
    this.store.dispatch(wipOverviewReportNavigationInitiatedFromReportDateModal({wipOverviewReportDate: this.reportDate.format(DATE_FORMAT) }));

    this.router.navigate([this.reportPath, {reportDate: this.reportDate.format(DATE_FORMAT)}]).finally(() => {
      this.activeModal.close('dateSelected');
    });
  }

  previousState(): void {
    window.history.back();
  }
}
