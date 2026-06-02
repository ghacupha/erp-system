import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseLiabilityByAccountReportItemDetailComponent } from './lease-liability-by-account-report-item-detail.component';

describe('LeaseLiabilityByAccountReportItem Management Detail Component', () => {
  let comp: LeaseLiabilityByAccountReportItemDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityByAccountReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityByAccountReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiabilityByAccountReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityByAccountReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityByAccountReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiabilityByAccountReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiabilityByAccountReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
