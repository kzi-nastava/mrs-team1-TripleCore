import { RegisterComponent } from './register';
import { of, throwError } from 'rxjs';
import { describe, it, expect, beforeEach, vi } from 'vitest';

// run with: npx vitest register.spec.ts

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let mockRegisterService: any;
  let mockRouter: any;

  beforeEach(() => {
    // mock global alert to prevent actual alerts during tests
    vi.spyOn(window, 'alert').mockImplementation(() => {});

    mockRegisterService = {   
      register: vi.fn()
    };

    mockRouter = {
      navigate: vi.fn()
    };

    component = new RegisterComponent(
      { detectChanges: vi.fn() } as any, // mock ChangeDetectorRef
      mockRegisterService,
      mockRouter
    );
  });

  // test if component is created successfully
  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  // ---------------- FORM VALIDATION ----------------

  // group form validation tests
  describe('Form validation', () => {
    // test invalid form with empty fields
    it('should be invalid when fields are empty', () => {
      expect(component.isFormValid()).toBe(false);  // all fields are empty by default
    });

    // test valid form
    it('should be valid when all fields are properly filled', () => {
      component.firstName = 'John';
      component.lastName = 'Doe';
      component.address = 'Test Street 123';
      component.phone = '+123456789';
      component.email = 'test@example.com';
      component.password = 'password123';
      component.confirmPassword = 'password123';

      expect(component.isFormValid()).toBe(true);
    });

    // test invalid form when passwords do not match
    it('should be invalid when passwords do not match', () => {
      component.firstName = 'John';
      component.lastName = 'Doe';
      component.address = 'Test Street 123';
      component.phone = '+123456789';
      component.email = 'test@example.com';
      component.password = 'password123';
      component.confirmPassword = 'wrong';

      expect(component.isFormValid()).toBe(false);
    });

    // test invalid form when password length is less than 6 characters
    it('should be invalid when password is too short', () => {
      component.firstName = 'John';
      component.lastName = 'Doe';
      component.address = 'Test Street 123';
      component.phone = '+123456789';
      component.email = 'test@example.com';
      component.password = '12345';
      component.confirmPassword = '12345';

      expect(component.isFormValid()).toBe(false);
    });
  });

  // ---------------- FIELD VALIDATION ----------------

  describe('Field validation', () => {
    // test email format validation
    it('should validate email format', () => {
      component.firstName = 'John';
      component.lastName = 'Doe';
      component.address = 'Test';
      component.phone = '+123456789';
      component.email = 'invalid-email';
      component.password = 'password123';
      component.confirmPassword = 'password123';

      component.register();

      expect(component.errorMessage).toContain('valid email');
    });

    // test phone number format validation
    it('should validate phone number format', () => {
      component.firstName = 'John';
      component.lastName = 'Doe';
      component.address = 'Test';
      component.phone = 'abc';
      component.email = 'test@example.com';
      component.password = 'password123';
      component.confirmPassword = 'password123';

      component.register();

      expect(component.errorMessage).toContain('valid phone number');
    });
  });

  // ---------------- SUCCESS REGISTRATION ----------------

  describe('Successful registration', () => {
    beforeEach(() => {
      component.firstName = 'John';
      component.lastName = 'Doe';
      component.address = 'Test';
      component.phone = '+123456789';
      component.email = 'test@example.com';
      component.password = 'password123';
      component.confirmPassword = 'password123';

      mockRegisterService.register.mockReturnValue(of({}));   
    });

    // test if register service is called with correct data
    it('should call register service with correct data', () => {
      component.register();

      expect(mockRegisterService.register).toHaveBeenCalledWith({
        firstName: 'John',
        lastName: 'Doe',
        address: 'Test',
        phoneNumber: '+123456789',
        email: 'test@example.com',
        password: 'password123',
        confirmPassword: 'password123',
        profileImage: ''
      });
    });

    it('should navigate to login on success', () => {
      component.register();

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login'], {
        queryParams: {
          registered: 'true',
          email: 'test@example.com'
        }
      });
    });

    it('should reset form after successful registration', () => {
      component.register();

      expect(component.firstName).toBe('');
      expect(component.email).toBe('');
      expect(component.loading).toBe(false);
    });
  });

  // ---------------- FAILED REGISTRATION ----------------

  describe('Failed registration', () => {
    beforeEach(() => {
      component.firstName = 'John';
      component.lastName = 'Doe';
      component.address = 'Test';
      component.phone = '+123456789';
      component.email = 'test@example.com';
      component.password = 'password123';
      component.confirmPassword = 'password123';
    });

    // test 400 error handling
    it('should handle 400 error', () => {
      mockRegisterService.register.mockReturnValue(
        throwError(() => ({ status: 400, error: 'Invalid data' }))
      );

      component.register();

      expect(component.errorMessage).toBe('Invalid data');
      expect(component.loading).toBe(false);
    });

    // test 409 error handling - email already exists
    it('should handle 409 error', () => {
      mockRegisterService.register.mockReturnValue(
        throwError(() => ({ status: 409, error: 'already exists' }))
      );

      component.register();

      expect(component.errorMessage).toContain('already registered');
    });

    // test network error handling
    it('should handle network error', () => {
      mockRegisterService.register.mockReturnValue(
        throwError(() => ({ status: 0 }))
      );

      component.register();

      expect(component.errorMessage).toContain('Cannot connect');
    });
  });

  // ---------------- PROFILE PICTURE ----------------

  describe('Profile picture handling', () => {
    it('should set default profile picture on init', () => {
      expect(component.profilePic).toBe('icons/profile.png');
    });

    it('should remove profile picture', () => {
      component.profilePic = 'image-data';
      component.selectedFile = new File([''], 'test.jpg');

      component.removeProfilePicture();

      expect(component.profilePic).toBe('icons/profile.png');
      expect(component.selectedFile).toBeNull();
    });
  });

  // ---------------- CANCEL REGISTRATION ----------------

  describe('Cancel functionality', () => {
    // cancel should reset form and navigate to login if confirmed
    it('should reset and navigate when confirmed', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(true);

      component.firstName = 'John';
      component.cancel();

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should not navigate if declined', () => {
      vi.spyOn(window, 'confirm').mockReturnValue(false);

      component.firstName = 'John';
      component.cancel();

      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });
  });
});
