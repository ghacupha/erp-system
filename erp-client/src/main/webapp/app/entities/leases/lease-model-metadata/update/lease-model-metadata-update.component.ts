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
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeaseModelMetadata, LeaseModelMetadata } from '../lease-model-metadata.model';
import { LeaseModelMetadataService } from '../service/lease-model-metadata.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { ILeaseContract } from 'app/entities/leases/lease-contract/lease-contract.model';
import { LeaseContractService } from 'app/entities/leases/lease-contract/service/lease-contract.service';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/entities/system/settlement-currency/service/settlement-currency.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';
import { ISecurityClearance } from 'app/entities/people/security-clearance/security-clearance.model';
import { SecurityClearanceService } from 'app/entities/people/security-clearance/service/security-clearance.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';

@Component({
  selector: 'jhi-lease-model-metadata-update',
  templateUrl: './lease-model-metadata-update.component.html',
})
export class LeaseModelMetadataUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  leaseContractsSharedCollection: ILeaseContract[] = [];
  predecessorsCollection: ILeaseModelMetadata[] = [];
  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  securityClearancesSharedCollection: ISecurityClearance[] = [];
  transactionAccountsSharedCollection: ITransactionAccount[] = [];

  editForm = this.fb.group({
    id: [],
    modelTitle: [null, [Validators.required]],
    modelVersion: [null, [Validators.required]],
    description: [],
    modelNotes: [],
    modelNotesContentType: [],
    annualDiscountingRate: [null, [Validators.required]],
    commencementDate: [null, [Validators.required]],
    terminalDate: [null, [Validators.required]],
    totalReportingPeriods: [],
    reportingPeriodsPerYear: [],
    settlementPeriodsPerYear: [],
    initialLiabilityAmount: [],
    initialROUAmount: [],
    totalDepreciationPeriods: [],
    placeholders: [],
    leaseMappings: [],
    leaseContract: [null, Validators.required],
    predecessor: [],
    liabilityCurrency: [null, Validators.required],
    rouAssetCurrency: [null, Validators.required],
    modelAttachments: [],
    securityClearance: [],
    leaseLiabilityAccount: [],
    interestPayableAccount: [],
    interestExpenseAccount: [],
    rouAssetAccount: [],
    rouDepreciationAccount: [],
    accruedDepreciationAccount: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected leaseModelMetadataService: LeaseModelMetadataService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected leaseContractService: LeaseContractService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected businessDocumentService: BusinessDocumentService,
    protected securityClearanceService: SecurityClearanceService,
    protected transactionAccountService: TransactionAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseModelMetadata }) => {
      this.updateForm(leaseModelMetadata);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseModelMetadata = this.createFromForm();
    if (leaseModelMetadata.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseModelMetadataService.update(leaseModelMetadata));
    } else {
      this.subscribeToSaveResponse(this.leaseModelMetadataService.create(leaseModelMetadata));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackLeaseContractById(index: number, item: ILeaseContract): number {
    return item.id!;
  }

  trackLeaseModelMetadataById(index: number, item: ILeaseModelMetadata): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackBusinessDocumentById(index: number, item: IBusinessDocument): number {
    return item.id!;
  }

  trackSecurityClearanceById(index: number, item: ISecurityClearance): number {
    return item.id!;
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
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

  getSelectedUniversallyUniqueMapping(
    option: IUniversallyUniqueMapping,
    selectedVals?: IUniversallyUniqueMapping[]
  ): IUniversallyUniqueMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseModelMetadata>>): void {
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

  protected updateForm(leaseModelMetadata: ILeaseModelMetadata): void {
    this.editForm.patchValue({
      id: leaseModelMetadata.id,
      modelTitle: leaseModelMetadata.modelTitle,
      modelVersion: leaseModelMetadata.modelVersion,
      description: leaseModelMetadata.description,
      modelNotes: leaseModelMetadata.modelNotes,
      modelNotesContentType: leaseModelMetadata.modelNotesContentType,
      annualDiscountingRate: leaseModelMetadata.annualDiscountingRate,
      commencementDate: leaseModelMetadata.commencementDate,
      terminalDate: leaseModelMetadata.terminalDate,
      totalReportingPeriods: leaseModelMetadata.totalReportingPeriods,
      reportingPeriodsPerYear: leaseModelMetadata.reportingPeriodsPerYear,
      settlementPeriodsPerYear: leaseModelMetadata.settlementPeriodsPerYear,
      initialLiabilityAmount: leaseModelMetadata.initialLiabilityAmount,
      initialROUAmount: leaseModelMetadata.initialROUAmount,
      totalDepreciationPeriods: leaseModelMetadata.totalDepreciationPeriods,
      placeholders: leaseModelMetadata.placeholders,
      leaseMappings: leaseModelMetadata.leaseMappings,
      leaseContract: leaseModelMetadata.leaseContract,
      predecessor: leaseModelMetadata.predecessor,
      liabilityCurrency: leaseModelMetadata.liabilityCurrency,
      rouAssetCurrency: leaseModelMetadata.rouAssetCurrency,
      modelAttachments: leaseModelMetadata.modelAttachments,
      securityClearance: leaseModelMetadata.securityClearance,
      leaseLiabilityAccount: leaseModelMetadata.leaseLiabilityAccount,
      interestPayableAccount: leaseModelMetadata.interestPayableAccount,
      interestExpenseAccount: leaseModelMetadata.interestExpenseAccount,
      rouAssetAccount: leaseModelMetadata.rouAssetAccount,
      rouDepreciationAccount: leaseModelMetadata.rouDepreciationAccount,
      accruedDepreciationAccount: leaseModelMetadata.accruedDepreciationAccount,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(leaseModelMetadata.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(leaseModelMetadata.leaseMappings ?? [])
    );
    this.leaseContractsSharedCollection = this.leaseContractService.addLeaseContractToCollectionIfMissing(
      this.leaseContractsSharedCollection,
      leaseModelMetadata.leaseContract
    );
    this.predecessorsCollection = this.leaseModelMetadataService.addLeaseModelMetadataToCollectionIfMissing(
      this.predecessorsCollection,
      leaseModelMetadata.predecessor
    );
    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      leaseModelMetadata.liabilityCurrency,
      leaseModelMetadata.rouAssetCurrency
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      leaseModelMetadata.modelAttachments
    );
    this.securityClearancesSharedCollection = this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
      this.securityClearancesSharedCollection,
      leaseModelMetadata.securityClearance
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      leaseModelMetadata.leaseLiabilityAccount,
      leaseModelMetadata.interestPayableAccount,
      leaseModelMetadata.interestExpenseAccount,
      leaseModelMetadata.rouAssetAccount,
      leaseModelMetadata.rouDepreciationAccount,
      leaseModelMetadata.accruedDepreciationAccount
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('leaseMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.leaseContractService
      .query()
      .pipe(map((res: HttpResponse<ILeaseContract[]>) => res.body ?? []))
      .pipe(
        map((leaseContracts: ILeaseContract[]) =>
          this.leaseContractService.addLeaseContractToCollectionIfMissing(leaseContracts, this.editForm.get('leaseContract')!.value)
        )
      )
      .subscribe((leaseContracts: ILeaseContract[]) => (this.leaseContractsSharedCollection = leaseContracts));

    this.leaseModelMetadataService
      .query({ 'successorId.specified': 'false' })
      .pipe(map((res: HttpResponse<ILeaseModelMetadata[]>) => res.body ?? []))
      .pipe(
        map((leaseModelMetadata: ILeaseModelMetadata[]) =>
          this.leaseModelMetadataService.addLeaseModelMetadataToCollectionIfMissing(
            leaseModelMetadata,
            this.editForm.get('predecessor')!.value
          )
        )
      )
      .subscribe((leaseModelMetadata: ILeaseModelMetadata[]) => (this.predecessorsCollection = leaseModelMetadata));

    this.settlementCurrencyService
      .query()
      .pipe(map((res: HttpResponse<ISettlementCurrency[]>) => res.body ?? []))
      .pipe(
        map((settlementCurrencies: ISettlementCurrency[]) =>
          this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
            settlementCurrencies,
            this.editForm.get('liabilityCurrency')!.value,
            this.editForm.get('rouAssetCurrency')!.value
          )
        )
      )
      .subscribe((settlementCurrencies: ISettlementCurrency[]) => (this.settlementCurrenciesSharedCollection = settlementCurrencies));

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            this.editForm.get('modelAttachments')!.value
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));

    this.securityClearanceService
      .query()
      .pipe(map((res: HttpResponse<ISecurityClearance[]>) => res.body ?? []))
      .pipe(
        map((securityClearances: ISecurityClearance[]) =>
          this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
            securityClearances,
            this.editForm.get('securityClearance')!.value
          )
        )
      )
      .subscribe((securityClearances: ISecurityClearance[]) => (this.securityClearancesSharedCollection = securityClearances));

    this.transactionAccountService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('leaseLiabilityAccount')!.value,
            this.editForm.get('interestPayableAccount')!.value,
            this.editForm.get('interestExpenseAccount')!.value,
            this.editForm.get('rouAssetAccount')!.value,
            this.editForm.get('rouDepreciationAccount')!.value,
            this.editForm.get('accruedDepreciationAccount')!.value
          )
        )
      )
      .subscribe((transactionAccounts: ITransactionAccount[]) => (this.transactionAccountsSharedCollection = transactionAccounts));
  }

  protected createFromForm(): ILeaseModelMetadata {
    return {
      ...new LeaseModelMetadata(),
      id: this.editForm.get(['id'])!.value,
      modelTitle: this.editForm.get(['modelTitle'])!.value,
      modelVersion: this.editForm.get(['modelVersion'])!.value,
      description: this.editForm.get(['description'])!.value,
      modelNotesContentType: this.editForm.get(['modelNotesContentType'])!.value,
      modelNotes: this.editForm.get(['modelNotes'])!.value,
      annualDiscountingRate: this.editForm.get(['annualDiscountingRate'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      terminalDate: this.editForm.get(['terminalDate'])!.value,
      totalReportingPeriods: this.editForm.get(['totalReportingPeriods'])!.value,
      reportingPeriodsPerYear: this.editForm.get(['reportingPeriodsPerYear'])!.value,
      settlementPeriodsPerYear: this.editForm.get(['settlementPeriodsPerYear'])!.value,
      initialLiabilityAmount: this.editForm.get(['initialLiabilityAmount'])!.value,
      initialROUAmount: this.editForm.get(['initialROUAmount'])!.value,
      totalDepreciationPeriods: this.editForm.get(['totalDepreciationPeriods'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      leaseMappings: this.editForm.get(['leaseMappings'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      predecessor: this.editForm.get(['predecessor'])!.value,
      liabilityCurrency: this.editForm.get(['liabilityCurrency'])!.value,
      rouAssetCurrency: this.editForm.get(['rouAssetCurrency'])!.value,
      modelAttachments: this.editForm.get(['modelAttachments'])!.value,
      securityClearance: this.editForm.get(['securityClearance'])!.value,
      leaseLiabilityAccount: this.editForm.get(['leaseLiabilityAccount'])!.value,
      interestPayableAccount: this.editForm.get(['interestPayableAccount'])!.value,
      interestExpenseAccount: this.editForm.get(['interestExpenseAccount'])!.value,
      rouAssetAccount: this.editForm.get(['rouAssetAccount'])!.value,
      rouDepreciationAccount: this.editForm.get(['rouDepreciationAccount'])!.value,
      accruedDepreciationAccount: this.editForm.get(['accruedDepreciationAccount'])!.value,
    };
  }
}
