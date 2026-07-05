import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SystemModuleComponent } from './list/system-module.component';
import { SystemModuleDetailComponent } from './detail/system-module-detail.component';
import { SystemModuleUpdateComponent } from './update/system-module-update.component';
import { SystemModuleDeleteDialogComponent } from './delete/system-module-delete-dialog.component';
import { SystemModuleRoutingModule } from './route/system-module-routing.module';

@NgModule({
  imports: [SharedModule, SystemModuleRoutingModule],
  declarations: [SystemModuleComponent, SystemModuleDetailComponent, SystemModuleUpdateComponent, SystemModuleDeleteDialogComponent],
  entryComponents: [SystemModuleDeleteDialogComponent],
})
export class SystemModuleModule {}
