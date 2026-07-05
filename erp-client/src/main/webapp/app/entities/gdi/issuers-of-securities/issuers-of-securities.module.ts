import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IssuersOfSecuritiesComponent } from './list/issuers-of-securities.component';
import { IssuersOfSecuritiesDetailComponent } from './detail/issuers-of-securities-detail.component';
import { IssuersOfSecuritiesUpdateComponent } from './update/issuers-of-securities-update.component';
import { IssuersOfSecuritiesDeleteDialogComponent } from './delete/issuers-of-securities-delete-dialog.component';
import { IssuersOfSecuritiesRoutingModule } from './route/issuers-of-securities-routing.module';

@NgModule({
  imports: [SharedModule, IssuersOfSecuritiesRoutingModule],
  declarations: [
    IssuersOfSecuritiesComponent,
    IssuersOfSecuritiesDetailComponent,
    IssuersOfSecuritiesUpdateComponent,
    IssuersOfSecuritiesDeleteDialogComponent,
  ],
  entryComponents: [IssuersOfSecuritiesDeleteDialogComponent],
})
export class IssuersOfSecuritiesModule {}
