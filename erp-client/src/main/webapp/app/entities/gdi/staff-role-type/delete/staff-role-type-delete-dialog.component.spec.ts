jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { StaffRoleTypeService } from '../service/staff-role-type.service';

import { StaffRoleTypeDeleteDialogComponent } from './staff-role-type-delete-dialog.component';

describe('StaffRoleType Management Delete Component', () => {
  let comp: StaffRoleTypeDeleteDialogComponent;
  let fixture: ComponentFixture<StaffRoleTypeDeleteDialogComponent>;
  let service: StaffRoleTypeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StaffRoleTypeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(StaffRoleTypeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StaffRoleTypeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StaffRoleTypeService);
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
