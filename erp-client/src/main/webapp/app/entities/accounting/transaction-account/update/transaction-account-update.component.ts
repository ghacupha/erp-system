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

import { ITransactionAccount, TransactionAccount } from '../transaction-account.model';
import { TransactionAccountService } from '../service/transaction-account.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITransactionAccountLedger } from 'app/entities/accounting/transaction-account-ledger/transaction-account-ledger.model';
import { TransactionAccountLedgerService } from 'app/entities/accounting/transaction-account-ledger/service/transaction-account-ledger.service';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { TransactionAccountCategoryService } from 'app/entities/accounting/transaction-account-category/service/transaction-account-category.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/entities/system/settlement-currency/service/settlement-currency.service';
import { IReportingEntity } from 'app/entities/admin/reporting-entity/reporting-entity.model';
import { ReportingEntityService } from 'app/entities/admin/reporting-entity/service/reporting-entity.service';
import { AccountTypes } from 'app/entities/enumerations/account-types.model';
import { AccountSubTypes } from 'app/entities/enumerations/account-sub-types.model';

@Component({
  selector: 'jhi-transaction-account-update',
  templateUrl: './transaction-account-update.component.html',
})
export class TransactionAccountUpdateComponent implements OnInit {
  isSaving = false;
  accountTypesValues = Object.keys(AccountTypes);
  accountSubTypesValues = Object.keys(AccountSubTypes);

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
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccount }) => {
      this.updateForm(transactionAccount);

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
    const transactionAccount = this.createFromForm();
    if (transactionAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionAccountService.update(transactionAccount));
    } else {
      this.subscribeToSaveResponse(this.transactionAccountService.create(transactionAccount));
    }
  }

  trackTransactionAccountLedgerById(index: number, item: ITransactionAccountLedger): number {
    return item.id!;
  }

  trackTransactionAccountCategoryById(index: number, item: ITransactionAccountCategory): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackReportingEntityById(index: number, item: IReportingEntity): number {
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
}
