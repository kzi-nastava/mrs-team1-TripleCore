package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileChangeRequestResponse;

import java.util.List;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileChangeRequestDetailsResponse;

public interface DriverProfileChangeRequestService {
    public List<DriverProfileChangeRequestResponse> getAllPending();
    public DriverProfileChangeRequestDetailsResponse getDetails(Long requestId);
    public void approveRequest(Long requestId);
    public void rejectRequest(Long requestId);
}
