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

import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { leasePeriodSelected } from '../../../store/actions/lease-id-report-view-update.action';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import { ILeasePeriod } from '../lease-period.model';
import {
  leasePeriodReportPath,
  leasePeriodReportTitle
} from 'app/erp/store/selectors/lease-period-report-path.selector';

@Component({
  selector: 'jhi-lease-period-report-nav-parameter',
  templateUrl: './lease-period-report-nav-parameter.component.html',
})
export class LeasePeriodReportNavParameterComponent {

  leasePeriodReportPath!: string;
  leasePeriodReportTitle!: string;

  editForm = this.fb.group({
    leasePeriod: [null, Validators.required],
  });

  constructor(
    protected fb: FormBuilder,
    protected store: Store<State>,
    protected router: Router
  ) {
    this.store.pipe(select(leasePeriodReportPath)).subscribe(reportPath => this.leasePeriodReportPath = reportPath);
    this.store.pipe(select(leasePeriodReportTitle)).subscribe(reportTitle => this.leasePeriodReportTitle = reportTitle);
  }

  updateLeasePeriod(update: ILeasePeriod): void {
    this.editForm.patchValue({
      leasePeriod: update
    });
  }

  previousState(): void {
    window.history.back();
  }

  navToReport(): void {

    const leasePeriodId: number| undefined = this.editForm.get(['leasePeriod'])!.value.id;

    // TODO pick this period from the actual report component
    this.store.dispatch(leasePeriodSelected({selectedLeasePeriodId: leasePeriodId}));

    // navigate to actual report-path
    this.router.navigate([this.leasePeriodReportPath])
  }
}
