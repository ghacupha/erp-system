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

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  leaseTemplateCopyWorkflowInitiatedFromView,
  leaseTemplateEditWorkflowInitiatedFromView
} from '../../../store/actions/lease-template-update-status.actions';
import { ILeaseTemplate } from '../lease-template.model';

@Component({
  selector: 'jhi-lease-template-detail',
  templateUrl: './lease-template-detail.component.html',
})
export class LeaseTemplateDetailComponent implements OnInit {
  leaseTemplate: ILeaseTemplate | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected store: Store<State>) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseTemplate }) => {
      this.leaseTemplate = leaseTemplate;
    });
  }

  editButtonEvent(instance: ILeaseTemplate): void {
    this.store.dispatch(leaseTemplateEditWorkflowInitiatedFromView({ editedInstance: instance }));
  }

  copyButtonEvent(instance: ILeaseTemplate): void {
    this.store.dispatch(leaseTemplateCopyWorkflowInitiatedFromView({ copiedInstance: instance }));
  }

  previousState(): void {
    window.history.back();
  }
}
