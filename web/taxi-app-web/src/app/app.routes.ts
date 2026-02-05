import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { ForgotPasswordComponent } from './auth/forgot-password/forgot-password';
import { ResetPasswordComponent } from './auth/reset-password/reset-password';
import { HomeComponent } from './home/home';
import { DriverHomeComponent } from './driver/driver-home/driver-home';
import { UserInfoComponent } from './user-info/user-info';
import { DriverRideHistoryComponent } from './driver/driver-ride-history/driver-ride-history';
import { RideDetailsComponent } from './shared/ride-details/ride-details';
import { EstimateRouteComponent } from './estimate-route/estimate-route';
import { AdminHomeComponent } from './admin/admin-home/admin-home';
import { AdminRideHistoryComponent } from './admin/admin-ride-history/admin-ride-history';
import { AdminRideDetailsComponent } from './admin/admin-ride-details/admin-ride-details';
import { DriverMyRidesComponent } from './driver/driver-my-rides/driver-my-rides';
import { PassengerMyRidesComponent } from './passenger/passenger-my-rides/passenger-my-rides';
import { PassengerHomeComponent } from './passenger/passenger-home/passenger-home';
import { DriverAdditionalInfoComponent } from './driver/driver-additional-info/driver-additional-info'; 
import { DriverRegistrationComponent } from './auth/register-driver/driver-registration-component/driver-registration-component'; 
import { OrderRideRegisteredUser } from './passenger/order-ride-registered-user/order-ride-registered-user';
import { ReviewFormComponent } from './reviews/review-form/review-form';
import { FavoriteRoutesComponent } from './passenger/favorite-routes/favorite-routes';
import { AdminPanicPageComponent } from './admin/admin-panic-page/admin-panic-page';
import { StartRideComponent } from './driver/start-ride/start-ride';
import { DriverReviewsComponent } from './driver/driver-reviews/driver-reviews';
import { ActiveRideTrackingComponent } from './active-ride-tracking/active-ride-tracking';
import { TestComponent } from './test/test';
import { PassengerRideDetailsComponent } from './passenger/passenger-ride-details/passenger-ride-details';
import { ChangeProfileRequestComponent } from './change-profile-request/change-profile-request';
import { DriverProfileChangeReviewComponent } from './driver-profile-change-review/driver-profile-change-review';
import { PassengerReviewsComponent } from './passenger/passenger-reviews/passenger-reviews';
import { PassengerNotificationsComponent } from './passenger/passenger-notifications/passenger-notifications';

export const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'login', component: LoginComponent},
  { path: 'user-info', component: UserInfoComponent },
  { path: 'driver-home', component: DriverHomeComponent },
  { path: 'driver-ride-history', component: DriverRideHistoryComponent },
  { path: 'ride-details/:rideId', component: RideDetailsComponent },
  { path: 'estimate-route', component: EstimateRouteComponent },
  { path: 'admin-home', component: AdminHomeComponent },
  { path: 'admin-ride-history', component: AdminRideHistoryComponent },
  { path: 'admin/admin-ride-details/:id', component: AdminRideDetailsComponent },
  { path: 'driver-my-rides', component: DriverMyRidesComponent },
  { path: 'passenger-home', component: PassengerHomeComponent},
  { path: 'passenger/passenger-my-rides', component: PassengerMyRidesComponent },
  { path: 'passenger/passenger-ride-details/:id', component: PassengerRideDetailsComponent },
  { path: 'driver-additional-info', component: DriverAdditionalInfoComponent},
  { path: 'register-driver', component: DriverRegistrationComponent },
  { path: 'order-ride-registered-user', component: OrderRideRegisteredUser },
  { path: 'review-form', component: ReviewFormComponent }, // this needs to be removed later
  { path: 'favorite-routes', component: FavoriteRoutesComponent },
  { path: 'admin/panic-notifications', component: AdminPanicPageComponent},
  { path: 'start-ride', component: StartRideComponent },
  { path: 'driver/:driverId/reviews', component: DriverReviewsComponent },
  { path: 'active-ride-tracking', component: ActiveRideTrackingComponent },
  { path: 'change-profile-request', component: ChangeProfileRequestComponent },
  { path: 'test', component: TestComponent},
  { path: 'driver-profile-change-review/:requestId', component: DriverProfileChangeReviewComponent},
  { path: 'driver-reviews', component: DriverReviewsComponent },
  { path: 'active-ride-tracking/:rideId', component: ActiveRideTrackingComponent },
  { path: 'test', component: TestComponent},
  { path: 'passenger-reviews', component: PassengerReviewsComponent },
  { path: 'passenger-notifications', component: PassengerNotificationsComponent },
  { path: '**', redirectTo: '' },
];
