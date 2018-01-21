import { BaseEntity } from './../../shared';

export class Profile implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
        public melodyEntities?: BaseEntity[],
    ) {
    }
}
