import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseLiabilityReportItemDetailComponent } from './lease-liability-report-item-detail.component';

describe('LeaseLiabilityReportItem Management Detail Component', () => {
  let comp: LeaseLiabilityReportItemDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiabilityReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiabilityReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiabilityReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
