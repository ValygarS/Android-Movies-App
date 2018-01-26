package com.kkmovies.kankanmovies.Helpers;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO
public class DateHelper {

    public static String parseDate(String originalDate){

        if (originalDate==null){

        }

        SimpleDateFormat dateReceived = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateParsed = new SimpleDateFormat("d MMM yyyy");
        String dateRequired = "";

        try{
            Date airDate = dateReceived.parse(originalDate);
            dateRequired = dateParsed.format(airDate);


        }catch (ParseException e){
            e.printStackTrace();
        }

        return dateRequired;
    }

    public static String concatToYear(String originalDate){
        if(originalDate.length()>0){
            String year = originalDate.substring(0, originalDate.indexOf('-'));
            return year;
        }
        else{
            return originalDate;
        }


    }
}
