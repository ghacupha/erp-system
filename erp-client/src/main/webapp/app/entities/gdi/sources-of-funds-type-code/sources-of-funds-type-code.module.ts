import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SourcesOfFundsTypeCodeComponent } from './list/sources-of-funds-type-code.component';
import { SourcesOfFundsTypeCodeDetailComponent } from './detail/sources-of-funds-type-code-detail.component';
import { SourcesOfFundsTypeCodeUpdateComponent } from './update/sources-of-funds-type-code-update.component';
import { SourcesOfFundsTypeCodeDeleteDialogComponent } from './delete/sources-of-funds-type-code-delete-dialog.component';
import { SourcesOfFundsTypeCodeRoutingModule } from './route/sources-of-funds-type-code-routing.module';

@NgModule({
  imports: [SharedModule, SourcesOfFundsTypeCodeRoutingModule],
  declarations: [
    SourcesOfFundsTypeCodeComponent,
    SourcesOfFundsTypeCodeDetailComponent,
    SourcesOfFundsTypeCodeUpdateComponent,
    SourcesOfFundsTypeCodeDeleteDialogComponent,
  ],
  entryComponents: [SourcesOfFundsTypeCodeDeleteDialogComponent],
})
export class SourcesOfFundsTypeCodeModule {}
