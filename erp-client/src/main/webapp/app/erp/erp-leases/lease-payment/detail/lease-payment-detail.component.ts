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

import { ILeasePayment } from '../lease-payment.model';
import { ILeaseLiability } from '../../lease-liability/lease-liability.model';
import {
  leaseLiabilityCopyWorkflowInitiatedFromView,
  leaseLiabilityEditWorkflowInitiatedFromView
} from '../../../store/actions/lease-liability.actions';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  leasePaymentCopyWorkflowInitiatedFromView,
  leasePaymentEditWorkflowInitiatedFromView
} from '../../../store/actions/lease-payment.actions';

@Component({
  selector: 'jhi-lease-payment-detail',
  templateUrl: './lease-payment-detail.component.html',
})
export class LeasePaymentDetailComponent implements OnInit {
  leasePayment: ILeasePayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected store: Store<State>) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasePayment }) => {
      this.leasePayment = leasePayment;
    });
  }

  previousState(): void {
    window.history.back();
  }

  editButtonEvent(instance: ILeasePayment): void {
    this.store.dispatch(leasePaymentEditWorkflowInitiatedFromView({editedInstance: instance}))
  }

  copyButtonEvent(instance: ILeasePayment): void {
    this.store.dispatch(leasePaymentCopyWorkflowInitiatedFromView({copiedInstance: instance}))
  }
}
