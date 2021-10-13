import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {Resolve} from "@angular/router";
import {IPayment, Payment} from "../payment.model";
import {Observable, of} from "rxjs";
import {
  dealerInvoiceSelected, dealerInvoiceSelectedDealer,
} from "../../../../store/selectors/dealer-invoice-worklows-status.actions";
import {IInvoice} from "../../invoice/invoice.model";
import {
  DEFAULT_CONVERSION_RATE,
  DEFAULT_CURRENCY,
  DEFAULT_DATE, DEFAULT_DESCRIPTION, DEFAULT_DISBURSEMENT_COST,
  DEFAULT_INVOICE_AMOUNT,
  DEFAULT_TRANSACTION_AMOUNT, DEFAULT_VATABLE_AMOUNT
} from "../default-values.constants";
import {IDealer} from "../../../dealers/dealer/dealer.model";

@Injectable({ providedIn: 'root' })
export class DealerInvoicePaymentResolveService implements Resolve<IPayment>  {

  constructor(protected store: Store<State>) {}

  resolve(): Observable<IPayment> | Promise<IPayment> | IPayment {

    // UPDATE DEFAULT VALUES
    let payment: IPayment = {
      ...new Payment(),
      paymentDate: DEFAULT_DATE,
      disbursementCost: DEFAULT_DISBURSEMENT_COST,
      vatableAmount: DEFAULT_VATABLE_AMOUNT,
      description: DEFAULT_DESCRIPTION
    };

    const invoice: Observable<IInvoice> = this.store.select<IInvoice>(dealerInvoiceSelected);

    const dealer: Observable<IDealer> = this.store.select<IDealer>(dealerInvoiceSelectedDealer);

    // TODO ownedInvoices in the store

    // UPDATE VALUES FROM THE INVOICE
    invoice.subscribe(inv => {
      payment = {
        ...payment,
        invoicedAmount: inv.invoiceAmount,
        settlementCurrency: inv.currency,
        conversionRate: inv.conversionRate,
        paymentAmount: inv.invoiceAmount
      }
    });

    // UPDATE WITH VALUES FROM THE DEALER
    dealer.subscribe(dealr => {
      payment = {
        ...payment,
        dealer: dealr,
        paymentLabels: dealr.paymentLabels,
        placeholders: dealr.placeholders,
      }
    });

    return of(payment)
  }


}
