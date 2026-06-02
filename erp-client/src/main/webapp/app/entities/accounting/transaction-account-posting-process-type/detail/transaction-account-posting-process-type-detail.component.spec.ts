import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransactionAccountPostingProcessTypeDetailComponent } from './transaction-account-posting-process-type-detail.component';

describe('TransactionAccountPostingProcessType Management Detail Component', () => {
  let comp: TransactionAccountPostingProcessTypeDetailComponent;
  let fixture: ComponentFixture<TransactionAccountPostingProcessTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionAccountPostingProcessTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transactionAccountPostingProcessType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransactionAccountPostingProcessTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransactionAccountPostingProcessTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transactionAccountPostingProcessType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transactionAccountPostingProcessType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
