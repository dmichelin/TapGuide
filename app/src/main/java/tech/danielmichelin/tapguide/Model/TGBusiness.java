package tech.danielmichelin.tapguide.Model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.yelp.fusion.client.models.Business;

/**
 * Created by eriks_pc on 11/29/2017.
 */

public class TGBusiness extends Business {

    public TGBusiness(Business parent){
        setCategories(parent.getCategories());
        setCoordinates(parent.getCoordinates());
        setDisplayPhone(parent.getDisplayPhone());
        setDistance(parent.getDistance());
        setHours(parent.getHours());
        setId(parent.getId());
        setImageUrl(parent.getImageUrl());
        setIsClaimed(parent.getIsClaimed());
        setIsClosed(parent.getIsClosed());
        setLocation(parent.getLocation());
        setName(parent.getName());
        setPhone(parent.getPhone());
        setPrice(parent.getPrice());
        setReviewCount(parent.getReviewCount());
        setRating(parent.getRating());
        setText(parent.getText());
        setUrl(parent.getUrl());
    }

    public TGBusiness(Business parent, String eventType){
        this(parent);
        setEventType(eventType);
    }
    @JsonGetter("eventType")
    public String getEventType() {
        return this.eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    String eventType;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Business) {
            return this.getId().equals(((Business) obj).getId());
        } else return false;
    }
}
