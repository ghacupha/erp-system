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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { TaxRuleDetailComponent } from 'app/entities/payments/tax-rule/tax-rule-detail.component';
import { TaxRule } from 'app/shared/model/payments/tax-rule.model';

describe('Component Tests', () => {
  describe('TaxRule Management Detail Component', () => {
    let comp: TaxRuleDetailComponent;
    let fixture: ComponentFixture<TaxRuleDetailComponent>;
    const route = ({ data: of({ taxRule: new TaxRule(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [TaxRuleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TaxRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaxRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load taxRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taxRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
