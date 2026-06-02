import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { NatureOfCustomerComplaintsDetailComponent } from './nature-of-customer-complaints-detail.component';

describe('NatureOfCustomerComplaints Management Detail Component', () => {
  let comp: NatureOfCustomerComplaintsDetailComponent;
  let fixture: ComponentFixture<NatureOfCustomerComplaintsDetailComponent>;
  let dataUtils: DataUtils;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NatureOfCustomerComplaintsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ natureOfCustomerComplaints: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NatureOfCustomerComplaintsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NatureOfCustomerComplaintsDetailComponent);
    comp = fixture.componentInstance;
    dataUtils = TestBed.inject(DataUtils);
    jest.spyOn(window, 'open').mockImplementation(() => null);
  });

  describe('OnInit', () => {
    it('Should load natureOfCustomerComplaints on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.natureOfCustomerComplaints).toEqual(expect.objectContaining({ id: 123 }));
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
