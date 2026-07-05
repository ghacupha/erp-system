import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategoryOfSecurityComponent } from './list/category-of-security.component';
import { CategoryOfSecurityDetailComponent } from './detail/category-of-security-detail.component';
import { CategoryOfSecurityUpdateComponent } from './update/category-of-security-update.component';
import { CategoryOfSecurityDeleteDialogComponent } from './delete/category-of-security-delete-dialog.component';
import { CategoryOfSecurityRoutingModule } from './route/category-of-security-routing.module';

@NgModule({
  imports: [SharedModule, CategoryOfSecurityRoutingModule],
  declarations: [
    CategoryOfSecurityComponent,
    CategoryOfSecurityDetailComponent,
    CategoryOfSecurityUpdateComponent,
    CategoryOfSecurityDeleteDialogComponent,
  ],
  entryComponents: [CategoryOfSecurityDeleteDialogComponent],
})
export class CategoryOfSecurityModule {}
