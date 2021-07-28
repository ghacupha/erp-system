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
import { PaymentCalculationDetailComponent } from 'app/entities/payments/payment-calculation/payment-calculation-detail.component';
import { PaymentCalculation } from 'app/shared/model/payments/payment-calculation.model';

describe('Component Tests', () => {
  describe('PaymentCalculation Management Detail Component', () => {
    let comp: PaymentCalculationDetailComponent;
    let fixture: ComponentFixture<PaymentCalculationDetailComponent>;
    const route = ({ data: of({ paymentCalculation: new PaymentCalculation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [PaymentCalculationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PaymentCalculationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentCalculationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentCalculation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentCalculation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
