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
