import { Component } from '@angular/core';

import { Path } from '../core/models/path';
import { PhilosophyService } from '../core/services/philosophy.service';
import { PathNode } from '../core/models/path-node';

@Component({
  selector: 'home',
  templateUrl: 'home.component.html'
})
export class HomeComponent {
  private path: Path;

  get displayPath(): boolean { return !!this.path; }
  get pathNodes(): PathNode[] { return this.path.pathNodes; }
  get foundPhilosophy(): boolean { return this.path && this.path.foundPhilosophy; }
  get hops(): number { return this.path.hops; }

  constructor(private philosophyService: PhilosophyService) { }

  findPath(search: string) {
    this.philosophyService.findPath(search)
    .subscribe(newPath => this.path = newPath);
  }
}
