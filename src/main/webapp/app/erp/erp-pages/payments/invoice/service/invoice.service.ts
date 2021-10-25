import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import {IInvoice, getInvoiceIdentifier, Invoice} from '../invoice.model';
import {Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {
  dealerInvoiceSelectedPayment
} from "../../../../store/selectors/dealer-invoice-worklows-status.selectors";
import {IPayment, Payment} from "../../payment/payment.model";

export type EntityResponseType = HttpResponse<IInvoice>;
export type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  selectedPayment: IPayment = {...new Payment()}

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoices');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/invoices');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    protected store: Store<State>) {

    this.store.select<IPayment>(dealerInvoiceSelectedPayment).subscribe(pyt => {
      this.selectedPayment = pyt;
    });
  }

  create(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .post<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .put<IInvoice>(`${this.resourceUrl}/${getInvoiceIdentifier(invoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateWithPayment(acquiredInvoice: IInvoice): Observable<IInvoice> {

    // Update the payment in the invoice
    acquiredInvoice = {
      ...acquiredInvoice,
      paymentId: this.selectedPayment.id,
    }

    this.update(acquiredInvoice).subscribe(res => {
      if (res.body) {
        return of(res.body)
      }
    });

    return of(acquiredInvoice)
  }

  partialUpdate(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .patch<IInvoice>(`${this.resourceUrl}/${getInvoiceIdentifier(invoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findEntity(id: number): Observable<IInvoice> {
    let invoice: IInvoice = {...new Invoice(), id}

    this.find(id).subscribe(res => {
      if (res.body) {
        invoice = res.body
      }
    });

    return of(invoice);
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoice[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addInvoiceToCollectionIfMissing(invoiceCollection: IInvoice[], ...invoicesToCheck: (IInvoice | null | undefined)[]): IInvoice[] {
    const invoices: IInvoice[] = invoicesToCheck.filter(isPresent);
    if (invoices.length > 0) {
      const invoiceCollectionIdentifiers = invoiceCollection.map(invoiceItem => getInvoiceIdentifier(invoiceItem)!);
      const invoicesToAdd = invoices.filter(invoiceItem => {
        const invoiceIdentifier = getInvoiceIdentifier(invoiceItem);
        if (invoiceIdentifier == null || invoiceCollectionIdentifiers.includes(invoiceIdentifier)) {
          return false;
        }
        invoiceCollectionIdentifiers.push(invoiceIdentifier);
        return true;
      });
      return [...invoicesToAdd, ...invoiceCollection];
    }
    return invoiceCollection;
  }

  protected convertDateFromClient(invoice: IInvoice): IInvoice {
    return Object.assign({}, invoice, {
      invoiceDate: invoice.invoiceDate?.isValid() ? invoice.invoiceDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.invoiceDate = res.body.invoiceDate ? dayjs(res.body.invoiceDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((invoice: IInvoice) => {
        invoice.invoiceDate = invoice.invoiceDate ? dayjs(invoice.invoiceDate) : undefined;
      });
    }
    return res;
  }
}
