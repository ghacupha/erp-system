import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SubCountyCodeComponent } from './list/sub-county-code.component';
import { SubCountyCodeDetailComponent } from './detail/sub-county-code-detail.component';
import { SubCountyCodeUpdateComponent } from './update/sub-county-code-update.component';
import { SubCountyCodeDeleteDialogComponent } from './delete/sub-county-code-delete-dialog.component';
import { SubCountyCodeRoutingModule } from './route/sub-county-code-routing.module';

@NgModule({
  imports: [SharedModule, SubCountyCodeRoutingModule],
  declarations: [SubCountyCodeComponent, SubCountyCodeDetailComponent, SubCountyCodeUpdateComponent, SubCountyCodeDeleteDialogComponent],
  entryComponents: [SubCountyCodeDeleteDialogComponent],
})
export class SubCountyCodeModule {}
