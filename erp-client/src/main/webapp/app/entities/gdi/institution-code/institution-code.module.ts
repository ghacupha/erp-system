import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InstitutionCodeComponent } from './list/institution-code.component';
import { InstitutionCodeDetailComponent } from './detail/institution-code-detail.component';
import { InstitutionCodeUpdateComponent } from './update/institution-code-update.component';
import { InstitutionCodeDeleteDialogComponent } from './delete/institution-code-delete-dialog.component';
import { InstitutionCodeRoutingModule } from './route/institution-code-routing.module';

@NgModule({
  imports: [SharedModule, InstitutionCodeRoutingModule],
  declarations: [
    InstitutionCodeComponent,
    InstitutionCodeDetailComponent,
    InstitutionCodeUpdateComponent,
    InstitutionCodeDeleteDialogComponent,
  ],
  entryComponents: [InstitutionCodeDeleteDialogComponent],
})
export class InstitutionCodeModule {}
