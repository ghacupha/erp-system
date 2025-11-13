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

import { IRouInitialDirectCost, RouInitialDirectCost } from '../rou-initial-direct-cost.model';
import { RouInitialDirectCostService } from '../service/rou-initial-direct-cost.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { IFRS16LeaseContractService } from '../../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { TransactionAccountService } from '../../../erp-accounts/transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-rou-initial-direct-cost-update',
  templateUrl: './rou-initial-direct-cost-update.component.html',
})
export class RouInitialDirectCostUpdateComponent implements OnInit {
  isSaving = false;

  iFRS16LeaseContractsSharedCollection: IIFRS16LeaseContract[] = [];
  settlementsSharedCollection: ISettlement[] = [];
  transactionAccountsSharedCollection: ITransactionAccount[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    transactionDate: [null, [Validators.required]],
    description: [],
    cost: [null, [Validators.required, Validators.min(0)]],
    referenceNumber: [],
    leaseContract: [null, Validators.required],
    settlementDetails: [null, Validators.required],
    targetROUAccount: [null, Validators.required],
    transferAccount: [null, Validators.required],
    placeholders: [],
  });

  constructor(
    protected rouInitialDirectCostService: RouInitialDirectCostService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected settlementService: SettlementService,
    protected transactionAccountService: TransactionAccountService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouInitialDirectCost }) => {
      this.updateForm(rouInitialDirectCost);

      if (rouInitialDirectCost.id === undefined ) {
        this.rouInitialDirectCostService.getNextReferenceNumber().subscribe(nextValue => {
          if (nextValue.body) {
            this.editForm.patchValue({
              referenceNumber: nextValue.body,
            })
          }
        });
      }

    });

    this.updateDescriptionGivenSettlement();
  }

  updateDescriptionGivenSettlement(): void {
    this.editForm.get(['settlementDetails'])?.valueChanges.subscribe((settlementDetailsChange) => {
      if (settlementDetailsChange.id) {
        this.settlementService.find(settlementDetailsChange.id).subscribe(settlementDetailsResponse => {
          if (settlementDetailsResponse.body) {
            const settlementDetailsBody = settlementDetailsResponse.body;
            this.editForm.patchValue({
              cost: settlementDetailsBody.paymentAmount,
              description: settlementDetailsBody.description,
              transactionDate: settlementDetailsBody.paymentDate
            });
          }
        });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rouInitialDirectCost = this.createFromForm();
    if (rouInitialDirectCost.id !== undefined) {
      this.subscribeToSaveResponse(this.rouInitialDirectCostService.update(rouInitialDirectCost));
    } else {
      this.subscribeToSaveResponse(this.rouInitialDirectCostService.create(rouInitialDirectCost));
    }
  }

  updatePlaceholders($event: IPlaceholder[]) {
    this.editForm.patchValue({
      placeholders: [...$event]
    })
  }

  transferAccount($event: ITransactionAccount) {
    this.editForm.patchValue({
      transferAccount: $event
    })
  }

  targetROUAccount($event: ITransactionAccount) {
    this.editForm.patchValue({
      targetROUAccount: $event
    })
  }

  settlementDetails($event: ISettlement) {
    this.editForm.patchValue({
      settlementDetails: $event
    })
  }

  leaseContract($event: IIFRS16LeaseContract) {
    this.editForm.patchValue({
      leaseContract: $event
    })
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouInitialDirectCost>>): void {
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

  protected updateForm(rouInitialDirectCost: IRouInitialDirectCost): void {
    this.editForm.patchValue({
      id: rouInitialDirectCost.id,
      transactionDate: rouInitialDirectCost.transactionDate,
      description: rouInitialDirectCost.description,
      cost: rouInitialDirectCost.cost,
      referenceNumber: rouInitialDirectCost.referenceNumber,
      leaseContract: rouInitialDirectCost.leaseContract,
      settlementDetails: rouInitialDirectCost.settlementDetails,
      targetROUAccount: rouInitialDirectCost.targetROUAccount,
      transferAccount: rouInitialDirectCost.transferAccount,
      placeholders: rouInitialDirectCost.placeholders,
    });

    this.iFRS16LeaseContractsSharedCollection = this.iFRS16LeaseContractService.addIFRS16LeaseContractToCollectionIfMissing(
      this.iFRS16LeaseContractsSharedCollection,
      rouInitialDirectCost.leaseContract
    );
    this.settlementsSharedCollection = this.settlementService.addSettlementToCollectionIfMissing(
      this.settlementsSharedCollection,
      rouInitialDirectCost.settlementDetails
    );
    this.transactionAccountsSharedCollection = this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
      this.transactionAccountsSharedCollection,
      rouInitialDirectCost.targetROUAccount,
      rouInitialDirectCost.transferAccount
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(rouInitialDirectCost.placeholders ?? [])
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
            this.editForm.get('leaseContract')!.value
          )
        )
      )
      .subscribe((iFRS16LeaseContracts: IIFRS16LeaseContract[]) => (this.iFRS16LeaseContractsSharedCollection = iFRS16LeaseContracts));

    this.settlementService
      .query()
      .pipe(map((res: HttpResponse<ISettlement[]>) => res.body ?? []))
      .pipe(
        map((settlements: ISettlement[]) =>
          this.settlementService.addSettlementToCollectionIfMissing(settlements, this.editForm.get('settlementDetails')!.value)
        )
      )
      .subscribe((settlements: ISettlement[]) => (this.settlementsSharedCollection = settlements));

    this.transactionAccountService
      .query()
      .pipe(map((res: HttpResponse<ITransactionAccount[]>) => res.body ?? []))
      .pipe(
        map((transactionAccounts: ITransactionAccount[]) =>
          this.transactionAccountService.addTransactionAccountToCollectionIfMissing(
            transactionAccounts,
            this.editForm.get('targetROUAccount')!.value,
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
  }

  protected createFromForm(): IRouInitialDirectCost {
    return {
      ...new RouInitialDirectCost(),
      id: this.editForm.get(['id'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      description: this.editForm.get(['description'])!.value,
      cost: this.editForm.get(['cost'])!.value,
      referenceNumber: this.editForm.get(['referenceNumber'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value,
      settlementDetails: this.editForm.get(['settlementDetails'])!.value,
      targetROUAccount: this.editForm.get(['targetROUAccount'])!.value,
      transferAccount: this.editForm.get(['transferAccount'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
