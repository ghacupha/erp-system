import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetRevaluationComponent } from './list/asset-revaluation.component';
import { AssetRevaluationDetailComponent } from './detail/asset-revaluation-detail.component';
import { AssetRevaluationUpdateComponent } from './update/asset-revaluation-update.component';
import { AssetRevaluationDeleteDialogComponent } from './delete/asset-revaluation-delete-dialog.component';
import { AssetRevaluationRoutingModule } from './route/asset-revaluation-routing.module';

@NgModule({
  imports: [SharedModule, AssetRevaluationRoutingModule],
  declarations: [
    AssetRevaluationComponent,
    AssetRevaluationDetailComponent,
    AssetRevaluationUpdateComponent,
    AssetRevaluationDeleteDialogComponent,
  ],
  entryComponents: [AssetRevaluationDeleteDialogComponent],
})
export class AssetRevaluationModule {}
