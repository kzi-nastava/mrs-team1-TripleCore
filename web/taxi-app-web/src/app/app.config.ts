import { provideRouter, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';


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
    loadComponent: () => import('./driver/driver-home/driver-home').then(m => m.DriverHomeComponent)
  },
  {
    path: 'user-info',
    loadComponent: () => import('./user-info/user-info').then(m => m.UserInfoComponent)

  },
  {
    path: 'driver/:driver-id/ride-history',
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
    loadComponent: () => import('./driver/driver-additional-info/driver-additional-info').then(m => m.DriverAdditionalInfoComponent)
  },
  {
    path: 'register-driver',
    loadComponent: () => import('./auth/register-driver/driver-registration-component/driver-registration-component').then(m => m.DriverRegistrationComponent)
  },
  {
    path: 'order-ride-registered-user',
    loadComponent: () => import('./passenger/order-ride-registered-user/order-ride-registered-user').then(m => m.OrderRideRegisteredUser)
  },
  {
    path: 'review-form',
    loadComponent: () => import('./reviews/review-form/review-form').then(m => m.ReviewFormComponent)
  },
  {
    path : 'favorite-routes',
    loadComponent: () => import('./passenger/favorite-routes/favorite-routes').then(m => m.FavoriteRoutesComponent)
  },
  {
    path: 'admin/panic-notifications',
    loadComponent: () =>
      import('./admin/admin-panic-page/admin-panic-page').then(m => m.AdminPanicPageComponent)
  },
  {
    path: 'start-ride',
    loadComponent: () => import('./driver/start-ride/start-ride').then(m => m.StartRideComponent)
  },
  {
    path: 'driver/:driverId/reviews',
    loadComponent: () => import('./driver/driver-reviews/driver-reviews').then(m => m.DriverReviewsComponent)
  },
  {
    path: 'active-ride-tracking',
    loadComponent: () => import('./active-ride-tracking/active-ride-tracking').then(m => m.ActiveRideTrackingComponent)
  },
  {
    path: 'test',
    loadComponent: () => import('./test/test').then(m => m.TestComponent)
  },
  {
    path: 'passenger-ride-details/:id',
    loadComponent: () => import('./passenger/passenger-ride-details/passenger-ride-details').then(m => m.PassengerRideDetailsComponent)
  },
  {
    path: 'passenger-reviews',
    loadComponent: () => import('./passenger/passenger-reviews/passenger-reviews').then(m => m.PassengerReviewsComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];

export const appConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]
};
