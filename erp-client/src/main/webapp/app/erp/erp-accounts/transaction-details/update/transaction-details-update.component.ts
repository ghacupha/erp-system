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

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITransactionDetails, TransactionDetails } from '../transaction-details.model';
import { TransactionDetailsService } from '../service/transaction-details.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccount } from '../../transaction-account/transaction-account.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { TransactionAccountService } from '../../transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-transaction-details-update',
  templateUrl: './transaction-details-update.component.html',
})
export class TransactionDetailsUpdateComponent implements OnInit {
  isSaving = false;

  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    entryId: [null, [Validators.required]],
    transactionDate: [null, [Validators.required]],
    description: [],
    amount: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    modifiedAt: [],
    transactionType: [],
    debitAccount: [null, Validators.required],
    creditAccount: [null, Validators.required],
    placeholders: [],
    postedBy: [],
  });

  constructor(
    protected transactionDetailsService: TransactionDetailsService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionDetails }) => {
      if (transactionDetails.id === undefined) {
        const today = dayjs().startOf('day');
        transactionDetails.createdAt = today;
        transactionDetails.modifiedAt = today;
      }

      this.updateForm(transactionDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionDetails = this.createFromForm();
    if (transactionDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionDetailsService.update(transactionDetails));
    } else {
      this.subscribeToSaveResponse(this.transactionDetailsService.create(transactionDetails));
    }
  }

  trackTransactionAccountById(index: number, item: ITransactionAccount): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionDetails>>): void {
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

  protected updateForm(transactionDetails: ITransactionDetails): void {
    this.editForm.patchValue({
      id: transactionDetails.id,
      entryId: transactionDetails.entryId,
      transactionDate: transactionDetails.transactionDate,
      description: transactionDetails.description,
      amount: transactionDetails.amount,
      createdAt: transactionDetails.createdAt ? transactionDetails.createdAt.format(DATE_TIME_FORMAT) : null,
      modifiedAt: transactionDetails.modifiedAt ? transactionDetails.modifiedAt.format(DATE_TIME_FORMAT) : null,
      transactionType: transactionDetails.transactionType,
      debitAccount: transactionDetails.debitAccount,
      creditAccount: transactionDetails.creditAccount,
      placeholders: transactionDetails.placeholders,
      postedBy: transactionDetails.postedBy,
    });

    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      transactionDetails.debitAccount,
      transactionDetails.creditAccount
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(transactionDetails.placeholders ?? [])
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      transactionDetails.postedBy
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

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('postedBy')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): ITransactionDetails {
    return {
      ...new TransactionDetails(),
      id: this.editForm.get(['id'])!.value,
      entryId: this.editForm.get(['entryId'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      description: this.editForm.get(['description'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      modifiedAt: this.editForm.get(['modifiedAt'])!.value ? dayjs(this.editForm.get(['modifiedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      transactionType: this.editForm.get(['transactionType'])!.value,
      debitAccount: this.editForm.get(['debitAccount'])!.value,
      creditAccount: this.editForm.get(['creditAccount'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      postedBy: this.editForm.get(['postedBy'])!.value,
    };
  }
}
