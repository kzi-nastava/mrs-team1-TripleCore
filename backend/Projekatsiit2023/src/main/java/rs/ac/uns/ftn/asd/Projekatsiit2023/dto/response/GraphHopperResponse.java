package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)  // ignore unknown properties during deserialization
public class GraphHopperResponse {

    @JsonProperty("hints")
    private Map<String, Object> hints;

    @JsonProperty("info")
    private Map<String, Object> info;

    @JsonProperty("paths")
    private List<Path> paths;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Path {
        @JsonProperty("distance")
        private double distance; // in meters

        @JsonProperty("time")
        private long time; // in milliseconds

        @JsonProperty("points")
        private Points points;

        @JsonProperty("points_encoded")
        private boolean pointsEncoded;

        @JsonProperty("bbox")
        private List<Double> bbox;

        @JsonProperty("instructions")
        private List<Object> instructions;

        @JsonProperty("details")
        private Map<String, Object> details;

        @JsonProperty("ascend")
        private double ascend;

        @JsonProperty("descend")
        private double descend;

        @JsonProperty("snapped_waypoints")
        private Object snappedWaypoints;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Points {
            @JsonProperty("type")
            private String type;

            @JsonProperty("coordinates")
            private List<List<Double>> coordinates; // [[lon, lat], [lon, lat], ...]
        }
    }
}