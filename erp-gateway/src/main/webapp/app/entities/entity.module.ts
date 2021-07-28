///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'file-type',
        loadChildren: () => import('./files/file-type/file-type.module').then(m => m.ErpServiceFileTypeModule),
      },
      {
        path: 'file-upload',
        loadChildren: () => import('./files/file-upload/file-upload.module').then(m => m.ErpServiceFileUploadModule),
      },
      {
        path: 'fixed-asset-acquisition',
        loadChildren: () =>
          import('./assets/fixed-asset-acquisition/fixed-asset-acquisition.module').then(m => m.ErpServiceFixedAssetAcquisitionModule),
      },
      {
        path: 'fixed-asset-depreciation',
        loadChildren: () =>
          import('./assets/fixed-asset-depreciation/fixed-asset-depreciation.module').then(m => m.ErpServiceFixedAssetDepreciationModule),
      },
      {
        path: 'fixed-asset-net-book-value',
        loadChildren: () =>
          import('./assets/fixed-asset-net-book-value/fixed-asset-net-book-value.module').then(
            m => m.ErpServiceFixedAssetNetBookValueModule
          ),
      },
      {
        path: 'message-token',
        loadChildren: () => import('./files/message-token/message-token.module').then(m => m.ErpServiceMessageTokenModule),
      },
      {
        path: 'payment-calculation',
        loadChildren: () =>
          import('./payments/payment-calculation/payment-calculation.module').then(m => m.ErpServicePaymentCalculationModule),
      },
      {
        path: 'payment-category',
        loadChildren: () => import('./payments/payment-category/payment-category.module').then(m => m.ErpServicePaymentCategoryModule),
      },
      {
        path: 'payment-requisition',
        loadChildren: () =>
          import('./payments/payment-requisition/payment-requisition.module').then(m => m.ErpServicePaymentRequisitionModule),
      },
      {
        path: 'tax-reference',
        loadChildren: () => import('./payments/tax-reference/tax-reference.module').then(m => m.ErpServiceTaxReferenceModule),
      },
      {
        path: 'tax-rule',
        loadChildren: () => import('./payments/tax-rule/tax-rule.module').then(m => m.ErpServiceTaxRuleModule),
      },
      {
        path: 'dealer',
        loadChildren: () => import('./dealers/dealer/dealer.module').then(m => m.ErpServiceDealerModule),
      },
      {
        path: 'payment',
        loadChildren: () => import('./payments/payment/payment.module').then(m => m.ErpServicePaymentModule),
      },
      {
        path: 'invoice',
        loadChildren: () => import('./payments/invoice/invoice.module').then(m => m.ErpServiceInvoiceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class ErpGatewayEntityModule {}
