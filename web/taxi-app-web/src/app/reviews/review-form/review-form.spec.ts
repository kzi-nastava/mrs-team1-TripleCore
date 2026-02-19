import { describe, it, expect, beforeEach, vi } from 'vitest';
import { of, throwError } from 'rxjs';
import { ReviewFormComponent } from './review-form';

describe('ReviewFormComponent', () => {
  let component: ReviewFormComponent;
  let mockReviewService: any;

  beforeEach(() => {
    mockReviewService = {
      createReview: vi.fn()
    };

    // mock localStorage
    vi.spyOn(Storage.prototype, 'getItem').mockReturnValue('5');

    component = new ReviewFormComponent(mockReviewService);
    component.rideId = 10;
  });


  // test if component is created successfully
  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize passengerId from localStorage', () => {
    expect(component.passengerId).toBe(5);
  });

  it('should initialize default values', () => {
    expect(component.driverRating).toBe(1);
    expect(component.vehicleRating).toBe(1);
    expect(component.comment).toBe('');
  });


  // rating functionality tests
  describe('Rating functionality', () => {
    it('should set driver rating', () => {
      component.setDriverRating(4);
      expect(component.driverRating).toBe(4);
    });

    it('should set vehicle rating', () => {
      component.setVehicleRating(3);
      expect(component.vehicleRating).toBe(3);
    });
  });


  // form validation tests
  describe('Form validation', () => {
    it('should be valid when ratings > 0', () => {
      component.driverRating = 5;
      component.vehicleRating = 4;

      expect(component.isFormValid()).toBe(true);
    });

    it('should be invalid when driver rating is 0', () => {
      component.driverRating = 0;
      component.vehicleRating = 4;

      expect(component.isFormValid()).toBe(false);
    });

    it('should be invalid when vehicle rating is 0', () => {
      component.driverRating = 3;
      component.vehicleRating = 0;

      expect(component.isFormValid()).toBe(false);
    });
  });

  // submit functionality tests
  describe('Successful submit', () => {
    beforeEach(() => {
      component.driverRating = 5;
      component.vehicleRating = 4;
      component.comment = 'Great ride!';

      mockReviewService.createReview.mockReturnValue(of('Success'));
    });

    it('should call reviewService with correct data', () => {
      component.submit();

      expect(mockReviewService.createReview).toHaveBeenCalledWith({
        passengerId: 5,
        rideId: 10,
        driverRating: 5,
        vehicleRating: 4,
        comment: 'Great ride!'
      });
    });

    it('should reset form after successful submit', () => {
      component.submit();

      expect(component.driverRating).toBe(1);
      expect(component.vehicleRating).toBe(1);
      expect(component.comment).toBe('');
    });
  });


  // error handling tests
  describe('Submit error handling', () => {
    it('should handle error without crashing', () => {
      component.driverRating = 5;
      component.vehicleRating = 5;

      mockReviewService.createReview.mockReturnValue(
        throwError(() => new Error('Server error'))
      );

      expect(() => component.submit()).not.toThrow();
      expect(mockReviewService.createReview).toHaveBeenCalled();
    });
  });


  // cancel functionality tests
  describe('Cancel functionality', () => {
    it('should reset form on cancel', () => {
      component.driverRating = 5;
      component.vehicleRating = 4;
      component.comment = 'Test';

      component.cancel();

      expect(component.driverRating).toBe(1);
      expect(component.vehicleRating).toBe(1);
      expect(component.comment).toBe('');
    });
  });


  // close event tests
  describe('Close event', () => {
    it('should emit close event', () => {
      const emitSpy = vi.spyOn(component.close, 'emit');

      component.closeSelf();

      expect(emitSpy).toHaveBeenCalled();
    });
  });
});
