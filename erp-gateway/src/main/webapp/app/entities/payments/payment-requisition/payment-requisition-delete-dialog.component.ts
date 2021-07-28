///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentRequisition } from 'app/shared/model/payments/payment-requisition.model';
import { PaymentRequisitionService } from './payment-requisition.service';

@Component({
  templateUrl: './payment-requisition-delete-dialog.component.html',
})
export class PaymentRequisitionDeleteDialogComponent {
  paymentRequisition?: IPaymentRequisition;

  constructor(
    protected paymentRequisitionService: PaymentRequisitionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentRequisitionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentRequisitionListModification');
      this.activeModal.close();
    });
  }
}
