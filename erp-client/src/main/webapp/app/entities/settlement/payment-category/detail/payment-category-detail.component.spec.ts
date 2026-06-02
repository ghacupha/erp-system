import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentCategoryDetailComponent } from './payment-category-detail.component';

describe('PaymentCategory Management Detail Component', () => {
  let comp: PaymentCategoryDetailComponent;
  let fixture: ComponentFixture<PaymentCategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentCategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paymentCategory: { id: 123 } }) },
        },
      ],
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
      expect(comp.paymentCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
