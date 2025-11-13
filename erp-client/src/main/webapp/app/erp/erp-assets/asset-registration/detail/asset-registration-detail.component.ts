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

import { IAssetRegistration } from '../asset-registration.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  assetRegistrationCopyWorkflowInitiatedFromView,
  assetRegistrationEditWorkflowInitiatedFromView
} from '../../../store/actions/fixed-assets-register-update-status.actions';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';

@Component({
  selector: 'jhi-asset-registration-detail',
  templateUrl: './asset-registration-detail.component.html',
})
export class AssetRegistrationDetailComponent implements OnInit {
  assetRegistration: IAssetRegistration | null = null;
  assetAcquiringTransaction: ISettlement | null = null;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected settlementService: SettlementService,
    protected store: Store<State>) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetRegistration }) => {
      this.assetRegistration = assetRegistration;

      if (this.assetRegistration?.acquiringTransaction?.id) {
        this.settlementService.find(this.assetRegistration.acquiringTransaction.id)
          .subscribe(settlement => {
            this.assetAcquiringTransaction = settlement.body
          });
      }
    });
  }

  copyButtonEvent(instance: IAssetRegistration): void {
    this.store.dispatch(assetRegistrationCopyWorkflowInitiatedFromView({copiedInstance: instance}));
  }

  editButtonEvent(instance: IAssetRegistration): void {
    this.store.dispatch(assetRegistrationEditWorkflowInitiatedFromView({editedInstance: instance}));
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
