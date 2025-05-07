import {TestBed} from '@angular/core/testing';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {MAT_SNACK_BAR_DATA, MatSnackBar, MatSnackBarRef} from '@angular/material/snack-bar';
import {httpErrorInterceptor} from './http-error.interceptor';
import {SnackbarComponent} from '../components/snackbar/snackbar.component';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {Component, Inject} from '@angular/core';

describe('httpErrorInterceptor (functional)', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;


  @Component({
    selector: 'app-snackbar',
    template: '',
  })
  class MockSnackbarComponent {
    constructor(
      @Inject(MAT_SNACK_BAR_DATA) public data: any,
      public snackBarRef: MatSnackBarRef<MockSnackbarComponent>
    ) {}
  }

  beforeEach(() => {
    const matSnackBarSpy = jasmine.createSpyObj('MatSnackBar', ['openFromComponent']);

    TestBed.configureTestingModule({
      imports: [MockSnackbarComponent],
      declarations: [],
      providers: [
        { provide: MatSnackBar, useValue: matSnackBarSpy },
        provideHttpClient(withInterceptors([httpErrorInterceptor])),
        provideHttpClientTesting()
      ],
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    snackBarSpy = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should open a snackbar with error message on HTTP error', (done) => {
    http.get('/test-url').subscribe({
      next: () => fail('Expected error'),
      error: (err: Error) => {
        expect(err.message).toContain('Error Code: 500');
        expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(SnackbarComponent, jasmine.objectContaining({
          data: jasmine.objectContaining({
            message: jasmine.stringMatching(/Error Code: 500/),
            action: 'Close',
            color: 'error'
          }),
          duration: 5000
        }));
        done();
      }
    });

    const req = httpMock.expectOne('/test-url');
    req.flush({ message: 'Something went wrong' }, { status: 500, statusText: 'Server Error' });
  });
});
