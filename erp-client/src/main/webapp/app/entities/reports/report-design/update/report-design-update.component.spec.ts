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

import { ReportDesignService } from '../service/report-design.service';
import { IReportDesign, ReportDesign } from '../report-design.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { ISecurityClearance } from 'app/entities/people/security-clearance/security-clearance.model';
import { SecurityClearanceService } from 'app/entities/people/security-clearance/service/security-clearance.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { ISystemModule } from 'app/entities/system/system-module/system-module.model';
import { SystemModuleService } from 'app/entities/system/system-module/service/system-module.service';
import { IAlgorithm } from 'app/entities/system/algorithm/algorithm.model';
import { AlgorithmService } from 'app/entities/system/algorithm/service/algorithm.service';

import { ReportDesignUpdateComponent } from './report-design-update.component';

describe('ReportDesign Management Update Component', () => {
  let comp: ReportDesignUpdateComponent;
  let fixture: ComponentFixture<ReportDesignUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportDesignService: ReportDesignService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let securityClearanceService: SecurityClearanceService;
  let applicationUserService: ApplicationUserService;
  let dealerService: DealerService;
  let placeholderService: PlaceholderService;
  let systemModuleService: SystemModuleService;
  let algorithmService: AlgorithmService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReportDesignUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ReportDesignUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportDesignUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportDesignService = TestBed.inject(ReportDesignService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    dealerService = TestBed.inject(DealerService);
    placeholderService = TestBed.inject(PlaceholderService);
    systemModuleService = TestBed.inject(SystemModuleService);
    algorithmService = TestBed.inject(AlgorithmService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const parameters: IUniversallyUniqueMapping[] = [{ id: 70076 }];
      reportDesign.parameters = parameters;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 58257 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...parameters];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityClearance query and add missing value', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const securityClearance: ISecurityClearance = { id: 70762 };
      reportDesign.securityClearance = securityClearance;

      const securityClearanceCollection: ISecurityClearance[] = [{ id: 40082 }];
      jest.spyOn(securityClearanceService, 'query').mockReturnValue(of(new HttpResponse({ body: securityClearanceCollection })));
      const additionalSecurityClearances = [securityClearance];
      const expectedCollection: ISecurityClearance[] = [...additionalSecurityClearances, ...securityClearanceCollection];
      jest.spyOn(securityClearanceService, 'addSecurityClearanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(securityClearanceService.query).toHaveBeenCalled();
      expect(securityClearanceService.addSecurityClearanceToCollectionIfMissing).toHaveBeenCalledWith(
        securityClearanceCollection,
        ...additionalSecurityClearances
      );
      expect(comp.securityClearancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const reportDesigner: IApplicationUser = { id: 59146 };
      reportDesign.reportDesigner = reportDesigner;

      const applicationUserCollection: IApplicationUser[] = [{ id: 55234 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [reportDesigner];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const organization: IDealer = { id: 97302 };
      reportDesign.organization = organization;
      const department: IDealer = { id: 50670 };
      reportDesign.department = department;

      const dealerCollection: IDealer[] = [{ id: 68684 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [organization, department];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 38136 }];
      reportDesign.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 85828 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SystemModule query and add missing value', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const systemModule: ISystemModule = { id: 91620 };
      reportDesign.systemModule = systemModule;

      const systemModuleCollection: ISystemModule[] = [{ id: 92035 }];
      jest.spyOn(systemModuleService, 'query').mockReturnValue(of(new HttpResponse({ body: systemModuleCollection })));
      const additionalSystemModules = [systemModule];
      const expectedCollection: ISystemModule[] = [...additionalSystemModules, ...systemModuleCollection];
      jest.spyOn(systemModuleService, 'addSystemModuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(systemModuleService.query).toHaveBeenCalled();
      expect(systemModuleService.addSystemModuleToCollectionIfMissing).toHaveBeenCalledWith(
        systemModuleCollection,
        ...additionalSystemModules
      );
      expect(comp.systemModulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Algorithm query and add missing value', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const fileCheckSumAlgorithm: IAlgorithm = { id: 73099 };
      reportDesign.fileCheckSumAlgorithm = fileCheckSumAlgorithm;

      const algorithmCollection: IAlgorithm[] = [{ id: 75364 }];
      jest.spyOn(algorithmService, 'query').mockReturnValue(of(new HttpResponse({ body: algorithmCollection })));
      const additionalAlgorithms = [fileCheckSumAlgorithm];
      const expectedCollection: IAlgorithm[] = [...additionalAlgorithms, ...algorithmCollection];
      jest.spyOn(algorithmService, 'addAlgorithmToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(algorithmService.query).toHaveBeenCalled();
      expect(algorithmService.addAlgorithmToCollectionIfMissing).toHaveBeenCalledWith(algorithmCollection, ...additionalAlgorithms);
      expect(comp.algorithmsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportDesign: IReportDesign = { id: 456 };
      const parameters: IUniversallyUniqueMapping = { id: 35505 };
      reportDesign.parameters = [parameters];
      const securityClearance: ISecurityClearance = { id: 96364 };
      reportDesign.securityClearance = securityClearance;
      const reportDesigner: IApplicationUser = { id: 72238 };
      reportDesign.reportDesigner = reportDesigner;
      const organization: IDealer = { id: 56094 };
      reportDesign.organization = organization;
      const department: IDealer = { id: 88590 };
      reportDesign.department = department;
      const placeholders: IPlaceholder = { id: 27572 };
      reportDesign.placeholders = [placeholders];
      const systemModule: ISystemModule = { id: 97044 };
      reportDesign.systemModule = systemModule;
      const fileCheckSumAlgorithm: IAlgorithm = { id: 96305 };
      reportDesign.fileCheckSumAlgorithm = fileCheckSumAlgorithm;

      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reportDesign));
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(parameters);
      expect(comp.securityClearancesSharedCollection).toContain(securityClearance);
      expect(comp.applicationUsersSharedCollection).toContain(reportDesigner);
      expect(comp.dealersSharedCollection).toContain(organization);
      expect(comp.dealersSharedCollection).toContain(department);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.systemModulesSharedCollection).toContain(systemModule);
      expect(comp.algorithmsSharedCollection).toContain(fileCheckSumAlgorithm);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportDesign>>();
      const reportDesign = { id: 123 };
      jest.spyOn(reportDesignService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportDesign }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportDesignService.update).toHaveBeenCalledWith(reportDesign);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportDesign>>();
      const reportDesign = new ReportDesign();
      jest.spyOn(reportDesignService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportDesign }));
      saveSubject.complete();

      // THEN
      expect(reportDesignService.create).toHaveBeenCalledWith(reportDesign);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportDesign>>();
      const reportDesign = { id: 123 };
      jest.spyOn(reportDesignService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDesign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportDesignService.update).toHaveBeenCalledWith(reportDesign);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSecurityClearanceById', () => {
      it('Should return tracked SecurityClearance primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityClearanceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
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

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSystemModuleById', () => {
      it('Should return tracked SystemModule primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSystemModuleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAlgorithmById', () => {
      it('Should return tracked Algorithm primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAlgorithmById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPlaceholder', () => {
      it('Should return option if no Placeholder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlaceholder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Placeholder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Placeholder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
