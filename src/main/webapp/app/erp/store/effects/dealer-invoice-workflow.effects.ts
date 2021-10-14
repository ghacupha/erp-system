import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {InvoiceService} from "../../erp-pages/payments/invoice/service/invoice.service";
import {
  selectedInvoiceUpdatedWithPayment,
  selectedInvoiceUpdatedWithPaymentSuccessfully, selectedInvoiceUpdateWithPaymentErrored
} from "../actions/dealer-invoice-workflows-status.actions";
import {catchError, map, mergeMap} from "rxjs/operators";
import {of} from "rxjs";
import {IInvoice, Invoice} from "../../erp-pages/payments/invoice/invoice.model";
import {State} from "../global-store.definition";
import {Store} from "@ngrx/store";
import {dealerInvoiceSelected} from "../selectors/dealer-invoice-worklows-status.selectors";

@Injectable()
export class DealerInvoiceWorkflowEffects {

  selectedInvoice: IInvoice = {...new Invoice()};

  dealerInvoiceUpdateEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(selectedInvoiceUpdatedWithPayment),
        // TODO SELECTED INVOICE IS UNDEFINED
        mergeMap(() => this.invoiceService.update(this.selectedInvoice).pipe(
          map(() => selectedInvoiceUpdatedWithPaymentSuccessfully()),
          catchError(error => of(selectedInvoiceUpdateWithPaymentErrored({error})))
          )
        )
      )
  );

  constructor(
    protected actions$: Actions,
    protected invoiceService: InvoiceService,
  protected store: Store<State>) {

    this.store.select<IInvoice>(dealerInvoiceSelected).subscribe(inv => {
      this.selectedInvoice = inv;
    });
  }
}
