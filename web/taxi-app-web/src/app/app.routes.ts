import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { ForgotPasswordComponent } from './auth/forgot-password/forgot-password';
import { ResetPasswordComponent } from './auth/reset-password/reset-password';
import { HomeComponent } from './home/home';
import { DriverHomeComponent } from './driver-home/driver-home';
import { UserInfoComponent } from './user-info/user-info';
import { DriverRideHistoryComponent } from './driver/driver-ride-history/driver-ride-history';
import { RideDetailsComponent } from './shared/ride-details/ride-details';

export const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'login', component: LoginComponent},
  { path: 'user-info', component: UserInfoComponent },
  { path: 'driver-home', component: DriverHomeComponent },

  { path: 'driver-ride-history', component: DriverRideHistoryComponent },
  { path: 'ride-details', component: RideDetailsComponent },
  { path: '**', redirectTo: '' },
];
