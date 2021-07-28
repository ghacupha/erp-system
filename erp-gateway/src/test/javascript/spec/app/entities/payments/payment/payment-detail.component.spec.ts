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
