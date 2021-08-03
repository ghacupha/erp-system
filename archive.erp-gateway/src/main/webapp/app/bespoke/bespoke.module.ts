import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { RouterModule, Routes } from '@angular/router';
import { AboutModule } from 'app/bespoke/about/about.module';
import {PaymentsMaintenanceModule} from "app/bespoke/payments/payments-maintenance.module";

const routes: Routes = [
  {
    path: 'about/finance-erp',
    loadChildren: () => import('./about/about.module').then(m => m.AboutModule),
  },
  {
    path: '',
    loadChildren: () => import('./payments/payments-maintenance.module').then(m => m.PaymentsMaintenanceModule),
  },
];

@NgModule({
  imports: [CommonModule, ErpGatewaySharedModule, RouterModule.forChild(routes), AboutModule, PaymentsMaintenanceModule],
  exports: [AboutModule, PaymentsMaintenanceModule],
})
export class BespokeModule {}
