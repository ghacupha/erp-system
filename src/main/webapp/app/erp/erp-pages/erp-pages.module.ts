import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";

@NgModule({
  imports: [RouterModule.forChild([
    {
      path: 'file-type',
      data: { pageTitle: 'FileTypes' },
      loadChildren: () => import('./files/file-type/file-type.module').then(m => m.ErpServiceFileTypeModule),
    },
    {
      path: 'file-upload',
      data: { pageTitle: 'FileUploads' },
      loadChildren: () => import('./files/file-upload/file-upload.module').then(m => m.ErpServiceFileUploadModule),
    },
    {
      path: 'message-token',
      data: { pageTitle: 'MessageTokens' },
      loadChildren: () => import('./files/message-token/message-token.module').then(m => m.ErpServiceMessageTokenModule),
    },
    {
      path: 'invoice',
      data: { pageTitle: 'Invoices' },
      loadChildren: () => import('./payments/invoice/invoice.module').then(m => m.ErpServiceInvoiceModule),
    },
    {
      path: 'payment',
      data: { pageTitle: 'Payments' },
      loadChildren: () => import('./payments/payment/payment.module').then(m => m.ErpServicePaymentModule),
    },
    {
      path: 'payment-calculation',
      data: { pageTitle: 'PaymentCalculations' },
      loadChildren: () =>
        import('./payments/payment-calculation/payment-calculation.module').then(m => m.ErpServicePaymentCalculationModule),
    },
    {
      path: 'payment-requisition',
      data: { pageTitle: 'PaymentRequisitions' },
      loadChildren: () =>
        import('./payments/payment-requisition/payment-requisition.module').then(m => m.ErpServicePaymentRequisitionModule),
    },
    {
      path: 'tax-reference',
      data: { pageTitle: 'TaxReferences' },
      loadChildren: () => import('./payments/tax-reference/tax-reference.module').then(m => m.ErpServiceTaxReferenceModule),
    },
    {
      path: 'tax-rule',
      data: { pageTitle: 'TaxRules' },
      loadChildren: () => import('./payments/tax-rule/tax-rule.module').then(m => m.ErpServiceTaxRuleModule),
    },
    {
      path: 'payment-category',
      data: { pageTitle: 'PaymentCategories' },
      loadChildren: () => import('./payments/payment-category/payment-category.module').then(m => m.ErpServicePaymentCategoryModule),
    },
    {
      path: 'payment-label',
      data: { pageTitle: 'PaymentLabels' },
      loadChildren: () => import('./payment-label/payment-label.module').then(m => m.PaymentLabelModule),
    },
    {
      path: 'signed-payment',
      data: { pageTitle: 'SignedPayments' },
      loadChildren: () => import('./signed-payment/signed-payment.module').then(m => m.SignedPaymentModule),
    },
  ])]
})
export class ErpPagesModule {}
