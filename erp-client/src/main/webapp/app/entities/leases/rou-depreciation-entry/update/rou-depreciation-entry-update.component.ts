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
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRouDepreciationEntry, RouDepreciationEntry } from '../rou-depreciation-entry.model';
import { RouDepreciationEntryService } from '../service/rou-depreciation-entry.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from 'app/entities/leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { IRouModelMetadata } from 'app/entities/leases/rou-model-metadata/rou-model-metadata.model';
import { RouModelMetadataService } from 'app/entities/leases/rou-model-metadata/service/rou-model-metadata.service';

@Component({
  selector: 'jhi-rou-depreciation-entry-update',
  templateUrl: './rou-depreciation-entry-update.component.html',
})
export class RouDepreciationEntryUpdateComponent implements OnInit {
  isSaving = false;

  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  iFRS16LeaseContractsSharedCollection: IIFRS16LeaseContract[] = [];
  rouModelMetadataSharedCollection: IRouModelMetadata[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    depreciationAmount: [null, [Validators.required, Validators.min(0)]],
    outstandingAmount: [null, [Validators.required, Validators.min(0)]],
    rouAssetIdentifier: [],
    rouDepreciationIdentifier: [null, [Validators.required]],
    sequenceNumber: [],
    invalidated: [],
    debitAccount: [null, Validators.required],
    creditAccount: [null, Validators.required],
    assetCategory: [null, Validators.required],
    leaseContract: [null, Validators.required],
    rouMetadata: [null, Validators.required],
  });

  constructor(
    protected rouDepreciationEntryService: RouDepreciationEntryService,
    protected transactionAccountService: TransactionAccountService,
    protected assetCategoryService: AssetCategoryService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected rouModelMetadataService: RouModelMetadataService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationEntry }) => {
      this.updateForm(rouDepreciationEntry);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rouDepreciationEntry = this.createFromForm();
    if (rouDepreciationEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.rouDepreciationEntryService.update(rouDepreciationEntry));
    } else {
      this.subscribeToSaveResponse(this.rouDepreciationEntryService.create(rouDepreciationEntry));
    }
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
    return item.id!;
  }

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  trackRouModelMetadataById(index: number, item: IRouModelMetadata): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouDepreciationEntry>>): void {
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

  protected updateForm(rouDepreciationEntry: IRouDepreciationEntry): void {
    this.editForm.patchValue({
      id: rouDepreciationEntry.id,
      description: rouDepreciationEntry.description,
      depreciationAmount: rouDepreciationEntry.depreciationAmount,
      outstandingAmount: rouDepreciationEntry.outstandingAmount,
      rouAssetIdentifier: rouDepreciationEntry.rouAssetIdentifier,
      rouDepreciationIdentifier: rouDepreciationEntry.rouDepreciationIdentifier,
      sequenceNumber: rouDepreciationEntry.sequenceNumber,
      invalidated: rouDepreciationEntry.invalidated,
      debitAccount: rouDepreciationEntry.debitAccount,
      creditAccount: rouDepreciationEntry.creditAccount,
      assetCategory: rouDepreciationEntry.assetCategory,
      leaseContract: rouDepreciationEntry.leaseContract,
      rouMetadata: rouDepreciationEntry.rouMetadata,
    });

    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      rouDepreciationEntry.debitAccount,
      rouDepreciationEntry.creditAccount
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      rouDepreciationEntry.assetCategory
    );
    this.iFRS16LeaseContractsSharedCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.iFRS16LeaseContractsSharedCollection,
      rouDepreciationEntry.leaseContract
    );
    this.rouModelMetadataSharedCollection = this.rouModelMetadataService.addRouModelMetadataToCollectionIfMissing(
      this.rouModelMetadataSharedCollection,
      rouDepreciationEntry.rouMetadata
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionAccountService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('debitAccount')!.value,
            this.editForm.get('creditAccount')!.value
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

    this.iFRS16LeaseContractService
      .query()
      .pipe(map((res: HttpResponse<IIFRS16LeaseContract[]>) => res.body ?? []))
      .pipe(
        map((iFRS16LeaseContracts: IIFRS16LeaseContract[]) =>
          this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
            iFRS16LeaseContracts,
            this.editForm.get('leaseContract')!.value
          )
        )
      )
      .subscribe((iFRS16LeaseContracts: IIFRS16LeaseContract[]) => (this.iFRS16LeaseContractsSharedCollection = iFRS16LeaseContracts));

    this.rouModelMetadataService
      .query()
      .pipe(map((res: HttpResponse<IRouModelMetadata[]>) => res.body ?? []))
      .pipe(
        map((rouModelMetadata: IRouModelMetadata[]) =>
          this.rouModelMetadataService.addRouModelMetadataToCollectionIfMissing(rouModelMetadata, this.editForm.get('rouMetadata')!.value)
        )
      )
      .subscribe((rouModelMetadata: IRouModelMetadata[]) => (this.rouModelMetadataSharedCollection = rouModelMetadata));
  }

  protected createFromForm(): IRouDepreciationEntry {
    return {
      ...new RouDepreciationEntry(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      depreciationAmount: this.editForm.get(['depreciationAmount'])!.value,
      outstandingAmount: this.editForm.get(['outstandingAmount'])!.value,
      rouAssetIdentifier: this.editForm.get(['rouAssetIdentifier'])!.value,
      rouDepreciationIdentifier: this.editForm.get(['rouDepreciationIdentifier'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      invalidated: this.editForm.get(['invalidated'])!.value,
      debitAccount: this.editForm.get(['debitAccount'])!.value,
      creditAccount: this.editForm.get(['creditAccount'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      rouMetadata: this.editForm.get(['rouMetadata'])!.value,
    };
  }
}
