import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountySubCountyCodeComponent } from './list/county-sub-county-code.component';
import { CountySubCountyCodeDetailComponent } from './detail/county-sub-county-code-detail.component';
import { CountySubCountyCodeUpdateComponent } from './update/county-sub-county-code-update.component';
import { CountySubCountyCodeDeleteDialogComponent } from './delete/county-sub-county-code-delete-dialog.component';
import { CountySubCountyCodeRoutingModule } from './route/county-sub-county-code-routing.module';

@NgModule({
  imports: [SharedModule, CountySubCountyCodeRoutingModule],
  declarations: [
    CountySubCountyCodeComponent,
    CountySubCountyCodeDetailComponent,
    CountySubCountyCodeUpdateComponent,
    CountySubCountyCodeDeleteDialogComponent,
  ],
  entryComponents: [CountySubCountyCodeDeleteDialogComponent],
})
export class CountySubCountyCodeModule {}
