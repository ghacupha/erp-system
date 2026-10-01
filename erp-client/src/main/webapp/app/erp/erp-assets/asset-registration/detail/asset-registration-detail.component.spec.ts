///
/// Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
/// Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { BehaviorSubject, of, throwError } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { IAssetRegistration } from '../asset-registration.model';

import { AssetRegistrationDetailComponent } from './asset-registration-detail.component';

describe('AssetRegistration Management Detail Component', () => {
  let comp: AssetRegistrationDetailComponent;
  let fixture: ComponentFixture<AssetRegistrationDetailComponent>;
  let dataUtils: DataUtils;

  // Mutable route data so each test can drive the resolver payload before ngOnInit().
  const routeData$ = new BehaviorSubject<{ assetRegistration: Partial<IAssetRegistration> }>({ assetRegistration: { id: 123 } });

  const settlementServiceMock = {
    find: jest.fn((id: number) =>
      of(new HttpResponse({ body: { id, businessDocuments: [{ id: id * 10, documentTitle: `settlement-doc-${id}` }] } }))
    ),
  };
  const businessDocumentServiceMock = {
    find: jest.fn((id: number) =>
      of(
        new HttpResponse({
          body: { id, documentTitle: `doc-${id}`, documentFile: 'ZmlsZQ==', documentFileContentType: 'application/pdf' },
        })
      )
    ),
  };

  const initWith = (assetRegistration: Partial<IAssetRegistration>): void => {
    routeData$.next({ assetRegistration });
    comp.ngOnInit();
  };

  beforeEach(() => {
    routeData$.next({ assetRegistration: { id: 123 } });
    settlementServiceMock.find.mockClear();
    businessDocumentServiceMock.find.mockClear();
    businessDocumentServiceMock.find.mockImplementation((id: number) =>
      of(
        new HttpResponse({
          body: { id, documentTitle: `doc-${id}`, documentFile: 'ZmlsZQ==', documentFileContentType: 'application/pdf' },
        })
      )
    );

    (global as any).URL.createObjectURL = jest.fn(() => 'blob:mock-url');
    (global as any).URL.revokeObjectURL = jest.fn();

    TestBed.configureTestingModule({
      declarations: [AssetRegistrationDetailComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { data: routeData$ } },
        { provide: SettlementService, useValue: settlementServiceMock },
        { provide: BusinessDocumentService, useValue: businessDocumentServiceMock },
      ],
    })
      .overrideTemplate(AssetRegistrationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetRegistrationDetailComponent);
    comp = fixture.componentInstance;
    dataUtils = TestBed.inject(DataUtils);
    jest.spyOn(window, 'open').mockImplementation(() => null);
  });

  describe('OnInit', () => {
    it('Should load assetRegistration on init', () => {
      comp.ngOnInit();

      expect(comp.assetRegistration).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('Should not call the settlement service when the asset has no related settlements', () => {
      initWith({ id: 1, businessDocuments: [{ id: 7, documentTitle: 'own' }] });

      expect(settlementServiceMock.find).not.toHaveBeenCalled();
      expect(comp.relatedBusinessDocuments.map(doc => doc.id)).toEqual([7]);
    });

    it('Should merge related documents from the acquiring transaction and other related settlements, de-duplicated', () => {
      initWith({
        id: 1,
        businessDocuments: [{ id: 1, documentTitle: 'own-doc' }],
        acquiringTransaction: { id: 2 },
        otherRelatedSettlements: [{ id: 3 }, { id: 2 }],
      });

      // id 2 requested once despite appearing in both acquiringTransaction and otherRelatedSettlements
      expect(settlementServiceMock.find).toHaveBeenCalledWith(2);
      expect(settlementServiceMock.find).toHaveBeenCalledWith(3);
      expect(settlementServiceMock.find).toHaveBeenCalledTimes(2);
      expect(comp.relatedBusinessDocuments.map(doc => doc.id)).toEqual([1, 20, 30]);
    });

    it('Should reuse the acquiring settlement response for the acquiring-transaction summary line', () => {
      initWith({ id: 1, acquiringTransaction: { id: 2 } });

      expect(comp.assetAcquiringTransaction).toEqual(expect.objectContaining({ id: 2 }));
    });

    it('Should survive a failing settlement fetch and still surface the remaining documents', () => {
      settlementServiceMock.find.mockImplementationOnce(() => throwError(() => new Error('boom')));
      initWith({
        id: 1,
        businessDocuments: [{ id: 1, documentTitle: 'own-doc' }],
        acquiringTransaction: { id: 2 },
        otherRelatedSettlements: [{ id: 3 }],
      });

      expect(comp.relatedBusinessDocuments.map(doc => doc.id)).toContain(1);
      expect(comp.loadingDocuments).toBe(false);
    });
  });

  describe('business document preview', () => {
    it('Should build an inline preview for the selected document', () => {
      comp.selectBusinessDocument({ id: 5, documentTitle: 'Invoice' });

      expect(businessDocumentServiceMock.find).toHaveBeenCalledWith(5);
      expect(comp.selectedBusinessDocument).toEqual(expect.objectContaining({ id: 5 }));
      expect(comp.documentPreviewTitle).toBe('doc-5');
      expect(comp.documentPreviewUrl).toBeTruthy();
    });

    it('Should clear the preview when the fetched document has no file payload', () => {
      businessDocumentServiceMock.find.mockReturnValueOnce(of(new HttpResponse({ body: { id: 6, documentTitle: 'empty' } })));

      comp.selectBusinessDocument({ id: 6, documentTitle: 'empty' });

      expect(comp.documentPreviewUrl).toBeNull();
      expect(comp.selectedBusinessDocument).toBeNull();
    });

    it('Should stream the file to a new tab via DataUtils.openFile', () => {
      jest.spyOn(dataUtils, 'openFile').mockImplementation(() => undefined);

      comp.openBusinessDocumentFile({ id: 9, documentTitle: 'Agreement' });

      expect(businessDocumentServiceMock.find).toHaveBeenCalledWith(9);
      expect(dataUtils.openFile).toHaveBeenCalledWith('ZmlsZQ==', 'application/pdf');
    });
  });

  describe('cleanup', () => {
    it('Should revoke the preview object url on destroy', () => {
      comp.selectBusinessDocument({ id: 5, documentTitle: 'Invoice' });

      comp.ngOnDestroy();

      expect((global as any).URL.revokeObjectURL).toHaveBeenCalledWith('blob:mock-url');
    });
  });

  describe('byteSize', () => {
    it('Should call byteSize from DataUtils', () => {
      jest.spyOn(dataUtils, 'byteSize');
      const fakeBase64 = 'fake base64';

      comp.byteSize(fakeBase64);

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
      jest.spyOn(dataUtils, 'openFile');
      const fakeContentType = 'fake content type';
      const fakeBase64 = 'fake base64';

      comp.openFile(fakeBase64, fakeContentType);

      expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
    });
  });
});
