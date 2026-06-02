import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { RouMonthlyDepreciationReportDetailComponent } from './rou-monthly-depreciation-report-detail.component';

describe('RouMonthlyDepreciationReport Management Detail Component', () => {
  let comp: RouMonthlyDepreciationReportDetailComponent;
  let fixture: ComponentFixture<RouMonthlyDepreciationReportDetailComponent>;
  let dataUtils: DataUtils;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouMonthlyDepreciationReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouMonthlyDepreciationReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouMonthlyDepreciationReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouMonthlyDepreciationReportDetailComponent);
    comp = fixture.componentInstance;
    dataUtils = TestBed.inject(DataUtils);
    jest.spyOn(window, 'open').mockImplementation(() => null);
  });

  describe('OnInit', () => {
    it('Should load rouMonthlyDepreciationReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouMonthlyDepreciationReport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('byteSize', () => {
    it('Should call byteSize from DataUtils', () => {
      // GIVEN
      jest.spyOn(dataUtils, 'byteSize');
      const fakeBase64 = 'fake base64';

      // WHEN
      comp.byteSize(fakeBase64);

      // THEN
      expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
    });
  });

  describe('openFile', () => {
    it('Should call openFile from DataUtils', () => {
      const newWindow = { ...window };
      newWindow.document.write = jest.fn();
      window.open = jest.fn(() => newWindow);
      window.onload = jest.fn(() => newWindow);
      window.URL.createObjectURL = jest.fn();
      // GIVEN
      jest.spyOn(dataUtils, 'openFile');
      const fakeContentType = 'fake content type';
      const fakeBase64 = 'fake base64';

      // WHEN
      comp.openFile(fakeBase64, fakeContentType);

      // THEN
      expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
    });
  });
});
