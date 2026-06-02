import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SecurityTypeComponent } from './list/security-type.component';
import { SecurityTypeDetailComponent } from './detail/security-type-detail.component';
import { SecurityTypeUpdateComponent } from './update/security-type-update.component';
import { SecurityTypeDeleteDialogComponent } from './delete/security-type-delete-dialog.component';
import { SecurityTypeRoutingModule } from './route/security-type-routing.module';

@NgModule({
  imports: [SharedModule, SecurityTypeRoutingModule],
  declarations: [SecurityTypeComponent, SecurityTypeDetailComponent, SecurityTypeUpdateComponent, SecurityTypeDeleteDialogComponent],
  entryComponents: [SecurityTypeDeleteDialogComponent],
})
export class SecurityTypeModule {}
