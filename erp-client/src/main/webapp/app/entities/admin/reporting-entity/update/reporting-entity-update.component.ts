import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReportingEntity, ReportingEntity } from '../reporting-entity.model';
import { ReportingEntityService } from '../service/reporting-entity.service';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/entities/system/settlement-currency/service/settlement-currency.service';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { TransactionAccountService } from 'app/entities/accounting/transaction-account/service/transaction-account.service';

@Component({
  selector: 'jhi-reporting-entity-update',
  templateUrl: './reporting-entity-update.component.html',
})
export class ReportingEntityUpdateComponent implements OnInit {
  isSaving = false;

  reportingCurrenciesCollection: ISettlementCurrency[] = [];
  retainedEarningsAccountsCollection: ITransactionAccount[] = [];

  editForm = this.fb.group({
    id: [],
    entityName: [null, [Validators.required]],
    reportingCurrency: [],
    retainedEarningsAccount: [],
  });

  constructor(
    protected reportingEntityService: ReportingEntityService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected transactionAccountService: TransactionAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingEntity }) => {
      this.updateForm(reportingEntity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportingEntity = this.createFromForm();
    if (reportingEntity.id !== undefined) {
      this.subscribeToSaveResponse(this.reportingEntityService.update(reportingEntity));
    } else {
      this.subscribeToSaveResponse(this.reportingEntityService.create(reportingEntity));
    }
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportingEntity>>): void {
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

  protected updateForm(reportingEntity: IReportingEntity): void {
    this.editForm.patchValue({
      id: reportingEntity.id,
      entityName: reportingEntity.entityName,
      reportingCurrency: reportingEntity.reportingCurrency,
      retainedEarningsAccount: reportingEntity.retainedEarningsAccount,
    });

    this.reportingCurrenciesCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.reportingCurrenciesCollection,
      reportingEntity.reportingCurrency
    );
    this.retainedEarningsAccountsCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.retainedEarningsAccountsCollection,
      reportingEntity.retainedEarningsAccount
    );
  }

  protected loadRelationshipsOptions(): void {
    this.settlementCurrencyService
      .query({ 'reportingEntityId.specified': 'false' })
      .pipe(map((res: HttpResponse<ISettlementCurrency[]>) => res.body ?? []))
      .pipe(
        map((settlementCurrencies: ISettlementCurrency[]) =>
          this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
            settlementCurrencies,
            this.editForm.get('reportingCurrency')!.value
          )
        )
      )
      .subscribe((settlementCurrencies: ISettlementCurrency[]) => (this.reportingCurrenciesCollection = settlementCurrencies));

    this.transactionAccountService
      .query({ 'reportingEntityId.specified': 'false' })
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('retainedEarningsAccount')!.value
          )
        )
      )
      .subscribe((transactionAccounts: ITransactionAccount[]) => (this.retainedEarningsAccountsCollection = transactionAccounts));
  }

  protected createFromForm(): IReportingEntity {
    return {
      ...new ReportingEntity(),
      id: this.editForm.get(['id'])!.value,
      entityName: this.editForm.get(['entityName'])!.value,
      reportingCurrency: this.editForm.get(['reportingCurrency'])!.value,
      retainedEarningsAccount: this.editForm.get(['retainedEarningsAccount'])!.value,
    };
  }
}
