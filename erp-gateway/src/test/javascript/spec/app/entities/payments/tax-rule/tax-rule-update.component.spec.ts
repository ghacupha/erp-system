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
import { TaxRuleUpdateComponent } from 'app/entities/payments/tax-rule/tax-rule-update.component';
import { TaxRuleService } from 'app/entities/payments/tax-rule/tax-rule.service';
import { TaxRule } from 'app/shared/model/payments/tax-rule.model';

describe('Component Tests', () => {
  describe('TaxRule Management Update Component', () => {
    let comp: TaxRuleUpdateComponent;
    let fixture: ComponentFixture<TaxRuleUpdateComponent>;
    let service: TaxRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [TaxRuleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TaxRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaxRuleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaxRuleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaxRule(123);
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
        const entity = new TaxRule();
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
