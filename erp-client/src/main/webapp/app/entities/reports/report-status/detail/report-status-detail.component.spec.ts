import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReportStatusDetailComponent } from './report-status-detail.component';

describe('ReportStatus Management Detail Component', () => {
  let comp: ReportStatusDetailComponent;
  let fixture: ComponentFixture<ReportStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReportStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ reportStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReportStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reportStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.reportStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
