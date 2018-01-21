import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BeethovenMelodyEntityModule } from './melody-entity/melody-entity.module';
import { BeethovenProfileModule } from './profile/profile.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        BeethovenMelodyEntityModule,
        BeethovenProfileModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BeethovenEntityModule {}
