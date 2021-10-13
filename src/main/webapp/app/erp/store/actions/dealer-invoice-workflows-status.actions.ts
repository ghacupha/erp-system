import {createAction, props} from "@ngrx/store";
import {IDealer} from "../../erp-pages/dealers/dealer/dealer.model";

export const recordDealerInvoiceButtonClicked = createAction(
  '[Dealers Page] record dealer invoice button clicked',
  props<{
    selectedDealer: IDealer,
  }>()
);

export const dealerInvoiceStateReset = createAction(
  '[System] dealer invoice state has been reset');
