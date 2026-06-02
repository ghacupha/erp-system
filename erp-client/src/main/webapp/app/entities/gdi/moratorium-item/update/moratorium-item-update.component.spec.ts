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
