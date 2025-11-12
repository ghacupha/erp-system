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

import { MoratoriumItemService } from '../service/moratorium-item.service';
import { IMoratoriumItem, MoratoriumItem } from '../moratorium-item.model';

import { MoratoriumItemUpdateComponent } from './moratorium-item-update.component';

describe('MoratoriumItem Management Update Component', () => {
  let comp: MoratoriumItemUpdateComponent;
  let fixture: ComponentFixture<MoratoriumItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moratoriumItemService: MoratoriumItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MoratoriumItemUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MoratoriumItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoratoriumItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moratoriumItemService = TestBed.inject(MoratoriumItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const moratoriumItem: IMoratoriumItem = { id: 456 };

      activatedRoute.data = of({ moratoriumItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(moratoriumItem));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MoratoriumItem>>();
      const moratoriumItem = { id: 123 };
      jest.spyOn(moratoriumItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moratoriumItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moratoriumItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(moratoriumItemService.update).toHaveBeenCalledWith(moratoriumItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MoratoriumItem>>();
      const moratoriumItem = new MoratoriumItem();
      jest.spyOn(moratoriumItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moratoriumItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: moratoriumItem }));
      saveSubject.complete();

      // THEN
      expect(moratoriumItemService.create).toHaveBeenCalledWith(moratoriumItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MoratoriumItem>>();
      const moratoriumItem = { id: 123 };
      jest.spyOn(moratoriumItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moratoriumItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moratoriumItemService.update).toHaveBeenCalledWith(moratoriumItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
