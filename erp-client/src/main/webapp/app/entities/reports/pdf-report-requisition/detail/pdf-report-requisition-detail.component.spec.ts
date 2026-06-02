import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PdfReportRequisitionDetailComponent } from './pdf-report-requisition-detail.component';

describe('PdfReportRequisition Management Detail Component', () => {
  let comp: PdfReportRequisitionDetailComponent;
  let fixture: ComponentFixture<PdfReportRequisitionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PdfReportRequisitionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pdfReportRequisition: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PdfReportRequisitionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PdfReportRequisitionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pdfReportRequisition on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pdfReportRequisition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
