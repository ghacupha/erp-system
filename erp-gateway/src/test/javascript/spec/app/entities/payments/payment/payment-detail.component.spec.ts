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
import { PaymentDetailComponent } from 'app/entities/payments/payment/payment-detail.component';
import { Payment } from 'app/shared/model/payments/payment.model';

describe('Component Tests', () => {
  describe('Payment Management Detail Component', () => {
    let comp: PaymentDetailComponent;
    let fixture: ComponentFixture<PaymentDetailComponent>;
    const route = ({ data: of({ payment: new Payment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [PaymentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PaymentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load payment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.payment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
