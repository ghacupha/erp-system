import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LiabilityEnumerationComponent } from './list/liability-enumeration.component';
import { LiabilityEnumerationUpdateComponent } from './update/liability-enumeration-update.component';
import { LiabilityEnumerationRoutingModule } from './route/liability-enumeration-routing.module';
import { PresentValueEnumerationComponent } from './present-values/present-value-enumeration.component';

@NgModule({
  imports: [SharedModule, LiabilityEnumerationRoutingModule],
  declarations: [LiabilityEnumerationComponent, LiabilityEnumerationUpdateComponent, PresentValueEnumerationComponent],
})
export class LiabilityEnumerationModule {}
