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
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWorkInProgressTransfer, WorkInProgressTransfer } from '../work-in-progress-transfer.model';
import { WorkInProgressTransferService } from '../service/work-in-progress-transfer.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IAssetCategory } from '../../asset-category/asset-category.model';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { WorkInProgressTransferType } from '../../../erp-common/enumerations/work-in-progress-transfer-type.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { IWorkInProgressRegistration } from '../../work-in-progress-registration/work-in-progress-registration.model';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import {
  WorkInProgressRegistrationService
} from '../../work-in-progress-registration/service/work-in-progress-registration.service';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { WorkProjectRegisterService } from '../../work-project-register/service/work-project-register.service';
import { IWorkProjectRegister } from '../../work-project-register/work-project-register.model';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';

@Component({
  selector: 'jhi-work-in-progress-transfer-update',
  templateUrl: './work-in-progress-transfer-update.component.html'
})
export class WorkInProgressTransferUpdateComponent implements OnInit {
  isSaving = false;
  workInProgressTransferTypeValues = Object.keys(WorkInProgressTransferType);

  placeholdersSharedCollection: IPlaceholder[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  workInProgressRegistrationsSharedCollection: IWorkInProgressRegistration[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  settlementsSharedCollection: ISettlement[] = [];
  workProjectRegistersSharedCollection: IWorkProjectRegister[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    targetAssetNumber: [],
    transferAmount: [null, [Validators.required]],
    transferDate: [null, [Validators.required]],
    transferType: [null, [Validators.required]],
    placeholders: [],
    businessDocuments: [],
    assetCategory: [],
    workInProgressRegistration: [],
    serviceOutlet: [],
    transferSettlement: [],
    originalSettlement: [],
    workProjectRegister: []
  });

  constructor(
    protected workInProgressTransferService: WorkInProgressTransferService,
    protected placeholderService: PlaceholderService,
    protected businessDocumentService: BusinessDocumentService,
    protected assetCategoryService: AssetCategoryService,
    protected workInProgressRegistrationService: WorkInProgressRegistrationService,
    protected serviceOutletService: ServiceOutletService,
    protected settlementService: SettlementService,
    protected workProjectRegisterService: WorkProjectRegisterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressTransfer }) => {

      if (workInProgressTransfer) {
        this.updateForm(workInProgressTransfer);
      }
    });

    this.updateDetailsGivenWIPRegistration();

    this.updateDetailsGivenTransferSettlement();

    this.updateTransferAmountGivenWIPRegistration();
  }

  updateDetailsGivenWIPRegistration(): void {
    this.editForm.get(['workInProgressRegistration'])?.valueChanges.subscribe((registration) => {
      this.settlementService.find(registration.settlementTransaction.id).subscribe((settlementTransactionResponse) => {
        this.editForm.patchValue({
          originalSettlement: settlementTransactionResponse.body
        });
      });
    });
  }

  updateTransferAmountGivenWIPRegistration(): void {
    this.editForm.get(['workInProgressRegistration'])?.valueChanges.subscribe((registration) => {
      this.workInProgressRegistrationService.find(registration.id).subscribe((registrationResponse) => {
        if (registrationResponse.body) {
          this.editForm.patchValue({
            transferAmount: registrationResponse.body.instalmentAmount,
            serviceOutlet: registrationResponse.body.outletCode
          });
        }
      });
    });
  }

  updateDetailsGivenTransferSettlement(): void {
    this.editForm.get(['transferSettlement'])?.valueChanges.subscribe((trfSettlement) => {
      this.settlementService.find(trfSettlement.id).subscribe((settlementTransactionResponse) => {
        if (settlementTransactionResponse.body) {
          const transferredSettlement = settlementTransactionResponse.body;

          this.editForm.patchValue({
            transferDate: transferredSettlement.paymentDate,
            description: transferredSettlement.description,
            businessDocuments: transferredSettlement.businessDocuments
          });
        }
      });
    });
  }

  updateAssetCategory(update: IAssetCategory): void {
    this.editForm.patchValue({
      assetCategory: update
    });
  }

  updateBusinessDocuments(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      businessDocuments: [...update]
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  updateServiceOutlet(update: IServiceOutlet): void {
    this.editForm.patchValue({
      serviceOutlet: update
    });
  }

  updateTransferSettlement(update: ISettlement): void {
    this.editForm.patchValue({
      transferSettlement: update
    });
  }

  updateOriginalSettlement(update: ISettlement): void {
    this.editForm.patchValue({
      originalSettlement: update
    });
  }

  updateWorkProjectRegister(update: IWorkProjectRegister): void {
    this.editForm.patchValue({
      workProjectRegister: update
    });
  }

  updateWIPRegistration(update: IWorkInProgressRegistration): void {
    this.editForm.patchValue({
      workInProgressRegistration: update
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workInProgressTransfer = this.createFromForm();
    if (workInProgressTransfer.id !== undefined) {
      this.subscribeToSaveResponse(this.workInProgressTransferService.update(workInProgressTransfer));
    } else {
      this.subscribeToSaveResponse(this.workInProgressTransferService.create(workInProgressTransfer));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
    return item.id!;
  }

  trackWorkInProgressRegistrationById(index: number, item: IWorkInProgressRegistration): number {
    return item.id!;
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackSettlementById(index: number, item: ISettlement): number {
    return item.id!;
  }

  trackWorkProjectRegisterById(index: number, item: IWorkProjectRegister): number {
    return item.id!;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedBusinessDocument(option: IBusinessDocument, selectedVals?: IBusinessDocument[]): IBusinessDocument {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkInProgressTransfer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(workInProgressTransfer: IWorkInProgressTransfer): void {
    this.editForm.patchValue({
      id: workInProgressTransfer.id,
      description: workInProgressTransfer.description,
      targetAssetNumber: workInProgressTransfer.targetAssetNumber,
      transferAmount: workInProgressTransfer.transferAmount,
      transferDate: workInProgressTransfer.transferDate,
      transferType: workInProgressTransfer.transferType,
      placeholders: workInProgressTransfer.placeholders,
      businessDocuments: workInProgressTransfer.businessDocuments,
      assetCategory: workInProgressTransfer.assetCategory,
      workInProgressRegistration: workInProgressTransfer.workInProgressRegistration,
      serviceOutlet: workInProgressTransfer.serviceOutlet,
      transferSettlement: workInProgressTransfer.transferSettlement,
      originalSettlement: workInProgressTransfer.originalSettlement,
      workProjectRegister: workInProgressTransfer.workProjectRegister
    });

  }

  protected createFromForm(): IWorkInProgressTransfer {
    return {
      ...new WorkInProgressTransfer(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      targetAssetNumber: this.editForm.get(['targetAssetNumber'])!.value,
      transferAmount: this.editForm.get(['transferAmount'])!.value,
      transferDate: this.editForm.get(['transferDate'])!.value,
      transferType: this.editForm.get(['transferType'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      workInProgressRegistration: this.editForm.get(['workInProgressRegistration'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      transferSettlement: this.editForm.get(['transferSettlement'])!.value,
      originalSettlement: this.editForm.get(['originalSettlement'])!.value,
      workProjectRegister: this.editForm.get(['workProjectRegister'])!.value
    };
  }
}
