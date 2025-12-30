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

import { Component, Inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILeaseTemplate, LeaseTemplate } from '../lease-template.model';
import { LeaseTemplateService } from '../service/lease-template.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingLeaseTemplateStatus,
  creatingLeaseTemplateStatus,
  editingLeaseTemplateStatus,
  leaseTemplateUpdateSelectedInstance
} from '../../../store/selectors/lease-template-workflows-status.selector';
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';
import { IAssetCategory } from '../../../erp-assets/asset-category/asset-category.model';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';

@Component({
  selector: 'jhi-lease-template-update',
  templateUrl: './lease-template-update.component.html',
})
export class LeaseTemplateUpdateComponent implements OnInit {
  isSaving = false;

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = { ...new LeaseTemplate() };

  editForm = this.fb.group({
    id: [],
    templateTitle: [null, [Validators.required]],
    assetAccount: [],
    depreciationAccount: [],
    accruedDepreciationAccount: [],
    interestPaidTransferDebitAccount: [],
    interestPaidTransferCreditAccount: [],
    interestAccruedDebitAccount: [],
    interestAccruedCreditAccount: [],
    leaseRecognitionDebitAccount: [],
    leaseRecognitionCreditAccount: [],
    leaseRepaymentDebitAccount: [],
    leaseRepaymentCreditAccount: [],
    rouRecognitionCreditAccount: [],
    rouRecognitionDebitAccount: [],
    assetCategory: [],
    serviceOutlet: [],
    mainDealer: [],
  });

  constructor(
    @Inject(LeaseTemplateService) protected leaseTemplateService: LeaseTemplateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
  ) {
    this.store.pipe(select(copyingLeaseTemplateStatus)).subscribe(stat => (this.weAreCopying = stat));
    this.store.pipe(select(editingLeaseTemplateStatus)).subscribe(stat => (this.weAreEditing = stat));
    this.store.pipe(select(creatingLeaseTemplateStatus)).subscribe(stat => (this.weAreCreating = stat));
    this.store.pipe(select(leaseTemplateUpdateSelectedInstance)).subscribe(copied => (this.selectedItem = copied));
  }

  ngOnInit(): void {
    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {
      this.copyForm(this.selectedItem);
    }

    this.activatedRoute.data.subscribe(({ leaseTemplate }) => {
      if (!this.weAreEditing && !this.weAreCopying && leaseTemplate) {
        this.updateForm(leaseTemplate);
      }
    });
  }

  updateAssetAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ assetAccount: update });
  }

  updateDepreciationAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ depreciationAccount: update });
  }

  updateAccruedDepreciationAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ accruedDepreciationAccount: update });
  }

  updateInterestPaidTransferDebitAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ interestPaidTransferDebitAccount: update });
  }

  updateInterestPaidTransferCreditAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ interestPaidTransferCreditAccount: update });
  }

  updateInterestAccruedDebitAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ interestAccruedDebitAccount: update });
  }

  updateInterestAccruedCreditAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ interestAccruedCreditAccount: update });
  }

  updateLeaseRecognitionDebitAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ leaseRecognitionDebitAccount: update });
  }

  updateLeaseRecognitionCreditAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ leaseRecognitionCreditAccount: update });
  }

  updateLeaseRepaymentDebitAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ leaseRepaymentDebitAccount: update });
  }

  updateLeaseRepaymentCreditAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ leaseRepaymentCreditAccount: update });
  }

  updateRouRecognitionCreditAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ rouRecognitionCreditAccount: update });
  }

  updateRouRecognitionDebitAccount(update: ITransactionAccount): void {
    this.editForm.patchValue({ rouRecognitionDebitAccount: update });
  }

  updateAssetCategory(update: IAssetCategory): void {
    this.editForm.patchValue({ assetCategory: update });
  }

  updateServiceOutlet(update: IServiceOutlet): void {
    this.editForm.patchValue({ serviceOutlet: update });
  }

  updateMainDealer(update: IDealer): void {
    this.editForm.patchValue({ mainDealer: update });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.weAreCopying) {
      this.subscribeToSaveResponse(this.leaseTemplateService.create(this.copyFromForm()));
      return;
    }
    if (this.weAreEditing) {
      this.subscribeToSaveResponse(this.leaseTemplateService.update(this.createFromForm()));
      return;
    }
    this.subscribeToSaveResponse(this.leaseTemplateService.create(this.createFromForm()));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseTemplate>>): void {
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

  protected updateForm(leaseTemplate: ILeaseTemplate): void {
    this.editForm.patchValue({
      id: leaseTemplate.id,
      templateTitle: leaseTemplate.templateTitle,
      assetAccount: leaseTemplate.assetAccount,
      depreciationAccount: leaseTemplate.depreciationAccount,
      accruedDepreciationAccount: leaseTemplate.accruedDepreciationAccount,
      interestPaidTransferDebitAccount: leaseTemplate.interestPaidTransferDebitAccount,
      interestPaidTransferCreditAccount: leaseTemplate.interestPaidTransferCreditAccount,
      interestAccruedDebitAccount: leaseTemplate.interestAccruedDebitAccount,
      interestAccruedCreditAccount: leaseTemplate.interestAccruedCreditAccount,
      leaseRecognitionDebitAccount: leaseTemplate.leaseRecognitionDebitAccount,
      leaseRecognitionCreditAccount: leaseTemplate.leaseRecognitionCreditAccount,
      leaseRepaymentDebitAccount: leaseTemplate.leaseRepaymentDebitAccount,
      leaseRepaymentCreditAccount: leaseTemplate.leaseRepaymentCreditAccount,
      rouRecognitionCreditAccount: leaseTemplate.rouRecognitionCreditAccount,
      rouRecognitionDebitAccount: leaseTemplate.rouRecognitionDebitAccount,
      assetCategory: leaseTemplate.assetCategory,
      serviceOutlet: leaseTemplate.serviceOutlet,
      mainDealer: leaseTemplate.mainDealer,
    });
  }

  protected copyForm(leaseTemplate: ILeaseTemplate): void {
    this.editForm.patchValue({
      id: undefined,
      templateTitle: leaseTemplate.templateTitle,
      assetAccount: leaseTemplate.assetAccount,
      depreciationAccount: leaseTemplate.depreciationAccount,
      accruedDepreciationAccount: leaseTemplate.accruedDepreciationAccount,
      interestPaidTransferDebitAccount: leaseTemplate.interestPaidTransferDebitAccount,
      interestPaidTransferCreditAccount: leaseTemplate.interestPaidTransferCreditAccount,
      interestAccruedDebitAccount: leaseTemplate.interestAccruedDebitAccount,
      interestAccruedCreditAccount: leaseTemplate.interestAccruedCreditAccount,
      leaseRecognitionDebitAccount: leaseTemplate.leaseRecognitionDebitAccount,
      leaseRecognitionCreditAccount: leaseTemplate.leaseRecognitionCreditAccount,
      leaseRepaymentDebitAccount: leaseTemplate.leaseRepaymentDebitAccount,
      leaseRepaymentCreditAccount: leaseTemplate.leaseRepaymentCreditAccount,
      rouRecognitionCreditAccount: leaseTemplate.rouRecognitionCreditAccount,
      rouRecognitionDebitAccount: leaseTemplate.rouRecognitionDebitAccount,
      assetCategory: leaseTemplate.assetCategory,
      serviceOutlet: leaseTemplate.serviceOutlet,
      mainDealer: leaseTemplate.mainDealer,
    });
  }

  protected createFromForm(): ILeaseTemplate {
    return {
      ...new LeaseTemplate(),
      id: this.editForm.get(['id'])!.value,
      templateTitle: this.editForm.get(['templateTitle'])!.value,
      assetAccount: this.editForm.get(['assetAccount'])!.value,
      depreciationAccount: this.editForm.get(['depreciationAccount'])!.value,
      accruedDepreciationAccount: this.editForm.get(['accruedDepreciationAccount'])!.value,
      interestPaidTransferDebitAccount: this.editForm.get(['interestPaidTransferDebitAccount'])!.value,
      interestPaidTransferCreditAccount: this.editForm.get(['interestPaidTransferCreditAccount'])!.value,
      interestAccruedDebitAccount: this.editForm.get(['interestAccruedDebitAccount'])!.value,
      interestAccruedCreditAccount: this.editForm.get(['interestAccruedCreditAccount'])!.value,
      leaseRecognitionDebitAccount: this.editForm.get(['leaseRecognitionDebitAccount'])!.value,
      leaseRecognitionCreditAccount: this.editForm.get(['leaseRecognitionCreditAccount'])!.value,
      leaseRepaymentDebitAccount: this.editForm.get(['leaseRepaymentDebitAccount'])!.value,
      leaseRepaymentCreditAccount: this.editForm.get(['leaseRepaymentCreditAccount'])!.value,
      rouRecognitionCreditAccount: this.editForm.get(['rouRecognitionCreditAccount'])!.value,
      rouRecognitionDebitAccount: this.editForm.get(['rouRecognitionDebitAccount'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      mainDealer: this.editForm.get(['mainDealer'])!.value,
    };
  }

  protected copyFromForm(): ILeaseTemplate {
    return {
      ...new LeaseTemplate(),
      templateTitle: this.editForm.get(['templateTitle'])!.value,
      assetAccount: this.editForm.get(['assetAccount'])!.value,
      depreciationAccount: this.editForm.get(['depreciationAccount'])!.value,
      accruedDepreciationAccount: this.editForm.get(['accruedDepreciationAccount'])!.value,
      interestPaidTransferDebitAccount: this.editForm.get(['interestPaidTransferDebitAccount'])!.value,
      interestPaidTransferCreditAccount: this.editForm.get(['interestPaidTransferCreditAccount'])!.value,
      interestAccruedDebitAccount: this.editForm.get(['interestAccruedDebitAccount'])!.value,
      interestAccruedCreditAccount: this.editForm.get(['interestAccruedCreditAccount'])!.value,
      leaseRecognitionDebitAccount: this.editForm.get(['leaseRecognitionDebitAccount'])!.value,
      leaseRecognitionCreditAccount: this.editForm.get(['leaseRecognitionCreditAccount'])!.value,
      leaseRepaymentDebitAccount: this.editForm.get(['leaseRepaymentDebitAccount'])!.value,
      leaseRepaymentCreditAccount: this.editForm.get(['leaseRepaymentCreditAccount'])!.value,
      rouRecognitionCreditAccount: this.editForm.get(['rouRecognitionCreditAccount'])!.value,
      rouRecognitionDebitAccount: this.editForm.get(['rouRecognitionDebitAccount'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      mainDealer: this.editForm.get(['mainDealer'])!.value,
    };
  }
}
