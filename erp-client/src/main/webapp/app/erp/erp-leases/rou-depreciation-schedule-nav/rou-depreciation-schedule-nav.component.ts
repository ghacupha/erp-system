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
import { Store } from '@ngrx/store';

import { State } from '../../store/global-store.definition';
import { ifrs16LeaseContractReportSelected } from '../../store/actions/ifrs16-lease-contract-report.actions';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';

@Component({
  selector: 'jhi-rou-depreciation-schedule-nav',
  templateUrl: './rou-depreciation-schedule-nav.component.html',
})
export class RouDepreciationScheduleNavComponent {
  protected readonly reportPath = '/erp/rou-depreciation-schedule-view';

  editForm = this.fb.group({
    leaseContract: [null, Validators.required],
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly store: Store<State>,
    private readonly router: Router
  ) {}

  updateLeaseContract(contract: IIFRS16LeaseContract): void {
    this.editForm.patchValue({ leaseContract: contract });
  }

  cancel(): void {
    window.history.back();
  }

  launchReport(): void {
    const contract: IIFRS16LeaseContract | null = this.editForm.get('leaseContract')!.value;
    const contractId = contract?.id;

    if (contractId === undefined || contractId === null) {
      this.editForm.get('leaseContract')!.markAsTouched();
      return;
    }

    this.store.dispatch(ifrs16LeaseContractReportSelected({ selectedLeaseContractId: contractId }));
    this.router.navigate([this.reportPath, contractId]);
  }
}
