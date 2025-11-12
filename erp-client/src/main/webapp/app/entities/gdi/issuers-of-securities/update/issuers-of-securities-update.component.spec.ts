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

import { IssuersOfSecuritiesService } from '../service/issuers-of-securities.service';
import { IIssuersOfSecurities, IssuersOfSecurities } from '../issuers-of-securities.model';

import { IssuersOfSecuritiesUpdateComponent } from './issuers-of-securities-update.component';

describe('IssuersOfSecurities Management Update Component', () => {
  let comp: IssuersOfSecuritiesUpdateComponent;
  let fixture: ComponentFixture<IssuersOfSecuritiesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let issuersOfSecuritiesService: IssuersOfSecuritiesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IssuersOfSecuritiesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(IssuersOfSecuritiesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IssuersOfSecuritiesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    issuersOfSecuritiesService = TestBed.inject(IssuersOfSecuritiesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const issuersOfSecurities: IIssuersOfSecurities = { id: 456 };

      activatedRoute.data = of({ issuersOfSecurities });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(issuersOfSecurities));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IssuersOfSecurities>>();
      const issuersOfSecurities = { id: 123 };
      jest.spyOn(issuersOfSecuritiesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issuersOfSecurities });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issuersOfSecurities }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(issuersOfSecuritiesService.update).toHaveBeenCalledWith(issuersOfSecurities);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IssuersOfSecurities>>();
      const issuersOfSecurities = new IssuersOfSecurities();
      jest.spyOn(issuersOfSecuritiesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issuersOfSecurities });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issuersOfSecurities }));
      saveSubject.complete();

      // THEN
      expect(issuersOfSecuritiesService.create).toHaveBeenCalledWith(issuersOfSecurities);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IssuersOfSecurities>>();
      const issuersOfSecurities = { id: 123 };
      jest.spyOn(issuersOfSecuritiesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issuersOfSecurities });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(issuersOfSecuritiesService.update).toHaveBeenCalledWith(issuersOfSecurities);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
