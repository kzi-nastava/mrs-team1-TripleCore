package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

public class RideFinishResponse {
    private String message;
    private boolean priceChanged;
    private double newPrice;

    public RideFinishResponse(String message, boolean priceChanged, double newPrice){
        this.message = message;
        this.priceChanged = priceChanged;
        this.newPrice = newPrice;
    }

    public String getMessage(){
        return this.message;
    }

    public boolean getPriceChanged(){
        return priceChanged;
    }

    public double getNewPrice(){
        return newPrice;
    }


}
