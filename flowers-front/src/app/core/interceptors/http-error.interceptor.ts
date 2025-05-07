import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from 'rxjs';
import {inject} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {SnackbarComponent} from '../../shared/components/snackbar/snackbar.component';


export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {


  const _snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMsg = 'An unknown error occurred';

      if (error.error instanceof ErrorEvent) {
        // Client-side error
        errorMsg = `Error: ${error.error.message}`;
      } else {
        // Server-side error
        errorMsg = `Error Code: ${error.status}\nMessage: ${error.message}`;
      }

      _snackBar.openFromComponent(SnackbarComponent, {
        data: {message: errorMsg, action: 'Close', color: 'error'},
        duration: 5000,
      });

      return throwError(() => new Error(errorMsg));
    })
  );
};
