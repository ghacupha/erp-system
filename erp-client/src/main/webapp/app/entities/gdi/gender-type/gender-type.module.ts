import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GenderTypeComponent } from './list/gender-type.component';
import { GenderTypeDetailComponent } from './detail/gender-type-detail.component';
import { GenderTypeUpdateComponent } from './update/gender-type-update.component';
import { GenderTypeDeleteDialogComponent } from './delete/gender-type-delete-dialog.component';
import { GenderTypeRoutingModule } from './route/gender-type-routing.module';

@NgModule({
  imports: [SharedModule, GenderTypeRoutingModule],
  declarations: [GenderTypeComponent, GenderTypeDetailComponent, GenderTypeUpdateComponent, GenderTypeDeleteDialogComponent],
  entryComponents: [GenderTypeDeleteDialogComponent],
})
export class GenderTypeModule {}
