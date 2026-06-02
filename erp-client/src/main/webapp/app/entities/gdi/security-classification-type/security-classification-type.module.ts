import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SecurityClassificationTypeComponent } from './list/security-classification-type.component';
import { SecurityClassificationTypeDetailComponent } from './detail/security-classification-type-detail.component';
import { SecurityClassificationTypeUpdateComponent } from './update/security-classification-type-update.component';
import { SecurityClassificationTypeDeleteDialogComponent } from './delete/security-classification-type-delete-dialog.component';
import { SecurityClassificationTypeRoutingModule } from './route/security-classification-type-routing.module';

@NgModule({
  imports: [SharedModule, SecurityClassificationTypeRoutingModule],
  declarations: [
    SecurityClassificationTypeComponent,
    SecurityClassificationTypeDetailComponent,
    SecurityClassificationTypeUpdateComponent,
    SecurityClassificationTypeDeleteDialogComponent,
  ],
  entryComponents: [SecurityClassificationTypeDeleteDialogComponent],
})
export class SecurityClassificationTypeModule {}
