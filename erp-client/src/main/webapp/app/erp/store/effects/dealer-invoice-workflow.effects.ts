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

import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {InvoiceService} from "../../erp-pages/payments/invoice/service/invoice.service";
import {
  selectedInvoiceForUpdateAcquired, selectedInvoiceUpdatedRequisitioned,
  selectedInvoiceUpdatedWithPaymentSuccessfully, selectedInvoiceUpdateWithPaymentErrored
} from "../actions/dealer-invoice-workflows-status.actions";
import {catchError, map, mergeMap} from "rxjs/operators";
import {of} from "rxjs";

@Injectable()
export class DealerInvoiceWorkflowEffects {

  dealerInvoiceUpdateEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(selectedInvoiceUpdatedRequisitioned),
        mergeMap((action) => this.invoiceService.findEntity(action.selectedInvoiceId).pipe(
          map((acquiredInvoice) => selectedInvoiceForUpdateAcquired({acquiredInvoice})),
          catchError(error => of(selectedInvoiceUpdateWithPaymentErrored({error})))
          )
        )
      )
  );

  invoiceUpdateWithPaymentEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(selectedInvoiceForUpdateAcquired),
        mergeMap((action) => this.invoiceService.updateWithPayment(action.acquiredInvoice).pipe(
          map(() => selectedInvoiceUpdatedWithPaymentSuccessfully()),
          catchError(error => of(selectedInvoiceUpdateWithPaymentErrored({error})))
          )
        )
      )
  );

  constructor(
    protected actions$: Actions,
    protected invoiceService: InvoiceService) {
  }
}
