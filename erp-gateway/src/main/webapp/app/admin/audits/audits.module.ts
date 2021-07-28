import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ErpGatewaySharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent],
})
export class AuditsModule {}
