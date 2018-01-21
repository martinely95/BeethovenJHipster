/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { BeethovenTestModule } from '../../../test.module';
import { MelodyEntityDetailComponent } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity-detail.component';
import { MelodyEntityService } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity.service';
import { MelodyEntity } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity.model';

describe('Component Tests', () => {

    describe('MelodyEntity Management Detail Component', () => {
        let comp: MelodyEntityDetailComponent;
        let fixture: ComponentFixture<MelodyEntityDetailComponent>;
        let service: MelodyEntityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BeethovenTestModule],
                declarations: [MelodyEntityDetailComponent],
                providers: [
                    MelodyEntityService
                ]
            })
            .overrideTemplate(MelodyEntityDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MelodyEntityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MelodyEntityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new MelodyEntity(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.melodyEntity).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
