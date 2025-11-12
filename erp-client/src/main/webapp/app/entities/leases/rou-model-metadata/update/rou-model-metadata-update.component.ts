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

import { IRouModelMetadata, RouModelMetadata } from '../rou-model-metadata.model';
import { RouModelMetadataService } from '../service/rou-model-metadata.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';

@Component({
  selector: 'jhi-rou-model-metadata-update',
  templateUrl: './rou-model-metadata-update.component.html',
})
export class RouModelMetadataUpdateComponent implements OnInit {
  isSaving = false;

  iFRS16LeaseContractsSharedCollection: IIFRS16LeaseContract[] = [];
  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];

  editForm = this.fb.group({
    id: [],
    modelTitle: [null, [Validators.required]],
    modelVersion: [null, [Validators.required]],
    description: [],
    leaseTermPeriods: [null, [Validators.required]],
    leaseAmount: [null, [Validators.required, Validators.min(0)]],
    rouModelReference: [null, [Validators.required]],
    commencementDate: [],
    expirationDate: [],
    hasBeenFullyAmortised: [],
    hasBeenDecommissioned: [],
    ifrs16LeaseContract: [null, Validators.required],
    assetAccount: [null, Validators.required],
    depreciationAccount: [null, Validators.required],
    accruedDepreciationAccount: [null, Validators.required],
    assetCategory: [],
    documentAttachments: [],
  });

  constructor(
    protected rouModelMetadataService: RouModelMetadataService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected transactionAccountService: TransactionAccountService,
    protected assetCategoryService: AssetCategoryService,
    protected businessDocumentService: BusinessDocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouModelMetadata }) => {
      this.updateForm(rouModelMetadata);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rouModelMetadata = this.createFromForm();
    if (rouModelMetadata.id !== undefined) {
      this.subscribeToSaveResponse(this.rouModelMetadataService.update(rouModelMetadata));
    } else {
      this.subscribeToSaveResponse(this.rouModelMetadataService.create(rouModelMetadata));
    }
  }

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouModelMetadata>>): void {
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

  protected updateForm(rouModelMetadata: IRouModelMetadata): void {
    this.editForm.patchValue({
      id: rouModelMetadata.id,
      modelTitle: rouModelMetadata.modelTitle,
      modelVersion: rouModelMetadata.modelVersion,
      description: rouModelMetadata.description,
      leaseTermPeriods: rouModelMetadata.leaseTermPeriods,
      leaseAmount: rouModelMetadata.leaseAmount,
      rouModelReference: rouModelMetadata.rouModelReference,
      commencementDate: rouModelMetadata.commencementDate,
      expirationDate: rouModelMetadata.expirationDate,
      hasBeenFullyAmortised: rouModelMetadata.hasBeenFullyAmortised,
      hasBeenDecommissioned: rouModelMetadata.hasBeenDecommissioned,
      ifrs16LeaseContract: rouModelMetadata.ifrs16LeaseContract,
      assetAccount: rouModelMetadata.assetAccount,
      depreciationAccount: rouModelMetadata.depreciationAccount,
      accruedDepreciationAccount: rouModelMetadata.accruedDepreciationAccount,
      assetCategory: rouModelMetadata.assetCategory,
      documentAttachments: rouModelMetadata.documentAttachments,
    });

    this.iFRS16LeaseContractsSharedCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.iFRS16LeaseContractsSharedCollection,
      rouModelMetadata.ifrs16LeaseContract
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      rouModelMetadata.assetAccount,
      rouModelMetadata.depreciationAccount,
      rouModelMetadata.accruedDepreciationAccount
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      rouModelMetadata.assetCategory
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(rouModelMetadata.documentAttachments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.iFRS16LeaseContractService
      .query()
      .pipe(map((res: HttpResponse<IIFRS16LeaseContract[]>) => res.body ?? []))
      .pipe(
        map((iFRS16LeaseContracts: IIFRS16LeaseContract[]) =>
          this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
            iFRS16LeaseContracts,
            this.editForm.get('ifrs16LeaseContract')!.value
          )
        )
      )
      .subscribe((iFRS16LeaseContracts: IIFRS16LeaseContract[]) => (this.iFRS16LeaseContractsSharedCollection = iFRS16LeaseContracts));

    this.transactionAccountService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('assetAccount')!.value,
            this.editForm.get('depreciationAccount')!.value,
            this.editForm.get('accruedDepreciationAccount')!.value
          )
        )
      )
      .subscribe((transactionAccounts: ITransactionAccount[]) => (this.transactionAccountsSharedCollection = transactionAccounts));

    this.assetCategoryService
      .query()
      .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
      .pipe(
        map((assetCategories: IAssetCategory[]) =>
          this.assetCategoryService.addAssetCategoryToCollectionIfMissing(assetCategories, this.editForm.get('assetCategory')!.value)
        )
      )
      .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            ...(this.editForm.get('documentAttachments')!.value ?? [])
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));
  }

  protected createFromForm(): IRouModelMetadata {
    return {
      ...new RouModelMetadata(),
      id: this.editForm.get(['id'])!.value,
      modelTitle: this.editForm.get(['modelTitle'])!.value,
      modelVersion: this.editForm.get(['modelVersion'])!.value,
      description: this.editForm.get(['description'])!.value,
      leaseTermPeriods: this.editForm.get(['leaseTermPeriods'])!.value,
      leaseAmount: this.editForm.get(['leaseAmount'])!.value,
      rouModelReference: this.editForm.get(['rouModelReference'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      hasBeenFullyAmortised: this.editForm.get(['hasBeenFullyAmortised'])!.value,
      hasBeenDecommissioned: this.editForm.get(['hasBeenDecommissioned'])!.value,
      ifrs16LeaseContract: this.editForm.get(['ifrs16LeaseContract'])!.value,
      assetAccount: this.editForm.get(['assetAccount'])!.value,
      depreciationAccount: this.editForm.get(['depreciationAccount'])!.value,
      accruedDepreciationAccount: this.editForm.get(['accruedDepreciationAccount'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      documentAttachments: this.editForm.get(['documentAttachments'])!.value,
    };
  }
}
