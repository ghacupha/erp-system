import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeasePaymentDetailComponent } from './lease-payment-detail.component';

describe('LeasePayment Management Detail Component', () => {
  let comp: LeasePaymentDetailComponent;
  let fixture: ComponentFixture<LeasePaymentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeasePaymentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leasePayment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeasePaymentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeasePaymentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leasePayment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leasePayment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
