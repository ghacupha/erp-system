import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NetBookValueEntryComponent } from './list/net-book-value-entry.component';
import { NetBookValueEntryDetailComponent } from './detail/net-book-value-entry-detail.component';
import { NetBookValueEntryUpdateComponent } from './update/net-book-value-entry-update.component';
import { NetBookValueEntryDeleteDialogComponent } from './delete/net-book-value-entry-delete-dialog.component';
import { NetBookValueEntryRoutingModule } from './route/net-book-value-entry-routing.module';

@NgModule({
  imports: [SharedModule, NetBookValueEntryRoutingModule],
  declarations: [
    NetBookValueEntryComponent,
    NetBookValueEntryDetailComponent,
    NetBookValueEntryUpdateComponent,
    NetBookValueEntryDeleteDialogComponent,
  ],
  entryComponents: [NetBookValueEntryDeleteDialogComponent],
})
export class NetBookValueEntryModule {}
