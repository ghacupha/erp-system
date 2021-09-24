
import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {IPayment, Payment} from "../payment.model";
import {Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {Observable, of} from "rxjs";
import {newPaymentButtonClicked} from "../../../../store/update-menu-status.actions";
import {
  DEEFAULT_CONVERSION_RATE,
  DEFAULT_CURRENCY,
  DEFAULT_DATE, DEFAULT_DESCRIPTION, DEFAULT_DISBURSEMENT_COST, DEFAULT_INVOICE_AMOUNT,
  DEFAULT_TRANSACTION_AMOUNT, DEFAULT_VATABLE_AMOUNT
} from "../default-values.constants";
import {Injectable} from "@angular/core";

/**
 * Provides the New Payment form with default values
 */
@Injectable({ providedIn: 'root' })
export class NewPaymentResolveService implements Resolve<IPayment> {

  constructor(
    protected store: Store<State>
  ) {
  }


  /* eslint-disable-next-line */
  resolve(route: ActivatedRouteSnapshot): Observable<IPayment> | Observable<never> {

    const payment: Payment = {
      paymentDate: DEFAULT_DATE,
      settlementCurrency: DEFAULT_CURRENCY,
      paymentAmount: DEFAULT_TRANSACTION_AMOUNT,
      invoicedAmount: DEFAULT_INVOICE_AMOUNT,
      disbursementCost: DEFAULT_DISBURSEMENT_COST,
      vatableAmount: DEFAULT_VATABLE_AMOUNT,
      conversionRate: DEEFAULT_CONVERSION_RATE,
      description: DEFAULT_DESCRIPTION
    }

    this.store.dispatch(newPaymentButtonClicked({newPayment: payment}))

    return of(payment);
  }
}
