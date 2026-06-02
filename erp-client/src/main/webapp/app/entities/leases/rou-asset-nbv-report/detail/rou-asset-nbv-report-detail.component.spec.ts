import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { RouAssetNBVReportDetailComponent } from './rou-asset-nbv-report-detail.component';

describe('RouAssetNBVReport Management Detail Component', () => {
  let comp: RouAssetNBVReportDetailComponent;
  let fixture: ComponentFixture<RouAssetNBVReportDetailComponent>;
  let dataUtils: DataUtils;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouAssetNBVReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouAssetNBVReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouAssetNBVReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouAssetNBVReportDetailComponent);
    comp = fixture.componentInstance;
    dataUtils = TestBed.inject(DataUtils);
    jest.spyOn(window, 'open').mockImplementation(() => null);
  });

  describe('OnInit', () => {
    it('Should load rouAssetNBVReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouAssetNBVReport).toEqual(expect.objectContaining({ id: 123 }));
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
