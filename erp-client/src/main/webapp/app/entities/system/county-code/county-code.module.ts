import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountyCodeComponent } from './list/county-code.component';
import { CountyCodeDetailComponent } from './detail/county-code-detail.component';
import { CountyCodeUpdateComponent } from './update/county-code-update.component';
import { CountyCodeDeleteDialogComponent } from './delete/county-code-delete-dialog.component';
import { CountyCodeRoutingModule } from './route/county-code-routing.module';

@NgModule({
  imports: [SharedModule, CountyCodeRoutingModule],
  declarations: [CountyCodeComponent, CountyCodeDetailComponent, CountyCodeUpdateComponent, CountyCodeDeleteDialogComponent],
  entryComponents: [CountyCodeDeleteDialogComponent],
})
export class CountyCodeModule {}
