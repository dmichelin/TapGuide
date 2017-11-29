package tech.danielmichelin.tapguide.Model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.yelp.fusion.client.models.Business;

/**
 * Created by eriks_pc on 11/29/2017.
 */

public class TGBusiness extends Business {
    @JsonGetter("eventType")
    public String getEventType() {
        return this.eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    String eventType;
}
