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
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';

import { ILeasePayment, LeasePayment } from '../lease-payment.model';
import { LeasePaymentService } from '../service/lease-payment.service';
import { IFRS16LeaseContract, IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingLeasePaymentStatus,
  creatingLeasePaymentStatus,
  editingLeasePaymentStatus,
  leasePaymentSelectedInstance
} from '../../../store/selectors/lease-payment-workflows-status.selector';

@Component({
  selector: 'jhi-lease-payment-update',
  templateUrl: './lease-payment-update.component.html'
})
export class LeasePaymentUpdateComponent implements OnInit {
  isSaving = false;

  iFRS16LeaseContractsSharedCollection: IIFRS16LeaseContract[] = [];

  editForm = this.fb.group({
    id: [],
    paymentDate: [],
    paymentAmount: [],
    leaseContract: [null, Validators.required]
  });

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = { ...new LeasePayment() };
  selectedLeaseContract = { ...new IFRS16LeaseContract() };

  constructor(
    protected leasePaymentService: LeasePaymentService,
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>
  ) {
    this.store.pipe(select(copyingLeasePaymentStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingLeasePaymentStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingLeasePaymentStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(leasePaymentSelectedInstance)).subscribe(copied => {
      this.selectedItem = copied;

      if (this.selectedItem.leaseContract) {
        const ifrs16Lease: IIFRS16LeaseContract = this.selectedItem.leaseContract;
        if (ifrs16Lease.id !== undefined) {
          this.iFRS16LeaseContractService.find(ifrs16Lease.id).subscribe(leaseContractResponse => {
            if (leaseContractResponse.body) {
              this.selectedLeaseContract = leaseContractResponse.body;
            }
          });
        }
      }
    });
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCreating) {
      this.editForm.patchValue({
        paymentDate: dayjs()
      });
    }
  }

  updateLeaseContract(value: IIFRS16LeaseContract): void {
    this.editForm.patchValue({
      leaseContract: value
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leasePaymentService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leasePaymentService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.leasePaymentService.create(this.copyFromForm()));
  }

  trackIFRS16LeaseContractById(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeasePayment>>): void {
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

  protected updateForm(leasePayment: ILeasePayment): void {
    this.editForm.patchValue({
      id: leasePayment.id,
      paymentAmount: leasePayment.paymentAmount,
      paymentDate: leasePayment.paymentDate,
      leaseContract: leasePayment.leaseContract
    });
  }

  protected createFromForm(): ILeasePayment {
    return {
      ...new LeasePayment(),
      id: this.editForm.get(['id'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value
    };
  }

  protected copyFromForm(): ILeasePayment {
    return {
      ...new LeasePayment(),
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      leaseContract: this.editForm.get(['leaseContract'])!.value
    };
  }
}
