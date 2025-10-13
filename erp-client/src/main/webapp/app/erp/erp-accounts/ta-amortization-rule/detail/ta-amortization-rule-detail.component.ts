///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { ITAAmortizationRule } from '../ta-amortization-rule.model';
import { IIFRS16LeaseContract } from '../../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import {
  ifrs16LeaseContractCopyWorkflowInitiatedFromView,
  ifrs16LeaseContractEditWorkflowInitiatedFromView
} from '../../../store/actions/ifrs16-lease-model-update-status.actions';
import {
  taAmortizationRuleCopyWorkflowInitiatedFromView,
  taAmortizationRuleEditWorkflowInitiatedFromView
} from '../../../store/actions/ta-amortization-update-status.actions';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';

@Component({
  selector: 'jhi-ta-amortization-rule-detail',
  templateUrl: './ta-amortization-rule-detail.component.html',
})
export class TAAmortizationRuleDetailComponent implements OnInit {
  tAAmortizationRule: ITAAmortizationRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected store: Store<State>) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tAAmortizationRule }) => {
      this.tAAmortizationRule = tAAmortizationRule;
    });
  }

  editButtonEvent(instance: IIFRS16LeaseContract): void {
    this.store.dispatch(taAmortizationRuleEditWorkflowInitiatedFromView({editedInstance: instance}))
  }

  copyButtonEvent(instance: IIFRS16LeaseContract): void {
    this.store.dispatch(taAmortizationRuleCopyWorkflowInitiatedFromView({copiedInstance: instance}))
  }

  previousState(): void {
    window.history.back();
  }
}
