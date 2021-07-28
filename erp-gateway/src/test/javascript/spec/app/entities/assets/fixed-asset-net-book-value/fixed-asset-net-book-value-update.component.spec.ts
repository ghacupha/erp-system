///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetNetBookValueUpdateComponent } from 'app/entities/assets/fixed-asset-net-book-value/fixed-asset-net-book-value-update.component';
import { FixedAssetNetBookValueService } from 'app/entities/assets/fixed-asset-net-book-value/fixed-asset-net-book-value.service';
import { FixedAssetNetBookValue } from 'app/shared/model/assets/fixed-asset-net-book-value.model';

describe('Component Tests', () => {
  describe('FixedAssetNetBookValue Management Update Component', () => {
    let comp: FixedAssetNetBookValueUpdateComponent;
    let fixture: ComponentFixture<FixedAssetNetBookValueUpdateComponent>;
    let service: FixedAssetNetBookValueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetNetBookValueUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FixedAssetNetBookValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixedAssetNetBookValueUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FixedAssetNetBookValueService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FixedAssetNetBookValue(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FixedAssetNetBookValue();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
