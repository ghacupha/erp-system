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
