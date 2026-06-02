import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SettlementRequisitionDetailComponent } from './settlement-requisition-detail.component';

describe('SettlementRequisition Management Detail Component', () => {
  let comp: SettlementRequisitionDetailComponent;
  let fixture: ComponentFixture<SettlementRequisitionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SettlementRequisitionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ settlementRequisition: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SettlementRequisitionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SettlementRequisitionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load settlementRequisition on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.settlementRequisition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
