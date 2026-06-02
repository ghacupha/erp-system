import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InstitutionContactDetailsComponent } from './list/institution-contact-details.component';
import { InstitutionContactDetailsDetailComponent } from './detail/institution-contact-details-detail.component';
import { InstitutionContactDetailsUpdateComponent } from './update/institution-contact-details-update.component';
import { InstitutionContactDetailsDeleteDialogComponent } from './delete/institution-contact-details-delete-dialog.component';
import { InstitutionContactDetailsRoutingModule } from './route/institution-contact-details-routing.module';

@NgModule({
  imports: [SharedModule, InstitutionContactDetailsRoutingModule],
  declarations: [
    InstitutionContactDetailsComponent,
    InstitutionContactDetailsDetailComponent,
    InstitutionContactDetailsUpdateComponent,
    InstitutionContactDetailsDeleteDialogComponent,
  ],
  entryComponents: [InstitutionContactDetailsDeleteDialogComponent],
})
export class InstitutionContactDetailsModule {}
