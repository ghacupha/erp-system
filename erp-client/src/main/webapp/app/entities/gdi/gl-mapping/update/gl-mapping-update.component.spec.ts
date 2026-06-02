jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GlMappingService } from '../service/gl-mapping.service';
import { IGlMapping, GlMapping } from '../gl-mapping.model';

import { GlMappingUpdateComponent } from './gl-mapping-update.component';

describe('GlMapping Management Update Component', () => {
  let comp: GlMappingUpdateComponent;
  let fixture: ComponentFixture<GlMappingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let glMappingService: GlMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GlMappingUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(GlMappingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GlMappingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    glMappingService = TestBed.inject(GlMappingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const glMapping: IGlMapping = { id: 456 };

      activatedRoute.data = of({ glMapping });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(glMapping));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GlMapping>>();
      const glMapping = { id: 123 };
      jest.spyOn(glMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ glMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: glMapping }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(glMappingService.update).toHaveBeenCalledWith(glMapping);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GlMapping>>();
      const glMapping = new GlMapping();
      jest.spyOn(glMappingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ glMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: glMapping }));
      saveSubject.complete();

      // THEN
      expect(glMappingService.create).toHaveBeenCalledWith(glMapping);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GlMapping>>();
      const glMapping = { id: 123 };
      jest.spyOn(glMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ glMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(glMappingService.update).toHaveBeenCalledWith(glMapping);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
