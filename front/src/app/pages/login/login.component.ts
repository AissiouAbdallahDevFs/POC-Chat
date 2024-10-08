import { Component, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { LoginForm } from 'src/app/interfaces/login.interface';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { responseLogin } from 'src/app/interfaces/login.interface';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnDestroy {
  private loginSubscription: Subscription | undefined;
  formData: LoginForm = { email: '', password: '' };

  constructor(private http: HttpClient, private router: Router, private snackbarService: SnackbarService ) {}

  login(): void {
    this.loginSubscription = this.http.post<responseLogin>('/api/auth/login', this.formData)
      .subscribe(
        (response) => {
          if (response && response.token) {
            this.snackbarService.openSnackBar('Connexion réussie !', 'Fermer');
            localStorage.setItem('token', response.token);
            this.router.navigate(['/profile']);
          } 
        }, 
        (error) => {
          console.error('Erreur lors de la connexion:', error);
          this.snackbarService.openSnackBar('Erreur lors de la connexion', 'Fermer');
        }
      );
  }

  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
  }
}
