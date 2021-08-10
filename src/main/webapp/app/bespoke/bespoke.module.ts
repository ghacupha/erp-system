import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AboutModule } from 'app/bespoke/about/about.module';
import {PaymentsMaintenanceModule} from "app/bespoke/payments/payments-maintenance.module";
import { SharedModule } from '../shared/shared.module';

const routes: Routes = [
  {
    path: 'about',
    loadChildren: () => import('./about/about.module').then(m => m.AboutModule),
  },
  {
    path: '',
    loadChildren: () => import('./payments/payments-maintenance.module').then(m => m.PaymentsMaintenanceModule),
  },
];

@NgModule({
  imports: [CommonModule, SharedModule, RouterModule.forChild(routes), AboutModule, PaymentsMaintenanceModule],
  exports: [AboutModule, PaymentsMaintenanceModule],
})
export class BespokeModule {}
