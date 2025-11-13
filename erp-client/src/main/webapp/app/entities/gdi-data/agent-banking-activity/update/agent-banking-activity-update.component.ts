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

import { IAgentBankingActivity, AgentBankingActivity } from '../agent-banking-activity.model';
import { AgentBankingActivityService } from '../service/agent-banking-activity.service';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { InstitutionCodeService } from 'app/entities/gdi/institution-code/service/institution-code.service';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { BankBranchCodeService } from 'app/entities/system/bank-branch-code/service/bank-branch-code.service';
import { IBankTransactionType } from 'app/entities/gdi/bank-transaction-type/bank-transaction-type.model';
import { BankTransactionTypeService } from 'app/entities/gdi/bank-transaction-type/service/bank-transaction-type.service';

@Component({
  selector: 'jhi-agent-banking-activity-update',
  templateUrl: './agent-banking-activity-update.component.html',
})
export class AgentBankingActivityUpdateComponent implements OnInit {
  isSaving = false;

  institutionCodesSharedCollection: IInstitutionCode[] = [];
  bankBranchCodesSharedCollection: IBankBranchCode[] = [];
  bankTransactionTypesSharedCollection: IBankTransactionType[] = [];

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    agentUniqueId: [null, [Validators.required]],
    terminalUniqueId: [null, [Validators.required]],
    totalCountOfTransactions: [null, [Validators.required, Validators.min(0)]],
    totalValueOfTransactionsInLCY: [null, [Validators.required, Validators.min(0)]],
    bankCode: [null, Validators.required],
    branchCode: [null, Validators.required],
    transactionType: [null, Validators.required],
  });

  constructor(
    protected agentBankingActivityService: AgentBankingActivityService,
    protected institutionCodeService: InstitutionCodeService,
    protected bankBranchCodeService: BankBranchCodeService,
    protected bankTransactionTypeService: BankTransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agentBankingActivity }) => {
      this.updateForm(agentBankingActivity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agentBankingActivity = this.createFromForm();
    if (agentBankingActivity.id !== undefined) {
      this.subscribeToSaveResponse(this.agentBankingActivityService.update(agentBankingActivity));
    } else {
      this.subscribeToSaveResponse(this.agentBankingActivityService.create(agentBankingActivity));
    }
  }

  trackInstitutionCodeById(index: number, item: IInstitutionCode): number {
    return item.id!;
  }

  trackBankBranchCodeById(index: number, item: IBankBranchCode): number {
    return item.id!;
  }

  trackBankTransactionTypeById(index: number, item: IBankTransactionType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgentBankingActivity>>): void {
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

  protected updateForm(agentBankingActivity: IAgentBankingActivity): void {
    this.editForm.patchValue({
      id: agentBankingActivity.id,
      reportingDate: agentBankingActivity.reportingDate,
      agentUniqueId: agentBankingActivity.agentUniqueId,
      terminalUniqueId: agentBankingActivity.terminalUniqueId,
      totalCountOfTransactions: agentBankingActivity.totalCountOfTransactions,
      totalValueOfTransactionsInLCY: agentBankingActivity.totalValueOfTransactionsInLCY,
      bankCode: agentBankingActivity.bankCode,
      branchCode: agentBankingActivity.branchCode,
      transactionType: agentBankingActivity.transactionType,
    });

    this.institutionCodesSharedCollection = this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(
      this.institutionCodesSharedCollection,
      agentBankingActivity.bankCode
    );
    this.bankBranchCodesSharedCollection = this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(
      this.bankBranchCodesSharedCollection,
      agentBankingActivity.branchCode
    );
    this.bankTransactionTypesSharedCollection = this.bankTransactionTypeService.addBankTransactionTypeToCollectionIfMissing(
      this.bankTransactionTypesSharedCollection,
      agentBankingActivity.transactionType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.institutionCodeService
      .query()
      .pipe(map((res: HttpResponse<IInstitutionCode[]>) => res.body ?? []))
      .pipe(
        map((institutionCodes: IInstitutionCode[]) =>
          this.institutionCodeService.addInstitutionCodeToCollectionIfMissing(institutionCodes, this.editForm.get('bankCode')!.value)
        )
      )
      .subscribe((institutionCodes: IInstitutionCode[]) => (this.institutionCodesSharedCollection = institutionCodes));

    this.bankBranchCodeService
      .query()
      .pipe(map((res: HttpResponse<IBankBranchCode[]>) => res.body ?? []))
      .pipe(
        map((bankBranchCodes: IBankBranchCode[]) =>
          this.bankBranchCodeService.addBankBranchCodeToCollectionIfMissing(bankBranchCodes, this.editForm.get('branchCode')!.value)
        )
      )
      .subscribe((bankBranchCodes: IBankBranchCode[]) => (this.bankBranchCodesSharedCollection = bankBranchCodes));

    this.bankTransactionTypeService
      .query()
      .pipe(map((res: HttpResponse<IBankTransactionType[]>) => res.body ?? []))
      .pipe(
        map((bankTransactionTypes: IBankTransactionType[]) =>
          this.bankTransactionTypeService.addBankTransactionTypeToCollectionIfMissing(
            bankTransactionTypes,
            this.editForm.get('transactionType')!.value
          )
        )
      )
      .subscribe((bankTransactionTypes: IBankTransactionType[]) => (this.bankTransactionTypesSharedCollection = bankTransactionTypes));
  }

  protected createFromForm(): IAgentBankingActivity {
    return {
      ...new AgentBankingActivity(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      agentUniqueId: this.editForm.get(['agentUniqueId'])!.value,
      terminalUniqueId: this.editForm.get(['terminalUniqueId'])!.value,
      totalCountOfTransactions: this.editForm.get(['totalCountOfTransactions'])!.value,
      totalValueOfTransactionsInLCY: this.editForm.get(['totalValueOfTransactionsInLCY'])!.value,
      bankCode: this.editForm.get(['bankCode'])!.value,
      branchCode: this.editForm.get(['branchCode'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
    };
  }
}
