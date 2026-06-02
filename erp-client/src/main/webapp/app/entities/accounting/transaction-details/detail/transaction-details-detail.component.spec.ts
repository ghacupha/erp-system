import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransactionDetailsDetailComponent } from './transaction-details-detail.component';

describe('TransactionDetails Management Detail Component', () => {
  let comp: TransactionDetailsDetailComponent;
  let fixture: ComponentFixture<TransactionDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transactionDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransactionDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransactionDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transactionDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transactionDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
