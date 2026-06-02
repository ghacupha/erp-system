import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseTemplateComponent } from './list/lease-template.component';
import { LeaseTemplateDetailComponent } from './detail/lease-template-detail.component';
import { LeaseTemplateUpdateComponent } from './update/lease-template-update.component';
import { LeaseTemplateDeleteDialogComponent } from './delete/lease-template-delete-dialog.component';
import { LeaseTemplateRoutingModule } from './route/lease-template-routing.module';

@NgModule({
  imports: [SharedModule, LeaseTemplateRoutingModule],
  declarations: [LeaseTemplateComponent, LeaseTemplateDetailComponent, LeaseTemplateUpdateComponent, LeaseTemplateDeleteDialogComponent],
  entryComponents: [LeaseTemplateDeleteDialogComponent],
})
export class LeaseTemplateModule {}
