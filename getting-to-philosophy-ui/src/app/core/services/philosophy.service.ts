import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Path } from '../models/path';

@Injectable()
export class PhilosophyService {
  private url = 'http://localhost:8080/findPhilosophy';

  constructor(private http: HttpClient) { }

  findPath(initialUrl: string): Observable<Path> {
    return this.http.post<Path>(`${this.url}?url=https://en.wikipedia.org/wiki/${initialUrl}`, null)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      console.error('Client-side or network error occurred:', error.error.message);
    } else {
      console.error(`Error code ${error.status} with body ${error.error}`);
    }
    return throwError('Something went wrong');
  }
}
