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
import { PaymentCategoryDetailComponent } from 'app/entities/payments/payment-category/payment-category-detail.component';
import { PaymentCategory } from 'app/shared/model/payments/payment-category.model';

describe('Component Tests', () => {
  describe('PaymentCategory Management Detail Component', () => {
    let comp: PaymentCategoryDetailComponent;
    let fixture: ComponentFixture<PaymentCategoryDetailComponent>;
    const route = ({ data: of({ paymentCategory: new PaymentCategory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [PaymentCategoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PaymentCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
