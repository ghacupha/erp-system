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

import { IPrepaymentAccount } from '../prepayment-account.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  prepaymentAccountCopyWorkflowInitiatedFromView,
  prepaymentAccountEditWorkflowInitiatedFromView
} from '../../../store/actions/prepayment-account-update-status.actions';

@Component({
  selector: 'jhi-prepayment-account-detail',
  templateUrl: './prepayment-account-detail.component.html',
})
export class PrepaymentAccountDetailComponent implements OnInit {
  prepaymentAccount: IPrepaymentAccount | null = null;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected store: Store<State>,) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentAccount }) => {
      this.prepaymentAccount = prepaymentAccount;
    });
  }

  editButtonEvent(instance: IPrepaymentAccount): void {
    this.store.dispatch(prepaymentAccountEditWorkflowInitiatedFromView({editedInstance: instance}))
  }

  copyButtonEvent(instance: IPrepaymentAccount): void {
    this.store.dispatch(prepaymentAccountCopyWorkflowInitiatedFromView({copiedInstance: instance}))
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
