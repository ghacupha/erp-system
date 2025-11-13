///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';
import { IIFRS16LeaseContract, IFRS16LeaseContract } from '../ifrs-16-lease-contract.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { BusinessDocumentService } from 'app/entities/documentation/business-document/service/business-document.service';

import { IFRS16LeaseContractUpdateComponent } from './ifrs-16-lease-contract-update.component';

describe('IFRS16LeaseContract Management Update Component', () => {
  let comp: IFRS16LeaseContractUpdateComponent;
  let fixture: ComponentFixture<IFRS16LeaseContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let iFRS16LeaseContractService: IFRS16LeaseContractService;
  let serviceOutletService: ServiceOutletService;
  let dealerService: DealerService;
  let fiscalMonthService: FiscalMonthService;
  let businessDocumentService: BusinessDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IFRS16LeaseContractUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(IFRS16LeaseContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IFRS16LeaseContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    iFRS16LeaseContractService = TestBed.inject(IFRS16LeaseContractService);
    serviceOutletService = TestBed.inject(ServiceOutletService);
    dealerService = TestBed.inject(DealerService);
    fiscalMonthService = TestBed.inject(FiscalMonthService);
    businessDocumentService = TestBed.inject(BusinessDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceOutlet query and add missing value', () => {
      const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 456 };
      const superintendentServiceOutlet: IServiceOutlet = { id: 38791 };
      iFRS16LeaseContract.superintendentServiceOutlet = superintendentServiceOutlet;

      const serviceOutletCollection: IServiceOutlet[] = [{ id: 49301 }];
      jest.spyOn(serviceOutletService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceOutletCollection })));
      const additionalServiceOutlets = [superintendentServiceOutlet];
      const expectedCollection: IServiceOutlet[] = [...additionalServiceOutlets, ...serviceOutletCollection];
      jest.spyOn(serviceOutletService, 'addServiceOutletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      expect(serviceOutletService.query).toHaveBeenCalled();
      expect(serviceOutletService.addServiceOutletToCollectionIfMissing).toHaveBeenCalledWith(
        serviceOutletCollection,
        ...additionalServiceOutlets
      );
      expect(comp.serviceOutletsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 456 };
      const mainDealer: IDealer = { id: 82292 };
      iFRS16LeaseContract.mainDealer = mainDealer;

      const dealerCollection: IDealer[] = [{ id: 674 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [mainDealer];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiscalMonth query and add missing value', () => {
      const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 456 };
      const firstReportingPeriod: IFiscalMonth = { id: 81058 };
      iFRS16LeaseContract.firstReportingPeriod = firstReportingPeriod;
      const lastReportingPeriod: IFiscalMonth = { id: 39910 };
      iFRS16LeaseContract.lastReportingPeriod = lastReportingPeriod;

      const fiscalMonthCollection: IFiscalMonth[] = [{ id: 23784 }];
      jest.spyOn(fiscalMonthService, 'query').mockReturnValue(of(new HttpResponse({ body: fiscalMonthCollection })));
      const additionalFiscalMonths = [firstReportingPeriod, lastReportingPeriod];
      const expectedCollection: IFiscalMonth[] = [...additionalFiscalMonths, ...fiscalMonthCollection];
      jest.spyOn(fiscalMonthService, 'addFiscalMonthToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      expect(fiscalMonthService.query).toHaveBeenCalled();
      expect(fiscalMonthService.addFiscalMonthToCollectionIfMissing).toHaveBeenCalledWith(fiscalMonthCollection, ...additionalFiscalMonths);
      expect(comp.fiscalMonthsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessDocument query and add missing value', () => {
      const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 456 };
      const leaseContractDocument: IBusinessDocument = { id: 51552 };
      iFRS16LeaseContract.leaseContractDocument = leaseContractDocument;
      const leaseContractCalculations: IBusinessDocument = { id: 69686 };
      iFRS16LeaseContract.leaseContractCalculations = leaseContractCalculations;

      const businessDocumentCollection: IBusinessDocument[] = [{ id: 50636 }];
      jest.spyOn(businessDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: businessDocumentCollection })));
      const additionalBusinessDocuments = [leaseContractDocument, leaseContractCalculations];
      const expectedCollection: IBusinessDocument[] = [...additionalBusinessDocuments, ...businessDocumentCollection];
      jest.spyOn(businessDocumentService, 'addBusinessDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      expect(businessDocumentService.query).toHaveBeenCalled();
      expect(businessDocumentService.addBusinessDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        businessDocumentCollection,
        ...additionalBusinessDocuments
      );
      expect(comp.businessDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 456 };
      const superintendentServiceOutlet: IServiceOutlet = { id: 76435 };
      iFRS16LeaseContract.superintendentServiceOutlet = superintendentServiceOutlet;
      const mainDealer: IDealer = { id: 23457 };
      iFRS16LeaseContract.mainDealer = mainDealer;
      const firstReportingPeriod: IFiscalMonth = { id: 72142 };
      iFRS16LeaseContract.firstReportingPeriod = firstReportingPeriod;
      const lastReportingPeriod: IFiscalMonth = { id: 615 };
      iFRS16LeaseContract.lastReportingPeriod = lastReportingPeriod;
      const leaseContractDocument: IBusinessDocument = { id: 26031 };
      iFRS16LeaseContract.leaseContractDocument = leaseContractDocument;
      const leaseContractCalculations: IBusinessDocument = { id: 32620 };
      iFRS16LeaseContract.leaseContractCalculations = leaseContractCalculations;

      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(iFRS16LeaseContract));
      expect(comp.serviceOutletsSharedCollection).toContain(superintendentServiceOutlet);
      expect(comp.dealersSharedCollection).toContain(mainDealer);
      expect(comp.fiscalMonthsSharedCollection).toContain(firstReportingPeriod);
      expect(comp.fiscalMonthsSharedCollection).toContain(lastReportingPeriod);
      expect(comp.businessDocumentsSharedCollection).toContain(leaseContractDocument);
      expect(comp.businessDocumentsSharedCollection).toContain(leaseContractCalculations);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFRS16LeaseContract>>();
      const iFRS16LeaseContract = { id: 123 };
      jest.spyOn(iFRS16LeaseContractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: iFRS16LeaseContract }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(iFRS16LeaseContractService.update).toHaveBeenCalledWith(iFRS16LeaseContract);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFRS16LeaseContract>>();
      const iFRS16LeaseContract = new IFRS16LeaseContract();
      jest.spyOn(iFRS16LeaseContractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: iFRS16LeaseContract }));
      saveSubject.complete();

      // THEN
      expect(iFRS16LeaseContractService.create).toHaveBeenCalledWith(iFRS16LeaseContract);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFRS16LeaseContract>>();
      const iFRS16LeaseContract = { id: 123 };
      jest.spyOn(iFRS16LeaseContractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ iFRS16LeaseContract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(iFRS16LeaseContractService.update).toHaveBeenCalledWith(iFRS16LeaseContract);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackServiceOutletById', () => {
      it('Should return tracked ServiceOutlet primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServiceOutletById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFiscalMonthById', () => {
      it('Should return tracked FiscalMonth primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiscalMonthById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBusinessDocumentById', () => {
      it('Should return tracked BusinessDocument primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBusinessDocumentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
