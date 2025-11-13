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
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPrepaymentAccount, PrepaymentAccount } from '../prepayment-account.model';
import { PrepaymentAccountService } from '../service/prepayment-account.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { IPrepaymentMapping } from '../../prepayment-mapping/prepayment-mapping.model';
import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { TransactionAccountService } from '../../../erp-accounts/transaction-account/service/transaction-account.service';
import { PrepaymentMappingService } from '../../prepayment-mapping/service/prepayment-mapping.service';
import { SettlementCurrencySuggestionService } from '../../../erp-common/suggestion/settlement-currency-suggestion.service';
import { SearchWithPagination } from '../../../../core/request/request.model';
import { uuidv7 as uuidv4 } from 'uuidv7';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingPrepaymentAccountStatus,
  creatingPrepaymentAccountStatus,
  editingPrepaymentAccountStatus,
  prepaymentAccountUpdateSelectedInstance
} from '../../../store/selectors/prepayment-account-workflows-status.selector';

@Component({
  selector: 'jhi-prepayment-account-update',
  templateUrl: './prepayment-account-update.component.html',
})
export class PrepaymentAccountUpdateComponent implements OnInit {
  isSaving = false;

  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  settlementsSharedCollection: ISettlement[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  dealersSharedCollection: IDealer[] = [];
  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  prepaymentMappingsSharedCollection: IPrepaymentMapping[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new PrepaymentAccount()}

  editForm = this.fb.group({
    id: [],
    catalogueNumber: [null, [Validators.required]],
    recognitionDate: [null, [Validators.required]],
    particulars: [null, [Validators.required]],
    notes: [],
    prepaymentAmount: [],
    prepaymentGuid: [],
    settlementCurrency: [],
    prepaymentTransaction: [null, [Validators.required]],
    serviceOutlet: [],
    dealer: [],
    debitAccount: [],
    transferAccount: [],
    placeholders: [],
    generalParameters: [],
    prepaymentParameters: [],
    businessDocuments: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected prepaymentAccountService: PrepaymentAccountService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected settlementService: SettlementService,
    protected serviceOutletService: ServiceOutletService,
    protected dealerService: DealerService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected settlementCurrencySuggestionService: SettlementCurrencySuggestionService,
    protected prepaymentMappingService: PrepaymentMappingService,
    protected businessDocumentService: BusinessDocumentService,
    protected activatedRoute: ActivatedRoute,
    protected store: Store<State>,
    protected router: Router,
    protected fb: FormBuilder
  ) {
    this.store.pipe(select(copyingPrepaymentAccountStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingPrepaymentAccountStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingPrepaymentAccountStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(prepaymentAccountUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);

      this.editForm.patchValue({
        prepaymentGuid: uuidv4(),
      })
    }

    if (this.weAreCopying) {
      this.copyForm(this.selectedItem)

      this.editForm.patchValue({
        prepaymentGuid: uuidv4(),
      })

      // Update catalogue number
      this.prepaymentAccountService.getNextCatalogueNumber().subscribe(nextValue => {
        if (nextValue.body) {
          this.editForm.patchValue({
            catalogueNumber: nextValue.body,
          })
        }
      });
    }

    if (this.weAreCreating) {

      this.editForm.patchValue({
        prepaymentGuid: uuidv4(),
      })

      this.prepaymentAccountService.getNextCatalogueNumber().subscribe(nextValue => {
        if (nextValue.body) {
          this.editForm.patchValue({
            catalogueNumber: nextValue.body,
          })
        }
      });

      this.updatePreferredCurrency();

      this.loadRelationshipsOptions();

      this.updateDetailsGivenTransaction();
    }
  }

  updateDetailsGivenTransaction(): void {
    this.editForm.get(['prepaymentTransaction'])?.valueChanges.subscribe((transaction) => {
      this.editForm.patchValue({
        // prevent GUID overwrite
        prepaymentGuid: this.editForm.get(['prepaymentGuid'])?.value,
        dealer: transaction.biller,
        recognitionDate: transaction.paymentDate,
        settlementCurrency: transaction.settlementCurrency,
        particulars: transaction.description,
        prepaymentAmount: transaction.paymentAmount,
        placeholders: transaction.placeholders,
      })
    });
  }

  updateCurrencies(update: ISettlementCurrency): void {
    this.editForm.patchValue({
      settlementCurrency: update
    });
  }

  updatePreferredCurrency(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredSettlementIso4217CurrencyCode")
      .subscribe(mapped => {
          this.settlementCurrencyService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
            .subscribe(({ body: currencies }) => {
              if (currencies) {
                this.editForm.get(['settlementCurrency'])?.setValue(currencies[0]);
              }
            });
        }
      );
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateBusinessDocument(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      businessDocuments: [...update],
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateDealer(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      dealer: dealerUpdate,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateTransactionAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({
      debitAccount: update,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateTransferAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({
      transferAccount: update,
    });
  }

  updateSettlement(update: ISettlement): void {
    this.editForm.patchValue({
      prepaymentTransaction: update
    });
  }

  updateServiceOutlet(update: IServiceOutlet): void {
    this.editForm.patchValue({
      serviceOutlet: update
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  updateGeneralParameters(update: IUniversallyUniqueMapping[]): void {
    this.editForm.patchValue({
      generalParameters: [...update]
    });
  }

  updatePrepaymentParameters(update: IPrepaymentMapping[]): void {
    this.editForm.patchValue({
      prepaymentParameters: [...update]
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
    this.subscribeToSaveResponse(this.prepaymentAccountService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.prepaymentAccountService.partialUpdate(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.prepaymentAccountService.create(this.copyFromForm()));
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackSettlementById(index: number, item: ISettlement): number {
    return item.id!;
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackPrepaymentMappingById(index: number, item: IPrepaymentMapping): number {
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

  getSelectedPrepaymentMapping(option: IPrepaymentMapping, selectedVals?: IPrepaymentMapping[]): IPrepaymentMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentAccount>>): void {
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

  protected updateForm(prepaymentAccount: IPrepaymentAccount): void {
    this.editForm.patchValue({
      id: prepaymentAccount.id,
      catalogueNumber: prepaymentAccount.catalogueNumber,
      recognitionDate: prepaymentAccount.recognitionDate,
      particulars: prepaymentAccount.particulars,
      notes: prepaymentAccount.notes,
      prepaymentAmount: prepaymentAccount.prepaymentAmount,
      prepaymentGuid: prepaymentAccount.prepaymentGuid,
      settlementCurrency: prepaymentAccount.settlementCurrency,
      prepaymentTransaction: prepaymentAccount.prepaymentTransaction,
      serviceOutlet: prepaymentAccount.serviceOutlet,
      dealer: prepaymentAccount.dealer,
      debitAccount: prepaymentAccount.debitAccount,
      transferAccount: prepaymentAccount.transferAccount,
      placeholders: prepaymentAccount.placeholders,
      generalParameters: prepaymentAccount.generalParameters,
      prepaymentParameters: prepaymentAccount.prepaymentParameters,
      businessDocuments: prepaymentAccount.businessDocuments,
    });

    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      prepaymentAccount.settlementCurrency
    );
    this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
      this.settlementsSharedCollection,
      prepaymentAccount.prepaymentTransaction
    );
    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      prepaymentAccount.serviceOutlet
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      prepaymentAccount.dealer
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      prepaymentAccount.debitAccount,
      prepaymentAccount.transferAccount
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(prepaymentAccount.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(prepaymentAccount.generalParameters ?? [])
    );
    this.prepaymentMappingsSharedCollection = this.prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing(
      this.prepaymentMappingsSharedCollection,
      ...(prepaymentAccount.prepaymentParameters ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(prepaymentAccount.businessDocuments ?? [])
    );
  }

  protected copyForm(prepaymentAccount: IPrepaymentAccount): void {
    this.editForm.patchValue({
      id: prepaymentAccount.id,
      catalogueNumber: prepaymentAccount.catalogueNumber,
      recognitionDate: prepaymentAccount.recognitionDate,
      particulars: prepaymentAccount.particulars,
      notes: prepaymentAccount.notes,
      prepaymentAmount: prepaymentAccount.prepaymentAmount,
      prepaymentGuid: prepaymentAccount.prepaymentGuid,
      settlementCurrency: prepaymentAccount.settlementCurrency,
      prepaymentTransaction: prepaymentAccount.prepaymentTransaction,
      serviceOutlet: prepaymentAccount.serviceOutlet,
      dealer: prepaymentAccount.dealer,
      debitAccount: prepaymentAccount.debitAccount,
      transferAccount: prepaymentAccount.transferAccount,
      placeholders: prepaymentAccount.placeholders,
      generalParameters: prepaymentAccount.generalParameters,
      prepaymentParameters: prepaymentAccount.prepaymentParameters,
      businessDocuments: prepaymentAccount.businessDocuments,
    });

    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      prepaymentAccount.settlementCurrency
    );
    this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
      this.settlementsSharedCollection,
      prepaymentAccount.prepaymentTransaction
    );
    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      prepaymentAccount.serviceOutlet
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      prepaymentAccount.dealer
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      prepaymentAccount.debitAccount,
      prepaymentAccount.transferAccount
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(prepaymentAccount.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(prepaymentAccount.generalParameters ?? [])
    );
    this.prepaymentMappingsSharedCollection = this.prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing(
      this.prepaymentMappingsSharedCollection,
      ...(prepaymentAccount.prepaymentParameters ?? [])
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(prepaymentAccount.businessDocuments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.settlementCurrencyService
      .query()
      .pipe(map((res: HttpResponse<ISettlementCurrency[]>) => res.body ?? []))
      .pipe(
        map((settlementCurrencies: ISettlementCurrency[]) =>
          this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
            settlementCurrencies,
            this.editForm.get('settlementCurrency')!.value
          )
        )
      )
      .subscribe((settlementCurrencies: ISettlementCurrency[]) => (this.settlementCurrenciesSharedCollection = settlementCurrencies));

    this.settlementService
      .query()
      .pipe(map((res: HttpResponse<ISettlement[]>) => res.body ?? []))
      .pipe(
        map((settlements: ISettlement[]) =>
          this.settlementService.addSettlementToCollectionIfMissing(settlements, this.editForm.get('prepaymentTransaction')!.value)
        )
      )
      .subscribe((settlements: ISettlement[]) => (this.settlementsSharedCollection = settlements));

    this.serviceOutletService
      .query()
      .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
      .pipe(
        map((serviceOutlets: IServiceOutlet[]) =>
          this.serviceOutletService.addServiceOutletToCollectionIfMissing(serviceOutlets, this.editForm.get('serviceOutlet')!.value)
        )
      )
      .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.transactionAccountService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('debitAccount')!.value,
            this.editForm.get('transferAccount')!.value
          )
        )
      )
      .subscribe((transactionAccounts: ITransactionAccount[]) => (this.transactionAccountsSharedCollection = transactionAccounts));

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
            ...(this.editForm.get('generalParameters')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.prepaymentMappingService
      .query()
      .pipe(map((res: HttpResponse<IPrepaymentMapping[]>) => res.body ?? []))
      .pipe(
        map((prepaymentMappings: IPrepaymentMapping[]) =>
          this.prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing(
            prepaymentMappings,
            ...(this.editForm.get('prepaymentParameters')!.value ?? [])
          )
        )
      )
      .subscribe((prepaymentMappings: IPrepaymentMapping[]) => (this.prepaymentMappingsSharedCollection = prepaymentMappings));

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            ...(this.editForm.get('businessDocuments')!.value ?? [])
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));
  }

  protected createFromForm(): IPrepaymentAccount {
    return {
      ...new PrepaymentAccount(),
      id: this.editForm.get(['id'])!.value,
      catalogueNumber: this.editForm.get(['catalogueNumber'])!.value,
      recognitionDate: this.editForm.get(['recognitionDate'])!.value,
      particulars: this.editForm.get(['particulars'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      prepaymentAmount: this.editForm.get(['prepaymentAmount'])!.value,
      prepaymentGuid: this.editForm.get(['prepaymentGuid'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      prepaymentTransaction: this.editForm.get(['prepaymentTransaction'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      debitAccount: this.editForm.get(['debitAccount'])!.value,
      transferAccount: this.editForm.get(['transferAccount'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      generalParameters: this.editForm.get(['generalParameters'])!.value,
      prepaymentParameters: this.editForm.get(['prepaymentParameters'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
    };
  }

  protected copyFromForm(): IPrepaymentAccount {
    return {
      ...new PrepaymentAccount(),
      // id: this.editForm.get(['id'])!.value,
      catalogueNumber: this.editForm.get(['catalogueNumber'])!.value,
      recognitionDate: this.editForm.get(['recognitionDate'])!.value,
      particulars: this.editForm.get(['particulars'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      prepaymentAmount: this.editForm.get(['prepaymentAmount'])!.value,
      prepaymentGuid: this.editForm.get(['prepaymentGuid'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      prepaymentTransaction: this.editForm.get(['prepaymentTransaction'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      debitAccount: this.editForm.get(['debitAccount'])!.value,
      transferAccount: this.editForm.get(['transferAccount'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      generalParameters: this.editForm.get(['generalParameters'])!.value,
      prepaymentParameters: this.editForm.get(['prepaymentParameters'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
    };
  }
}
