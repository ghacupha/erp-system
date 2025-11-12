///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RelatedPartyRelationshipComponent } from './list/related-party-relationship.component';
import { RelatedPartyRelationshipDetailComponent } from './detail/related-party-relationship-detail.component';
import { RelatedPartyRelationshipUpdateComponent } from './update/related-party-relationship-update.component';
import { RelatedPartyRelationshipDeleteDialogComponent } from './delete/related-party-relationship-delete-dialog.component';
import { RelatedPartyRelationshipRoutingModule } from './route/related-party-relationship-routing.module';

@NgModule({
  imports: [SharedModule, RelatedPartyRelationshipRoutingModule],
  declarations: [
    RelatedPartyRelationshipComponent,
    RelatedPartyRelationshipDetailComponent,
    RelatedPartyRelationshipUpdateComponent,
    RelatedPartyRelationshipDeleteDialogComponent,
  ],
  entryComponents: [RelatedPartyRelationshipDeleteDialogComponent],
})
export class RelatedPartyRelationshipModule {}
