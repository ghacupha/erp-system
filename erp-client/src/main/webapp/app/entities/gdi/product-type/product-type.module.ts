import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductTypeComponent } from './list/product-type.component';
import { ProductTypeDetailComponent } from './detail/product-type-detail.component';
import { ProductTypeUpdateComponent } from './update/product-type-update.component';
import { ProductTypeDeleteDialogComponent } from './delete/product-type-delete-dialog.component';
import { ProductTypeRoutingModule } from './route/product-type-routing.module';

@NgModule({
  imports: [SharedModule, ProductTypeRoutingModule],
  declarations: [ProductTypeComponent, ProductTypeDetailComponent, ProductTypeUpdateComponent, ProductTypeDeleteDialogComponent],
  entryComponents: [ProductTypeDeleteDialogComponent],
})
export class ProductTypeModule {}
