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
import { forkJoin, Observable, of } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPrepaymentCompilationRequest, PrepaymentCompilationRequest } from '../prepayment-compilation-request.model';
import { PrepaymentCompilationRequestService } from '../service/prepayment-compilation-request.service';
import { IPlaceholder } from 'app/erp/erp-pages/placeholder/placeholder.model';
import { CompilationStatusTypes } from '../../../erp-common/enumerations/compilation-status-types.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { v4 as uuidv4 } from 'uuid';
import { PrepaymentMarshallingService } from '../../prepayment-marshalling/service/prepayment-marshalling.service';
import { IPrepaymentMarshalling } from '../../prepayment-marshalling/prepayment-marshalling.model';
import { PrepaymentAccountService } from '../../prepayment-account/service/prepayment-account.service';
import { IPrepaymentAccount } from '../../prepayment-account/prepayment-account.model';
import { TransactionAccountService } from '../../../erp-accounts/transaction-account/service/transaction-account.service';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';

@Component({
  selector: 'jhi-prepayment-compilation-request-update',
  templateUrl: './prepayment-compilation-request-update.component.html',
  styleUrls: ['./prepayment-compilation-request-update.component.scss'],
})
export class PrepaymentCompilationRequestUpdateComponent implements OnInit {
  isSaving = false;
  compilationStatusTypesValues = Object.keys(CompilationStatusTypes);

  placeholdersSharedCollection: IPlaceholder[] = [];
  pendingMarshallings: IPrepaymentMarshalling[] = [];

  editForm = this.fb.group({
    id: [],
    timeOfRequest: [],
    compilationStatus: [],
    itemsProcessed: [],
    compilationToken: [null, [Validators.required]],
    narration: [],
    createdBy: [],
    placeholders: [],
  });

  constructor(
    protected prepaymentCompilationRequestService: PrepaymentCompilationRequestService,
    protected placeholderService: PlaceholderService,
    protected prepaymentMarshallingService: PrepaymentMarshallingService,
    protected prepaymentAccountService: PrepaymentAccountService,
    protected transactionAccountService: TransactionAccountService,
    protected fiscalMonthService: FiscalMonthService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentCompilationRequest }) => {
      if (prepaymentCompilationRequest.id === undefined) {
        const today = dayjs();
        prepaymentCompilationRequest.timeOfRequest = today;
        prepaymentCompilationRequest.compilationStatus = CompilationStatusTypes.STARTED;
        prepaymentCompilationRequest.compilationToken = uuidv4();

      }

      this.updateForm(prepaymentCompilationRequest);

      this.loadRelationshipsOptions();
      this.loadPendingMarshallings();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prepaymentCompilationRequest = this.createFromForm();
    if (prepaymentCompilationRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.prepaymentCompilationRequestService.update(prepaymentCompilationRequest));
    } else {
      this.subscribeToSaveResponse(this.prepaymentCompilationRequestService.create(prepaymentCompilationRequest));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
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

  trackPrepaymentMarshallingById(index: number, item: IPrepaymentMarshalling): number {
    return item.id ?? index;
  }

  marshallingCreditAccount(marshalling: IPrepaymentMarshalling): string {
    return marshalling.prepaymentAccount?.debitAccount?.accountNumber ?? '';
  }

  marshallingDebitAccount(marshalling: IPrepaymentMarshalling): string {
    return marshalling.prepaymentAccount?.transferAccount?.accountNumber ?? '';
  }

  marshallingAmount(marshalling: IPrepaymentMarshalling): number | null | undefined {
    return marshalling.prepaymentAccount?.prepaymentAmount;
  }

  marshallingNarration(marshalling: IPrepaymentMarshalling): string {
    return marshalling.prepaymentAccount?.particulars ?? '';
  }

  marshallingDealer(marshalling: IPrepaymentMarshalling): string {
    return marshalling.prepaymentAccount?.dealer?.dealerName ?? '';
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentCompilationRequest>>): void {
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

  protected updateForm(prepaymentCompilationRequest: IPrepaymentCompilationRequest): void {
    this.editForm.patchValue({
      id: prepaymentCompilationRequest.id,
      timeOfRequest: prepaymentCompilationRequest.timeOfRequest
        ? prepaymentCompilationRequest.timeOfRequest.format(DATE_TIME_FORMAT)
        : null,
      compilationStatus: prepaymentCompilationRequest.compilationStatus,
      itemsProcessed: prepaymentCompilationRequest.itemsProcessed,
      compilationToken: prepaymentCompilationRequest.compilationToken,
      narration: prepaymentCompilationRequest.narration,
      placeholders: prepaymentCompilationRequest.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(prepaymentCompilationRequest.placeholders ?? [])
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
  }

  protected loadPendingMarshallings(): void {
    this.prepaymentMarshallingService
      .query({ 'processed.equals': false, size: 200, sort: ['id,desc'] })
      .pipe(map((res: HttpResponse<IPrepaymentMarshalling[]>) => res.body ?? []))
      .subscribe((marshallings: IPrepaymentMarshalling[]) => {
        const pendingMarshallings = marshallings.filter(marshalling => marshalling.processed === false && !!marshalling.prepaymentAccount?.id);
        if (!pendingMarshallings.length) {
          this.pendingMarshallings = [];
          return;
        }

        forkJoin(pendingMarshallings.map(marshalling => this.hydratePendingMarshalling(marshalling))).subscribe((items: IPrepaymentMarshalling[]) => {
          this.pendingMarshallings = items;
        });
      });
  }

  protected hydratePendingMarshalling(marshalling: IPrepaymentMarshalling): Observable<IPrepaymentMarshalling> {
    const prepaymentAccount$ = this.prepaymentAccountService.find(marshalling.prepaymentAccount!.id!).pipe(
      map((response: HttpResponse<IPrepaymentAccount>) => response.body ?? marshalling.prepaymentAccount)
    );

    const firstFiscalMonth$ = marshalling.firstFiscalMonth?.id
      ? this.fiscalMonthService.find(marshalling.firstFiscalMonth.id).pipe(
          map((response: HttpResponse<IFiscalMonth>) => response.body ?? marshalling.firstFiscalMonth)
        )
      : of(marshalling.firstFiscalMonth ?? undefined);

    const lastFiscalMonth$ = marshalling.lastFiscalMonth?.id
      ? this.fiscalMonthService.find(marshalling.lastFiscalMonth.id).pipe(
          map((response: HttpResponse<IFiscalMonth>) => response.body ?? marshalling.lastFiscalMonth)
        )
      : of(marshalling.lastFiscalMonth ?? undefined);

    return forkJoin({
      prepaymentAccount: prepaymentAccount$,
      firstFiscalMonth: firstFiscalMonth$,
      lastFiscalMonth: lastFiscalMonth$,
    }).pipe(
      switchMap(({ prepaymentAccount, firstFiscalMonth, lastFiscalMonth }) => {
        const hydratedAccount = prepaymentAccount ?? marshalling.prepaymentAccount;
        const debitAccount$ = hydratedAccount?.debitAccount?.id
          ? this.transactionAccountService.find(hydratedAccount.debitAccount.id).pipe(
              map((response: HttpResponse<ITransactionAccount>) => response.body ?? hydratedAccount.debitAccount)
            )
          : of(hydratedAccount?.debitAccount ?? undefined);
        const transferAccount$ = hydratedAccount?.transferAccount?.id
          ? this.transactionAccountService.find(hydratedAccount.transferAccount.id).pipe(
              map((response: HttpResponse<ITransactionAccount>) => response.body ?? hydratedAccount.transferAccount)
            )
          : of(hydratedAccount?.transferAccount ?? undefined);

        return forkJoin({
          debitAccount: debitAccount$,
          transferAccount: transferAccount$,
        }).pipe(
          map(({ debitAccount, transferAccount }) => ({
            ...marshalling,
            prepaymentAccount: {
              ...(hydratedAccount ?? {}),
              debitAccount,
              transferAccount,
            } as IPrepaymentAccount,
            firstFiscalMonth,
            lastFiscalMonth,
          }))
        );
      })
    );
  }

  protected createFromForm(): IPrepaymentCompilationRequest {
    return {
      ...new PrepaymentCompilationRequest(),
      id: this.editForm.get(['id'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      compilationStatus: this.editForm.get(['compilationStatus'])!.value,
      itemsProcessed: this.editForm.get(['itemsProcessed'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      narration: this.editForm.get(['narration'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
