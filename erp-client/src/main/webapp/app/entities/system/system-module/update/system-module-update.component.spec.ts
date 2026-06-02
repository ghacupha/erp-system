jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SystemModuleService } from '../service/system-module.service';
import { ISystemModule, SystemModule } from '../system-module.model';

import { SystemModuleUpdateComponent } from './system-module-update.component';

describe('SystemModule Management Update Component', () => {
  let comp: SystemModuleUpdateComponent;
  let fixture: ComponentFixture<SystemModuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let systemModuleService: SystemModuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SystemModuleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SystemModuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SystemModuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    systemModuleService = TestBed.inject(SystemModuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const systemModule: ISystemModule = { id: 456 };

      activatedRoute.data = of({ systemModule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(systemModule));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SystemModule>>();
      const systemModule = { id: 123 };
      jest.spyOn(systemModuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemModule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemModule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(systemModuleService.update).toHaveBeenCalledWith(systemModule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SystemModule>>();
      const systemModule = new SystemModule();
      jest.spyOn(systemModuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemModule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemModule }));
      saveSubject.complete();

      // THEN
      expect(systemModuleService.create).toHaveBeenCalledWith(systemModule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SystemModule>>();
      const systemModule = { id: 123 };
      jest.spyOn(systemModuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemModule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(systemModuleService.update).toHaveBeenCalledWith(systemModule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
