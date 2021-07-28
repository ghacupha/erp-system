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
import { PaymentRequisitionDetailComponent } from 'app/entities/payments/payment-requisition/payment-requisition-detail.component';
import { PaymentRequisition } from 'app/shared/model/payments/payment-requisition.model';

describe('Component Tests', () => {
  describe('PaymentRequisition Management Detail Component', () => {
    let comp: PaymentRequisitionDetailComponent;
    let fixture: ComponentFixture<PaymentRequisitionDetailComponent>;
    const route = ({ data: of({ paymentRequisition: new PaymentRequisition(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [PaymentRequisitionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PaymentRequisitionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentRequisitionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentRequisition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentRequisition).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
