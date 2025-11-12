///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { CountySubCountyCodeService } from '../service/county-sub-county-code.service';
import { ICountySubCountyCode, CountySubCountyCode } from '../county-sub-county-code.model';

import { CountySubCountyCodeUpdateComponent } from './county-sub-county-code-update.component';

describe('CountySubCountyCode Management Update Component', () => {
  let comp: CountySubCountyCodeUpdateComponent;
  let fixture: ComponentFixture<CountySubCountyCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countySubCountyCodeService: CountySubCountyCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountySubCountyCodeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountySubCountyCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountySubCountyCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countySubCountyCodeService = TestBed.inject(CountySubCountyCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const countySubCountyCode: ICountySubCountyCode = { id: 456 };

      activatedRoute.data = of({ countySubCountyCode });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(countySubCountyCode));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountySubCountyCode>>();
      const countySubCountyCode = { id: 123 };
      jest.spyOn(countySubCountyCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countySubCountyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countySubCountyCode }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countySubCountyCodeService.update).toHaveBeenCalledWith(countySubCountyCode);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountySubCountyCode>>();
      const countySubCountyCode = new CountySubCountyCode();
      jest.spyOn(countySubCountyCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countySubCountyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countySubCountyCode }));
      saveSubject.complete();

      // THEN
      expect(countySubCountyCodeService.create).toHaveBeenCalledWith(countySubCountyCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountySubCountyCode>>();
      const countySubCountyCode = { id: 123 };
      jest.spyOn(countySubCountyCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countySubCountyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countySubCountyCodeService.update).toHaveBeenCalledWith(countySubCountyCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
