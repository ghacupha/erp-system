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

import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract.model';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  ifrs16LeaseContractCopyWorkflowInitiatedFromView,
  ifrs16LeaseContractEditWorkflowInitiatedFromView
} from '../../../store/actions/ifrs16-lease-model-update-status.actions';

@Component({
  selector: 'jhi-ifrs-16-lease-contract-detail',
  templateUrl: './ifrs-16-lease-contract-detail.component.html',
})
export class IFRS16LeaseContractDetailComponent implements OnInit {
  iFRS16LeaseContract: IIFRS16LeaseContract | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected store: Store<State>) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iFRS16LeaseContract }) => {
      this.iFRS16LeaseContract = iFRS16LeaseContract;
    });
  }

  editButtonEvent(instance: IIFRS16LeaseContract): void {
    this.store.dispatch(ifrs16LeaseContractEditWorkflowInitiatedFromView({editedInstance: instance}))
  }

  copyButtonEvent(instance: IIFRS16LeaseContract): void {
    this.store.dispatch(ifrs16LeaseContractCopyWorkflowInitiatedFromView({copiedInstance: instance}))
  }

  previousState(): void {
    window.history.back();
  }
}
