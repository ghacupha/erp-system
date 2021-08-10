import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AboutModule } from 'app/bespoke/about/about.module';
import { SharedModule } from '../shared/shared.module';
import {BespokeNavigationModule} from "./navigation/bespoke-navigation.module";

const routes: Routes = [
  {
    path: 'about',
    loadChildren: () => import('./about/about.module').then(m => m.AboutModule),
  },
];

@NgModule({
  imports: [CommonModule, SharedModule, RouterModule.forChild(routes), AboutModule,BespokeNavigationModule],
  exports: [AboutModule, BespokeNavigationModule],
})
export class BespokeModule {}
