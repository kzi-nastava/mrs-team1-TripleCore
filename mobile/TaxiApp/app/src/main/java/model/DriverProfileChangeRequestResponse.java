package model;

import java.time.LocalDateTime;

public class DriverProfileChangeRequestResponse {

        private Long id;
        private String email;
        private String createdAt;
        private String status;


        public DriverProfileChangeRequestResponse() { }

        public DriverProfileChangeRequestResponse(DriverProfileChangeRequestResponse req) {
            this.id = req.getId();
            this.email = req.getEmail();
            this.createdAt = req.getCreatedAt();
            this.status = req.getStatus();
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getStatus() {
            return status;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setStatus(String status) {
            this.status = status;
        }



}
