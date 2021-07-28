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
