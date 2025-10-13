///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [RouterModule.forChild([
    {
      path: 'erp/invoice',
      data: { pageTitle: 'ERP | Invoices' },
      loadChildren: () => import('./payments/invoice/invoice.module').then(m => m.ErpServiceInvoiceModule),
    },
    {
      path: 'erp/payment',
      data: { pageTitle: 'ERP | Payments' },
      loadChildren: () => import('./payments/payment/payment.module').then(m => m.ErpServicePaymentModule),
    },
    {
      path: 'erp/payment-calculation',
      data: { pageTitle: 'ERP | PaymentCalculations' },
      loadChildren: () =>
        import('./payments/payment-calculation/payment-calculation.module').then(m => m.ErpServicePaymentCalculationModule),
    },
    {
      path: 'erp/payment-requisition',
      data: { pageTitle: 'ERP | PaymentRequisitions' },
      loadChildren: () =>
        import('./payments/payment-requisition/payment-requisition.module').then(m => m.ErpServicePaymentRequisitionModule),
    },
    {
      path: 'erp/tax-reference',
      data: { pageTitle: 'ERP | Tax Ref' },
      loadChildren: () => import('./payments/tax-reference/tax-reference.module').then(m => m.ErpServiceTaxReferenceModule),
    },
    {
      path: 'erp/tax-rule',
      data: { pageTitle: 'ERP | Tax Rules' },
      loadChildren: () => import('./payments/tax-rule/tax-rule.module').then(m => m.ErpServiceTaxRuleModule),
    },
    {
      path: 'erp/payment-category',
      data: { pageTitle: 'ERP | Payment Categories' },
      loadChildren: () => import('./payments/payment-category/payment-category.module').then(m => m.ErpServicePaymentCategoryModule),
    },
    {
      path: 'erp/payment-label',
      data: {
        pageTitle: 'ERP | Payment Labels',
        authorities: ['ROLE_PAYMENTS_USER', 'ROLE_FIXED_ASSETS_USER'],
      },
      loadChildren: () => import('./payment-label/payment-label.module').then(m => m.PaymentLabelModule),
    },
    {
      path: 'erp/signed-payment',
      data: { pageTitle: 'ERP | Signed Payments' },
      loadChildren: () => import('./signed-payment/signed-payment.module').then(m => m.SignedPaymentModule),
    },
    {
      path: 'erp/dealer',
      data: { pageTitle: 'ERP | Payment Dealers' },
      loadChildren: () => import('./dealers/dealer/dealer.module').then(m => m.ErpServiceDealerModule),
    },
    {
      path: 'placeholder',
      data: { pageTitle: 'ERP | Placeholders' },
      loadChildren: () => import('./placeholder/placeholder.module')
        .then(m => m.ErpServicePlaceholderModule),
    },
    {
      path: 'universally-unique-mapping',
      data: { pageTitle: 'ERP | Universal Mapping' },
      loadChildren: () => import('./universally-unique-mapping/universally-unique-mapping.module')
        .then(m => m.UniversallyUniqueMappingModule),
    },
    {
        path: 'algorithm',
        data: {
          pageTitle: 'ERP | Algorithm',
          authorities: ['ROLE_ADMIN'],
        },
        loadChildren: () => import('./algorithm/algorithm.module')
          .then(m => m.AlgorithmModule),
      },
      {
        path: 'application-user',
        data: {
          pageTitle: 'ERP | Application User Admin',
          authorities: ['ROLE_ADMIN'],
        },
        loadChildren: () => import('./application-user/application-user.module')
          .then(m => m.ApplicationUserModule),
      },
      {
        path: 'process-status',
        data: {
          pageTitle: 'ERP | Process Status',
          authorities: [],
        },
        loadChildren: () => import('./process-status/process-status.module')
          .then(m => m.ProcessStatusModule),
      },
      {
        path: 'security-clearance',
        data: {
          pageTitle: 'ERP | Security Clearance',
          authorities: ['ROLE_ADMIN'],
        },
        loadChildren: () => import('./security-clearance/security-clearance.module')
          .then(m => m.SecurityClearanceModule),
      },
      {
        path: 'system-module',
        data: {
          pageTitle: 'ERP | System Module',
          authorities: ['ROLE_ADMIN'],
        },
        loadChildren: () => import('./system-module/system-module.module')
          .then(m => m.SystemModuleModule),
      },
      {
        path: 'business-document',
        data: {
          pageTitle: 'ERP | Business Document',
          authorities: ['ROLE_PAYMENTS_USER', 'ROLE_LEASE_MANAGER', 'ROLE_FIXED_ASSETS_USER', 'ROLE_PREPAYMENTS_MODULE_USER'],
        },
        loadChildren: () => import('./business-document/business-document.module')
          .then(m => m.BusinessDocumentModule),
      },
      {
        path: 'contract-metadata',
        data: {
          pageTitle: 'ERP | Contracts',
          authorities: ['ROLE_CONTRACT_MANAGER'],
        },
        loadChildren: () => import('./contract-metadata/contract-metadata.module')
          .then(m => m.ContractMetadataModule),
      },
      {
        path: 'fiscal-year',
        data: {
          pageTitle: 'ERP | Reports',
          authorities: [],
        },
        loadChildren: () => import('./fiscal-year/fiscal-year.module')
          .then(m => m.FiscalYearModule),
      },
      {
        path: 'fiscal-month',
        data: {
          pageTitle: 'ERP | Reports',
          authorities: [],
        },
        loadChildren: () => import('./fiscal-month/fiscal-month.module')
          .then(m => m.FiscalMonthModule),
      },
      {
        path: 'fiscal-quarter',
        data: {
          pageTitle: 'ERP | Reports',
          authorities: [],
        },
        loadChildren: () => import('./fiscal-quarter/fiscal-quarter.module')
          .then(m => m.FiscalQuarterModule),
      },
  ])]
})
export class ErpPagesModule {}
