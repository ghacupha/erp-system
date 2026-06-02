import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AlgorithmComponent } from './list/algorithm.component';
import { AlgorithmDetailComponent } from './detail/algorithm-detail.component';
import { AlgorithmUpdateComponent } from './update/algorithm-update.component';
import { AlgorithmDeleteDialogComponent } from './delete/algorithm-delete-dialog.component';
import { AlgorithmRoutingModule } from './route/algorithm-routing.module';

@NgModule({
  imports: [SharedModule, AlgorithmRoutingModule],
  declarations: [AlgorithmComponent, AlgorithmDetailComponent, AlgorithmUpdateComponent, AlgorithmDeleteDialogComponent],
  entryComponents: [AlgorithmDeleteDialogComponent],
})
export class AlgorithmModule {}
