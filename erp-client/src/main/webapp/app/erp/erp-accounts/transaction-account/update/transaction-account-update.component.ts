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

import { ITransactionAccount, TransactionAccount } from '../transaction-account.model';
import { TransactionAccountService } from '../service/transaction-account.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingTransactionAccountStatus,
  creatingTransactionAccountStatus,
  editingTransactionAccountStatus,
  transactionAccountUpdateSelectedInstance
} from '../../../store/selectors/transaction-account-update-status.selectors';
import { AccountTypes } from '../../../erp-common/enumerations/account-types.model';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { ReportingEntityService } from '../../../admin/reporting-entity/service/reporting-entity.service';
import { TransactionAccountLedgerService } from '../../transaction-account-ledger/service/transaction-account-ledger.service';
import { ITransactionAccountCategory } from '../../transaction-account-category/transaction-account-category.model';
import { ITransactionAccountLedger } from '../../transaction-account-ledger/transaction-account-ledger.model';
import { AccountSubTypes } from '../../../erp-common/enumerations/account-sub-types.model';
import { IReportingEntity } from '../../../admin/reporting-entity/reporting-entity.model';
import { TransactionAccountCategoryService } from '../../transaction-account-category/service/transaction-account-category.service';
import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';

@Component({
  selector: 'jhi-transaction-account-update',
  templateUrl: './transaction-account-update.component.html',
})
export class TransactionAccountUpdateComponent implements OnInit {
  isSaving = false;
  accountTypesValues = Object.keys(AccountTypes);
  accountSubTypesValues = Object.keys(AccountSubTypes);

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new TransactionAccount()}

  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  transactionAccountLedgersSharedCollection: ITransactionAccountLedger[] = [];
  transactionAccountCategoriesSharedCollection: ITransactionAccountCategory[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  reportingEntitiesSharedCollection: IReportingEntity[] = [];

  editForm = this.fb.group({
    id: [],
    accountNumber: [null, [Validators.required]],
    accountName: [null, [Validators.required]],
    notes: [],
    notesContentType: [],
    accountType: [null, [Validators.required]],
    accountSubType: [null, [Validators.required]],
    dummyAccount: [],
    accountLedger: [null, Validators.required],
    accountCategory: [null, Validators.required],
    placeholders: [],
    serviceOutlet: [null, Validators.required],
    settlementCurrency: [null, Validators.required],
    institution: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected transactionAccountService: TransactionAccountService,
    protected transactionAccountLedgerService: TransactionAccountLedgerService,
    protected transactionAccountCategoryService: TransactionAccountCategoryService,
    protected placeholderService: PlaceholderService,
    protected serviceOutletService: ServiceOutletService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected reportingEntityService: ReportingEntityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingTransactionAccountStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingTransactionAccountStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingTransactionAccountStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(transactionAccountUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {
    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
      this.loadRelationshipsOptions();
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);
      this.loadRelationshipsOptions();
    }

    if (this.weAreCreating) {
      this.loadRelationshipsOptions();
    }
  }

  trackPlaceholdersByFn(item: IPlaceholder): number {
    return item.id!;
  }

  trackTransactionAccountsByFn(item: ITransactionAccount): number {
    return item.id!;
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
    this.subscribeToSaveResponse(this.transactionAccountService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.transactionAccountService.partialUpdate(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.transactionAccountService.create(this.copyFromForm()));
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackTransactionAccountLedgerById(index: number, item: ITransactionAccountLedger): number {
    return item.id!;
  }

  trackReportingEntityById(index: number, item: IReportingEntity): number {
    return item.id!;
  }

  trackTransactionAccountCategoryById(index: number, item: ITransactionAccountCategory): number {
    return item.id!;
  }

  updatePlaceholders(value: IPlaceholder[]): void {
      this.editForm.patchValue({
        placeholders: [...value]
      });
  }

  updateAccountLedger($event: ITransactionAccountLedger): void {
      this.editForm.patchValue({
        accountLedger: $event
      });
  }

  updateServiceOutlet($event: IServiceOutlet): void {
      this.editForm.patchValue({
        serviceOutlet: $event
      });
  }

  updateAccountCategory($event: ITransactionAccountCategory): void {
      this.editForm.patchValue({
        accountCategory: $event
      });
  }

  updateSettlementCurrency($event: ISettlementCurrency): void {
      this.editForm.patchValue({
        settlementCurrency: $event
      });
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionAccount>>): void {
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

  protected updateForm(transactionAccount: ITransactionAccount): void {
    this.editForm.patchValue({
      id: transactionAccount.id,
      accountNumber: transactionAccount.accountNumber,
      accountName: transactionAccount.accountName,
      notes: transactionAccount.notes,
      notesContentType: transactionAccount.notesContentType,
      accountType: transactionAccount.accountType,
      accountSubType: transactionAccount.accountSubType,
      dummyAccount: transactionAccount.dummyAccount,
      accountLedger: transactionAccount.accountLedger,
      accountCategory: transactionAccount.accountCategory,
      placeholders: transactionAccount.placeholders,
      serviceOutlet: transactionAccount.serviceOutlet,
      settlementCurrency: transactionAccount.settlementCurrency,
      institution: transactionAccount.institution,
    });

    this.transactionAccountLedgersSharedCollection = this.transactionAccountLedgerService.addTransactionAccountLedgerToCollectionIfMissing(
      this.transactionAccountLedgersSharedCollection,
      transactionAccount.accountLedger
    );
    this.transactionAccountCategoriesSharedCollection =
      this.transactionAccountCategoryService.addTransactionAccountCategoryToCollectionIfMissing(
        this.transactionAccountCategoriesSharedCollection,
        transactionAccount.accountCategory
      );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(transactionAccount.placeholders ?? [])
    );
    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      transactionAccount.serviceOutlet
    );
    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      transactionAccount.settlementCurrency
    );
    this.reportingEntitiesSharedCollection = this.reportingEntityService.addReportingEntityToCollectionIfMissing(
      this.reportingEntitiesSharedCollection,
      transactionAccount.institution
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionAccountLedgerService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccountLedger[]>) => res.body ?? []))
      .pipe(
        map((transactionAccountLedgers: ITransactionAccountLedger[]) =>
          this.transactionAccountLedgerService.addTransactionAccountLedgerToCollectionIfMissing(
            transactionAccountLedgers,
            this.editForm.get('accountLedger')!.value
          )
        )
      )
      .subscribe(
        (transactionAccountLedgers: ITransactionAccountLedger[]) =>
          (this.transactionAccountLedgersSharedCollection = transactionAccountLedgers)
      );

    this.transactionAccountCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccountCategory[]>) => res.body ?? []))
      .pipe(
        map((transactionAccountCategories: ITransactionAccountCategory[]) =>
          this.transactionAccountCategoryService.addTransactionAccountCategoryToCollectionIfMissing(
            transactionAccountCategories,
            this.editForm.get('accountCategory')!.value
          )
        )
      )
      .subscribe(
        (transactionAccountCategories: ITransactionAccountCategory[]) =>
          (this.transactionAccountCategoriesSharedCollection = transactionAccountCategories)
      );

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.serviceOutletService
      .query()
      .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
      .pipe(
        map((serviceOutlets: IServiceOutlet[]) =>
          this.serviceOutletService.addServiceOutletToCollectionIfMissing(serviceOutlets, this.editForm.get('serviceOutlet')!.value)
        )
      )
      .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));

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

    this.reportingEntityService
      .query()
      .pipe(map((res: HttpResponse<IReportingEntity[]>) => res.body ?? []))
      .pipe(
        map((reportingEntities: IReportingEntity[]) =>
          this.reportingEntityService.addReportingEntityToCollectionIfMissing(reportingEntities, this.editForm.get('institution')!.value)
        )
      )
      .subscribe((reportingEntities: IReportingEntity[]) => (this.reportingEntitiesSharedCollection = reportingEntities));
  }

  protected createFromForm(): ITransactionAccount {
    return {
      ...new TransactionAccount(),
      id: this.editForm.get(['id'])!.value,
      accountNumber: this.editForm.get(['accountNumber'])!.value,
      accountName: this.editForm.get(['accountName'])!.value,
      notesContentType: this.editForm.get(['notesContentType'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      accountType: this.editForm.get(['accountType'])!.value,
      accountSubType: this.editForm.get(['accountSubType'])!.value,
      dummyAccount: this.editForm.get(['dummyAccount'])!.value,
      accountLedger: this.editForm.get(['accountLedger'])!.value,
      accountCategory: this.editForm.get(['accountCategory'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      institution: this.editForm.get(['institution'])!.value,
    };
  }

  protected copyFromForm(): ITransactionAccount {
    return {
      ...new TransactionAccount(),
      accountNumber: this.editForm.get(['accountNumber'])!.value,
      accountName: this.editForm.get(['accountName'])!.value,
      notesContentType: this.editForm.get(['notesContentType'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      accountType: this.editForm.get(['accountType'])!.value,
      accountSubType: this.editForm.get(['accountSubType'])!.value,
      dummyAccount: this.editForm.get(['dummyAccount'])!.value,
      accountLedger: this.editForm.get(['accountLedger'])!.value,
      accountCategory: this.editForm.get(['accountCategory'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      institution: this.editForm.get(['institution'])!.value,
    };
  }
}
