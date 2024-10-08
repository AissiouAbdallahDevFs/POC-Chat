import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { Subscription } from 'rxjs';
import { Profile,updatedUser } from 'src/app/interfaces/profile.interface';


@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.scss']
})
export class ProfilePageComponent implements OnInit, OnDestroy {
  user: Profile = { id: 0, username: '', email: '', created_at: new Date(), updated_at: new Date()};
  updatedUser: updatedUser = { username: '', email: '' };
  private meSubscription: Subscription | undefined;
  private themesSubscription: Subscription | undefined;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    private snackbarService: SnackbarService
  ) { }

  ngOnInit(): void {
    this.meSubscription = this.http.get<Profile>('/api/auth/me').subscribe((data) => {
      this.user = data;
      this.updatedUser.username = this.user.username;
      this.updatedUser.email = this.user.email;
    });

  }

  logout() {
    this.authService.logout();
    this.router.navigateByUrl('/');
  }

  unSubscribeTheme(themeId: number) {
    const headers = { 'Access-Control-Allow-Origin': '*' };
    this.http.delete(`/api/auth/unsubscribe/${themeId}`, { headers }).subscribe(() => {
      this.snackbarService.openSnackBar('Modifications enregistrées avec succès !', 'Fermer'); 
    });
  }

  saveChanges(): void {
    this.http.put('/api/auth/me', this.updatedUser).subscribe(() => {
      this.snackbarService.openSnackBar('Modifications enregistrées avec succès !', 'Fermer'); 
    });
  }

  ngOnDestroy(): void {
    if (this.meSubscription) {
      this.meSubscription.unsubscribe();
    }
    if (this.themesSubscription) {
      this.themesSubscription.unsubscribe();
    }
  }
}