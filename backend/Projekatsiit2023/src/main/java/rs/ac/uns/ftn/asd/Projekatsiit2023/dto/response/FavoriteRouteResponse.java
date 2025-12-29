package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

public class FavoriteRouteResponse {
    private Long id;
    private String routeName;
    private String origin;
    private String destination;
    private String message;

    public FavoriteRouteResponse(Long id, String origin, String destination, String message) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }



}

