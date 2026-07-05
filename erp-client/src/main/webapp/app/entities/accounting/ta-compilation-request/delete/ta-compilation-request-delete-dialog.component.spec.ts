jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TACompilationRequestService } from '../service/ta-compilation-request.service';

import { TACompilationRequestDeleteDialogComponent } from './ta-compilation-request-delete-dialog.component';

describe('TACompilationRequest Management Delete Component', () => {
  let comp: TACompilationRequestDeleteDialogComponent;
  let fixture: ComponentFixture<TACompilationRequestDeleteDialogComponent>;
  let service: TACompilationRequestService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TACompilationRequestDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TACompilationRequestDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TACompilationRequestDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TACompilationRequestService);
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
