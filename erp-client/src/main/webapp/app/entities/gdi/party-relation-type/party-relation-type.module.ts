import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PartyRelationTypeComponent } from './list/party-relation-type.component';
import { PartyRelationTypeDetailComponent } from './detail/party-relation-type-detail.component';
import { PartyRelationTypeUpdateComponent } from './update/party-relation-type-update.component';
import { PartyRelationTypeDeleteDialogComponent } from './delete/party-relation-type-delete-dialog.component';
import { PartyRelationTypeRoutingModule } from './route/party-relation-type-routing.module';

@NgModule({
  imports: [SharedModule, PartyRelationTypeRoutingModule],
  declarations: [
    PartyRelationTypeComponent,
    PartyRelationTypeDetailComponent,
    PartyRelationTypeUpdateComponent,
    PartyRelationTypeDeleteDialogComponent,
  ],
  entryComponents: [PartyRelationTypeDeleteDialogComponent],
})
export class PartyRelationTypeModule {}
