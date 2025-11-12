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

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'settlement',
        data: {
          pageTitle: 'ERP-Payments | Settlements',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_PREPAYMENTS_MODULE_USER',
            'ROLE_FIXED_ASSETS_USER',
            'ROLE_LEASE_MANAGER'
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./settlement/settlement.module').then(m => m.SettlementModule),
      },
      {
        path: 'payment-category',
        data: {
          pageTitle: 'ERP-Payments | PaymentCategories',
          authorities: ['ROLE_PAYMENTS_USER'],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./payments/payment-category/payment-category.module').then(m => m.ErpServicePaymentCategoryModule),
      },
      {
        path: 'settlement-currency',
        data: {
          pageTitle: 'ERP-Payments | SettlementCurrencies',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_PREPAYMENTS_MODULE_USER',
            'ROLE_TAX_MODULE_USER',
            'ROLE_LEASE_MANAGER',
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./settlement-currency/settlement-currency.module').then(m => m.SettlementCurrencyModule),
      },
      {
        path: 'purchase-order',
        data: {
          pageTitle: 'ERP-Payments | PurchaseOrders',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_PREPAYMENTS_MODULE_USER',
            'ROLE_FIXED_ASSETS_USER'
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./purchase-order/purchase-order.module').then(m => m.PurchaseOrderModule),
      },
      {
        path: 'payment-invoice',
        data: {
          pageTitle: 'ERP-Payments | PaymentInvoices',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_PREPAYMENTS_MODULE_USER',
            'ROLE_FIXED_ASSETS_USER',
            'ROLE_LEASE_MANAGER'
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./payment-invoice/payment-invoice.module').then(m => m.PaymentInvoiceModule),
      },
      {
        path: 'delivery-note',
        data: {
          pageTitle: 'ERP-Payments | Delivery Notes',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_FIXED_ASSETS_USER',
            'ROLE_LEASE_MANAGER',
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./delivery-note/delivery-note.module').then(m => m.DeliveryNoteModule),
      },
      {
        path: 'business-stamp',
        data: {
          pageTitle: 'ERP-Payments | Business Stamps',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_FIXED_ASSETS_USER'
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./business-stamp/business-stamp.module').then(m => m.BusinessStampModule),
      },
      {
        path: 'credit-note',
        data: {
          pageTitle: 'ERP-Payments | Credit Notes',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_PREPAYMENTS_MODULE_USER',
            'ROLE_FIXED_ASSETS_USER'
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./credit-note/credit-note.module').then(m => m.CreditNoteModule),
      },
      {
        path: 'job-sheet',
        data: {
          pageTitle: 'ERP-Payments | Job Sheet',
          authorities: [
            'ROLE_PAYMENTS_USER',
            'ROLE_PREPAYMENTS_MODULE_USER',
            'ROLE_FIXED_ASSETS_USER'
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./job-sheet/job-sheet.module').then(m => m.JobSheetModule),
      },
      {
        path: 'settlement-requisition',
        data: {
          pageTitle: 'ERP-Payments | Requisition',
          authorities: [
            'ROLE_REQUISITION_MANAGER',
            'ROLE_LEASE_MANAGER'
          ],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./settlement-requisition/settlement-requisition.module')
          .then(m => m.SettlementRequisitionModule),
      },
    ]),
  ],
})
export class ErpSettlementsModule {}
