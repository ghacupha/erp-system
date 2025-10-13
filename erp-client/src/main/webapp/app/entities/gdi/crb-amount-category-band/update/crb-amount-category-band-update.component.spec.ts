///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { CrbAmountCategoryBandService } from '../service/crb-amount-category-band.service';
import { ICrbAmountCategoryBand, CrbAmountCategoryBand } from '../crb-amount-category-band.model';

import { CrbAmountCategoryBandUpdateComponent } from './crb-amount-category-band-update.component';

describe('CrbAmountCategoryBand Management Update Component', () => {
  let comp: CrbAmountCategoryBandUpdateComponent;
  let fixture: ComponentFixture<CrbAmountCategoryBandUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbAmountCategoryBandService: CrbAmountCategoryBandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbAmountCategoryBandUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbAmountCategoryBandUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbAmountCategoryBandUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbAmountCategoryBandService = TestBed.inject(CrbAmountCategoryBandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbAmountCategoryBand: ICrbAmountCategoryBand = { id: 456 };

      activatedRoute.data = of({ crbAmountCategoryBand });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbAmountCategoryBand));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAmountCategoryBand>>();
      const crbAmountCategoryBand = { id: 123 };
      jest.spyOn(crbAmountCategoryBandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAmountCategoryBand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbAmountCategoryBand }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbAmountCategoryBandService.update).toHaveBeenCalledWith(crbAmountCategoryBand);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAmountCategoryBand>>();
      const crbAmountCategoryBand = new CrbAmountCategoryBand();
      jest.spyOn(crbAmountCategoryBandService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAmountCategoryBand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbAmountCategoryBand }));
      saveSubject.complete();

      // THEN
      expect(crbAmountCategoryBandService.create).toHaveBeenCalledWith(crbAmountCategoryBand);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAmountCategoryBand>>();
      const crbAmountCategoryBand = { id: 123 };
      jest.spyOn(crbAmountCategoryBandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAmountCategoryBand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbAmountCategoryBandService.update).toHaveBeenCalledWith(crbAmountCategoryBand);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
