import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReportContentTypeDetailComponent } from './report-content-type-detail.component';

describe('ReportContentType Management Detail Component', () => {
  let comp: ReportContentTypeDetailComponent;
  let fixture: ComponentFixture<ReportContentTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReportContentTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ reportContentType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReportContentTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportContentTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reportContentType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.reportContentType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
