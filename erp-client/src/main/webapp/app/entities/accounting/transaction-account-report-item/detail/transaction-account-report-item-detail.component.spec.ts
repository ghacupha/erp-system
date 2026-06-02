import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransactionAccountReportItemDetailComponent } from './transaction-account-report-item-detail.component';

describe('TransactionAccountReportItem Management Detail Component', () => {
  let comp: TransactionAccountReportItemDetailComponent;
  let fixture: ComponentFixture<TransactionAccountReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionAccountReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transactionAccountReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransactionAccountReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransactionAccountReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transactionAccountReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transactionAccountReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
