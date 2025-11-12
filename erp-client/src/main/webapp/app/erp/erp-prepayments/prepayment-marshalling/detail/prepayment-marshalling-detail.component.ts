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

import { IPrepaymentMarshalling } from '../prepayment-marshalling.model';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  prepaymentMarshallingCopyWorkflowInitiatedFromView,
  prepaymentMarshallingEditWorkflowInitiatedFromView
} from '../../../store/actions/prepayment-marshalling-update-status.actions';

@Component({
  selector: 'jhi-prepayment-marshalling-detail',
  templateUrl: './prepayment-marshalling-detail.component.html',
})
export class PrepaymentMarshallingDetailComponent implements OnInit {
  prepaymentMarshalling: IPrepaymentMarshalling | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected store: Store<State>) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentMarshalling }) => {
      this.prepaymentMarshalling = prepaymentMarshalling;
    });
  }

  editButtonEvent(instance: IPrepaymentMarshalling): void {
    this.store.dispatch(prepaymentMarshallingEditWorkflowInitiatedFromView({editedInstance: instance}))
  }

  copyButtonEvent(instance: IPrepaymentMarshalling): void {
    this.store.dispatch(prepaymentMarshallingCopyWorkflowInitiatedFromView({copiedInstance: instance}))
  }

  previousState(): void {
    window.history.back();
  }
}
