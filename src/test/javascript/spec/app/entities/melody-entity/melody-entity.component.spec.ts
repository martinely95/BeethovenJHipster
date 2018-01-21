/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { BeethovenTestModule } from '../../../test.module';
import { MelodyEntityComponent } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity.component';
import { MelodyEntityService } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity.service';
import { MelodyEntity } from '../../../../../../main/webapp/app/entities/melody-entity/melody-entity.model';

describe('Component Tests', () => {

    describe('MelodyEntity Management Component', () => {
        let comp: MelodyEntityComponent;
        let fixture: ComponentFixture<MelodyEntityComponent>;
        let service: MelodyEntityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BeethovenTestModule],
                declarations: [MelodyEntityComponent],
                providers: [
                    MelodyEntityService
                ]
            })
            .overrideTemplate(MelodyEntityComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MelodyEntityComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MelodyEntityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new MelodyEntity(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.melodyEntities[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
