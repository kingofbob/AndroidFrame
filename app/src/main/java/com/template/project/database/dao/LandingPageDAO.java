package com.template.project.database.dao;

import com.activeandroid.query.Select;
import com.template.project.database.entities.EventList;

import java.util.List;

/**
 * Created by 00020443 on 8/12/2016.
 */

public class LandingPageDAO {


    public static List<EventList> getLandingPage(){

        return new Select()
                .from(EventList.class)
                .execute();
    }
}
