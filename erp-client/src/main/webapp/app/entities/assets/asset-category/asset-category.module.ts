import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetCategoryComponent } from './list/asset-category.component';
import { AssetCategoryDetailComponent } from './detail/asset-category-detail.component';
import { AssetCategoryUpdateComponent } from './update/asset-category-update.component';
import { AssetCategoryDeleteDialogComponent } from './delete/asset-category-delete-dialog.component';
import { AssetCategoryRoutingModule } from './route/asset-category-routing.module';

@NgModule({
  imports: [SharedModule, AssetCategoryRoutingModule],
  declarations: [AssetCategoryComponent, AssetCategoryDetailComponent, AssetCategoryUpdateComponent, AssetCategoryDeleteDialogComponent],
  entryComponents: [AssetCategoryDeleteDialogComponent],
})
export class AssetCategoryModule {}
