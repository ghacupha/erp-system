import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { CurrencyAuthenticityFlagDetailComponent } from './currency-authenticity-flag-detail.component';

describe('CurrencyAuthenticityFlag Management Detail Component', () => {
  let comp: CurrencyAuthenticityFlagDetailComponent;
  let fixture: ComponentFixture<CurrencyAuthenticityFlagDetailComponent>;
  let dataUtils: DataUtils;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CurrencyAuthenticityFlagDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ currencyAuthenticityFlag: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CurrencyAuthenticityFlagDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CurrencyAuthenticityFlagDetailComponent);
    comp = fixture.componentInstance;
    dataUtils = TestBed.inject(DataUtils);
    jest.spyOn(window, 'open').mockImplementation(() => null);
  });

  describe('OnInit', () => {
    it('Should load currencyAuthenticityFlag on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.currencyAuthenticityFlag).toEqual(expect.objectContaining({ id: 123 }));
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
