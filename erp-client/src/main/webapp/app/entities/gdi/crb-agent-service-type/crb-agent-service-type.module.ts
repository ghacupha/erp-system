import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbAgentServiceTypeComponent } from './list/crb-agent-service-type.component';
import { CrbAgentServiceTypeDetailComponent } from './detail/crb-agent-service-type-detail.component';
import { CrbAgentServiceTypeUpdateComponent } from './update/crb-agent-service-type-update.component';
import { CrbAgentServiceTypeDeleteDialogComponent } from './delete/crb-agent-service-type-delete-dialog.component';
import { CrbAgentServiceTypeRoutingModule } from './route/crb-agent-service-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbAgentServiceTypeRoutingModule],
  declarations: [
    CrbAgentServiceTypeComponent,
    CrbAgentServiceTypeDetailComponent,
    CrbAgentServiceTypeUpdateComponent,
    CrbAgentServiceTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbAgentServiceTypeDeleteDialogComponent],
})
export class CrbAgentServiceTypeModule {}
