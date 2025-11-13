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
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { State } from '../../store/global-store.definition';
import {
  transactionAccountReportPath,
  transactionAccountReportTitle
} from '../../store/selectors/report-date-paramater.selector';
import { transactionAccountReportDateSelected } from '../../store/actions/report-date-parameter-status.action';
import dayjs from 'dayjs';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'jhi-report-date-parameter',
  templateUrl: './report-date-parameter.component.html',
})
export class ReportDateParameterComponent {

  reportPath!: string;
  reportTitle!: string;

  editForm = this.fb.group({
    reportDate: ['', Validators.required],
  });

  constructor(
    protected fb: FormBuilder,
    protected store: Store<State>,
    protected log: NGXLogger,
    protected router: Router
  ) {
    this.store.pipe(select(transactionAccountReportPath)).subscribe(reportPath => this.reportPath = reportPath);
    this.store.pipe(select(transactionAccountReportTitle)).subscribe(reportTitle => this.reportTitle = reportTitle);
  }

  previousState(): void {
    window.history.back();
  }

  navToReport(): void {
    const reportDate: string = this.editForm.get(['reportDate'])!.value;

    this.log.debug(`Report date selected: ${reportDate}`)

    this.store.dispatch(transactionAccountReportDateSelected({ selectedReportDate: dayjs(reportDate)}));

    // navigate to actual report-path
    this.router.navigate([this.reportPath]).then();
  }
}
