import { NgModule, ModuleWithProviders } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { PhilosophyService } from './services/philosophy.service';

@NgModule({
  imports: [
    HttpClientModule
  ]
})
export class CoreModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: CoreModule,
      providers: [
        PhilosophyService
      ]
    };
  }
}
