package com.acipi.evote.helpers;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

/**
 * Created by Altin Cipi on 3/7/2015.
 */
public class GeneralHelper
{
    public static long getDateDiff(DateTime date1, DateTime date2, TimeUnit timeUnit)
    {
        long diffInMillies = date2.getMillis() - date1.getMillis();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
