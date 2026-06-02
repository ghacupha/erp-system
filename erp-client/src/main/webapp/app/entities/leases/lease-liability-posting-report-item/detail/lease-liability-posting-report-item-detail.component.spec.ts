import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseLiabilityPostingReportItemDetailComponent } from './lease-liability-posting-report-item-detail.component';

describe('LeaseLiabilityPostingReportItem Management Detail Component', () => {
  let comp: LeaseLiabilityPostingReportItemDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityPostingReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityPostingReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiabilityPostingReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityPostingReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityPostingReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiabilityPostingReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiabilityPostingReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
