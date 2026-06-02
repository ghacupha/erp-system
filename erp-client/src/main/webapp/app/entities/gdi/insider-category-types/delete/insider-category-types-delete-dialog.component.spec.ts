jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { InsiderCategoryTypesService } from '../service/insider-category-types.service';

import { InsiderCategoryTypesDeleteDialogComponent } from './insider-category-types-delete-dialog.component';

describe('InsiderCategoryTypes Management Delete Component', () => {
  let comp: InsiderCategoryTypesDeleteDialogComponent;
  let fixture: ComponentFixture<InsiderCategoryTypesDeleteDialogComponent>;
  let service: InsiderCategoryTypesService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InsiderCategoryTypesDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(InsiderCategoryTypesDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InsiderCategoryTypesDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InsiderCategoryTypesService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
