import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlaceholderComponent } from './list/placeholder.component';
import { PlaceholderDetailComponent } from './detail/placeholder-detail.component';
import { PlaceholderUpdateComponent } from './update/placeholder-update.component';
import { PlaceholderDeleteDialogComponent } from './delete/placeholder-delete-dialog.component';
import { PlaceholderRoutingModule } from './route/placeholder-routing.module';

@NgModule({
  imports: [SharedModule, PlaceholderRoutingModule],
  declarations: [PlaceholderComponent, PlaceholderDetailComponent, PlaceholderUpdateComponent, PlaceholderDeleteDialogComponent],
  entryComponents: [PlaceholderDeleteDialogComponent],
})
export class ErpServicePlaceholderModule {}
