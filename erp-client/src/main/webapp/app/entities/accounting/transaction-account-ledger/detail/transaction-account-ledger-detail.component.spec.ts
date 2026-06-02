import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransactionAccountLedgerDetailComponent } from './transaction-account-ledger-detail.component';

describe('TransactionAccountLedger Management Detail Component', () => {
  let comp: TransactionAccountLedgerDetailComponent;
  let fixture: ComponentFixture<TransactionAccountLedgerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionAccountLedgerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transactionAccountLedger: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransactionAccountLedgerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransactionAccountLedgerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transactionAccountLedger on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transactionAccountLedger).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
