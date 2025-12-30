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
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { forkJoin, Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRouModelMetadata, RouModelMetadata } from '../rou-model-metadata.model';
import { RouModelMetadataService } from '../service/rou-model-metadata.service';
import { uuidv7 as uuidv4 } from 'uuidv7';
import { IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IAssetCategory } from '../../../erp-assets/asset-category/asset-category.model';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { IFRS16LeaseContractService } from '../../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { AssetCategoryService } from '../../../erp-assets/asset-category/service/asset-category.service';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { TransactionAccountService } from '../../../erp-accounts/transaction-account/service/transaction-account.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingRouModelMetadataStatus, creatingRouModelMetadataStatus,
  editingRouModelMetadataStatus, rouModelMetadataUpdateSelectedInstance
} from '../../../store/selectors/rou-model-metadata-workflows-status.selector';
import { LeaseLiabilityService } from '../../lease-liability/service/lease-liability.service';
import { RouInitialDirectCostService } from '../../rou-initial-direct-cost/service/rou-initial-direct-cost.service';

@Component({
  selector: 'jhi-rou-model-metadata-update',
  templateUrl: './rou-model-metadata-update.component.html',
})
export class RouModelMetadataUpdateComponent implements OnInit {
  isSaving = false;

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new RouModelMetadata()}

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
    ifrs16LeaseContract: [null, Validators.required],
    assetAccount: [null, Validators.required],
    depreciationAccount: [null, Validators.required],
    accruedDepreciationAccount: [null, Validators.required],
    assetCategory: [],
    documentAttachments: [],
    commencementDate: [],
    expirationDate: [],
    hasBeenFullyAmortised: [],
    hasBeenDecommissioned: [],
  });

  constructor(
    protected rouModelMetadataService: RouModelMetadataService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected transactionAccountService: TransactionAccountService,
    protected assetCategoryService: AssetCategoryService,
    protected businessDocumentService: BusinessDocumentService,
    protected leaseLiabilityService: LeaseLiabilityService,
    protected rouInitialDirectCostService: RouInitialDirectCostService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingRouModelMetadataStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingRouModelMetadataStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingRouModelMetadataStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(rouModelMetadataUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);

      this.editForm.patchValue({
        rouModelReference: uuidv4(),
      })
    }

    if (this.weAreCopying) {
      this.copyForm(this.selectedItem);

      this.editForm.patchValue({
        rouModelReference: uuidv4(),
      })
    }

    if (this.weAreCreating) {

      this.editForm.patchValue({
        rouModelReference: uuidv4(),
      })

      this.loadRelationshipsOptions();
    }

    this.updateCalculationsGivenLeaseContract();
  }

  updateCalculationsGivenLeaseContract(): void {
    this.editForm.get(['ifrs16LeaseContract'])?.valueChanges.subscribe((leaseContractChange) => {
      if (!leaseContractChange?.id) {
        return;
      }

      const leaseContractId = leaseContractChange.id;

      this.iFRS16LeaseContractService.find(leaseContractId).subscribe((ifrs16Response) => {
        if (ifrs16Response.body) {
          const ifrs16 = ifrs16Response.body;

          this.editForm.patchValue({
            modelTitle: ifrs16.bookingId,
            description: ifrs16.description,
          });

          if (ifrs16.leaseTemplate) {
            this.editForm.patchValue({
              assetAccount: ifrs16.leaseTemplate.assetAccount,
              depreciationAccount: ifrs16.leaseTemplate.depreciationAccount,
              accruedDepreciationAccount: ifrs16.leaseTemplate.accruedDepreciationAccount,
              assetCategory: ifrs16.leaseTemplate.assetCategory,
            });
          }
        }
      });

      forkJoin({
        liabilities: this.leaseLiabilityService.query({ 'leaseContractId.equals': leaseContractId }),
        directCosts: this.rouInitialDirectCostService.query({ 'leaseContractId.equals': leaseContractId }),
      }).subscribe(({ liabilities, directCosts }) => {
        const liabilityAmount = liabilities.body?.[0]?.liabilityAmount ?? 0;
        const directCostAmount = (directCosts.body ?? []).reduce((sum, item) => sum + (item.cost ?? 0), 0);

        this.editForm.patchValue({
          leaseAmount: liabilityAmount + directCostAmount,
        });
      });
    });
  }

  updateContractMetadata(update: IIFRS16LeaseContract): void {
    this.editForm.patchValue({
      ifrs16LeaseContract: update
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateAssetAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({
      assetAccount: update,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateDepreciationAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({
      depreciationAccount: update,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateAccruedDepreciationAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({
      accruedDepreciationAccount: update,
    });
  }

  updateAssetCategory(updated: IAssetCategory): void {
    this.editForm.patchValue({ assetCategory: updated });
  }

  updateDocumentAttachments(updated: IBusinessDocument[]): void {
    this.editForm.patchValue({ documentAttachments: [...updated] });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.rouModelMetadataService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.rouModelMetadataService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.rouModelMetadataService.create(this.copyFromForm()));
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
      ifrs16LeaseContract: rouModelMetadata.ifrs16LeaseContract,
      assetAccount: rouModelMetadata.assetAccount,
      depreciationAccount: rouModelMetadata.depreciationAccount,
      accruedDepreciationAccount: rouModelMetadata.accruedDepreciationAccount,
      assetCategory: rouModelMetadata.assetCategory,
      documentAttachments: rouModelMetadata.documentAttachments,
      commencementDate: rouModelMetadata.commencementDate,
      expirationDate: rouModelMetadata.expirationDate,
      hasBeenFullyAmortised: rouModelMetadata.hasBeenFullyAmortised,
      hasBeenDecommissioned: rouModelMetadata.hasBeenDecommissioned,
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

  protected copyForm(rouModelMetadata: IRouModelMetadata): void {
    this.editForm.patchValue({
      id: rouModelMetadata.id,
      modelTitle: rouModelMetadata.modelTitle,
      modelVersion: rouModelMetadata.modelVersion,
      description: rouModelMetadata.description,
      leaseTermPeriods: rouModelMetadata.leaseTermPeriods,
      leaseAmount: rouModelMetadata.leaseAmount,
      rouModelReference: rouModelMetadata.rouModelReference,
      ifrs16LeaseContract: rouModelMetadata.ifrs16LeaseContract,
      assetAccount: rouModelMetadata.assetAccount,
      depreciationAccount: rouModelMetadata.depreciationAccount,
      accruedDepreciationAccount: rouModelMetadata.accruedDepreciationAccount,
      assetCategory: rouModelMetadata.assetCategory,
      documentAttachments: rouModelMetadata.documentAttachments,
      commencementDate: rouModelMetadata.commencementDate,
      expirationDate: rouModelMetadata.expirationDate,
      hasBeenFullyAmortised: rouModelMetadata.hasBeenFullyAmortised,
      hasBeenDecommissioned: rouModelMetadata.hasBeenDecommissioned,
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
      ifrs16LeaseContract: this.editForm.get(['ifrs16LeaseContract'])!.value,
      assetAccount: this.editForm.get(['assetAccount'])!.value,
      depreciationAccount: this.editForm.get(['depreciationAccount'])!.value,
      accruedDepreciationAccount: this.editForm.get(['accruedDepreciationAccount'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      documentAttachments: this.editForm.get(['documentAttachments'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      hasBeenFullyAmortised: this.editForm.get(['hasBeenFullyAmortised'])!.value,
      hasBeenDecommissioned: this.editForm.get(['hasBeenDecommissioned'])!.value,
    };
  }

  protected copyFromForm(): IRouModelMetadata {
    return {
      ...new RouModelMetadata(),
      modelTitle: this.editForm.get(['modelTitle'])!.value,
      modelVersion: this.editForm.get(['modelVersion'])!.value,
      description: this.editForm.get(['description'])!.value,
      leaseTermPeriods: this.editForm.get(['leaseTermPeriods'])!.value,
      leaseAmount: this.editForm.get(['leaseAmount'])!.value,
      rouModelReference: this.editForm.get(['rouModelReference'])!.value,
      ifrs16LeaseContract: this.editForm.get(['ifrs16LeaseContract'])!.value,
      assetAccount: this.editForm.get(['assetAccount'])!.value,
      depreciationAccount: this.editForm.get(['depreciationAccount'])!.value,
      accruedDepreciationAccount: this.editForm.get(['accruedDepreciationAccount'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      documentAttachments: this.editForm.get(['documentAttachments'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      hasBeenFullyAmortised: this.editForm.get(['hasBeenFullyAmortised'])!.value,
      hasBeenDecommissioned: this.editForm.get(['hasBeenDecommissioned'])!.value,
    };
  }
}
