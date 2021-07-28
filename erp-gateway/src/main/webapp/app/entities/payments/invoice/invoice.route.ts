import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInvoice, Invoice } from 'app/shared/model/payments/invoice.model';
import { InvoiceService } from './invoice.service';
import { InvoiceComponent } from './invoice.component';
import { InvoiceDetailComponent } from './invoice-detail.component';
import { InvoiceUpdateComponent } from './invoice-update.component';

@Injectable({ providedIn: 'root' })
export class InvoiceResolve implements Resolve<IInvoice> {
  constructor(private service: InvoiceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((invoice: HttpResponse<Invoice>) => {
          if (invoice.body) {
            return of(invoice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Invoice());
  }
}

export const invoiceRoute: Routes = [
  {
    path: '',
    component: InvoiceComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Invoices',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceDetailComponent,
    resolve: {
      invoice: InvoiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Invoices',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceUpdateComponent,
    resolve: {
      invoice: InvoiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Invoices',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceUpdateComponent,
    resolve: {
      invoice: InvoiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Invoices',
    },
    canActivate: [UserRouteAccessService],
  },
];
