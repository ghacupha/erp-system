import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GdiMasterDataIndexComponent } from './list/gdi-master-data-index.component';
import { GdiMasterDataIndexDetailComponent } from './detail/gdi-master-data-index-detail.component';
import { GdiMasterDataIndexUpdateComponent } from './update/gdi-master-data-index-update.component';
import { GdiMasterDataIndexDeleteDialogComponent } from './delete/gdi-master-data-index-delete-dialog.component';
import { GdiMasterDataIndexRoutingModule } from './route/gdi-master-data-index-routing.module';

@NgModule({
  imports: [SharedModule, GdiMasterDataIndexRoutingModule],
  declarations: [
    GdiMasterDataIndexComponent,
    GdiMasterDataIndexDetailComponent,
    GdiMasterDataIndexUpdateComponent,
    GdiMasterDataIndexDeleteDialogComponent,
  ],
  entryComponents: [GdiMasterDataIndexDeleteDialogComponent],
})
export class GdiMasterDataIndexModule {}
