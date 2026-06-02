import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CounterPartyCategoryComponent } from './list/counter-party-category.component';
import { CounterPartyCategoryDetailComponent } from './detail/counter-party-category-detail.component';
import { CounterPartyCategoryUpdateComponent } from './update/counter-party-category-update.component';
import { CounterPartyCategoryDeleteDialogComponent } from './delete/counter-party-category-delete-dialog.component';
import { CounterPartyCategoryRoutingModule } from './route/counter-party-category-routing.module';

@NgModule({
  imports: [SharedModule, CounterPartyCategoryRoutingModule],
  declarations: [
    CounterPartyCategoryComponent,
    CounterPartyCategoryDetailComponent,
    CounterPartyCategoryUpdateComponent,
    CounterPartyCategoryDeleteDialogComponent,
  ],
  entryComponents: [CounterPartyCategoryDeleteDialogComponent],
})
export class CounterPartyCategoryModule {}
