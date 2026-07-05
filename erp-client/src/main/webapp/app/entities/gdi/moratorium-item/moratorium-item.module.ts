import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MoratoriumItemComponent } from './list/moratorium-item.component';
import { MoratoriumItemDetailComponent } from './detail/moratorium-item-detail.component';
import { MoratoriumItemUpdateComponent } from './update/moratorium-item-update.component';
import { MoratoriumItemDeleteDialogComponent } from './delete/moratorium-item-delete-dialog.component';
import { MoratoriumItemRoutingModule } from './route/moratorium-item-routing.module';

@NgModule({
  imports: [SharedModule, MoratoriumItemRoutingModule],
  declarations: [
    MoratoriumItemComponent,
    MoratoriumItemDetailComponent,
    MoratoriumItemUpdateComponent,
    MoratoriumItemDeleteDialogComponent,
  ],
  entryComponents: [MoratoriumItemDeleteDialogComponent],
})
export class MoratoriumItemModule {}
