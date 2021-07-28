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
