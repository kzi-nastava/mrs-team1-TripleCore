import { provideRouter, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./auth/register/register').then(m => m.RegisterComponent)
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./auth/forgot-password/forgot-password').then(m => m.ForgotPasswordComponent)
  },
  {
    path: 'reset-password',
    loadComponent: () => import('./auth/reset-password/reset-password').then(m => m.ResetPasswordComponent)
  },
  {
    path: 'home',
    loadComponent: () => import('./home/home').then(m => m.HomeComponent)
  },
  {
    path: 'driver-home',
    loadComponent: () => import('./driver-home/driver-home').then(m => m.DriverHomeComponent)
  },
  {
    path: 'user-info',
    loadComponent: () => import('./user-info/user-info').then(m => m.UserInfoComponent)

  },
  {
    path: 'driver-ride-history',
    loadComponent: () => import('./driver/driver-ride-history/driver-ride-history').then(m => m.DriverRideHistoryComponent)
  },
  {
    path: 'ride-details',
    loadComponent: () => import('./shared/ride-details/ride-details').then(m => m.RideDetailsComponent)
  },
  {
    path: 'estimate-route',
    loadComponent: () => import('./estimate-route/estimate-route').then(m => m.EstimateRouteComponent)
  },
  {
    path: 'admin-home',
    loadComponent: () => import('./admin/admin-home/admin-home').then(m => m.AdminHomeComponent)
  },
  {
    path: 'admin-ride-history',
    loadComponent: () => import('./admin/admin-ride-history/admin-ride-history').then(m => m.AdminRideHistoryComponent)
  },
  {
    path: 'admin-ride-details/:id',
    loadComponent: () => import('./admin/admin-ride-details/admin-ride-details').then(m => m.AdminRideDetailsComponent)
  },
  {
    path: 'driver-my-rides',
    loadComponent: () => import('./driver/driver-my-rides/driver-my-rides').then(m => m.DriverMyRidesComponent)
  },
  {
    path: 'passenger-home',
    loadComponent: () => import('./passenger/passenger-home/passenger-home').then(m => m.PassengerHomeComponent)
  },
  {
    path: 'passenger-my-rides',
    loadComponent: () => import('./passenger/passenger-my-rides/passenger-my-rides').then(m => m.PassengerMyRidesComponent)
  },
  {
    path: 'driver-additional-info',
    loadComponent: () => import('./driver-additional-info/driver-additional-info').then(m => m.DriverAdditionalInfo)
  },
  {
    path: '**',
    redirectTo: ''
  }
];

export const appConfig = {
  providers: [
    provideRouter(routes)
  ]
};
