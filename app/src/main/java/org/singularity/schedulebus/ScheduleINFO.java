package org.singularity.schedulebus;

import java.util.HashMap;

/**
 * Created by The Machine on 8/2/2015.
 */
public class ScheduleINFO {

        private HashMap<String, String> nameValuePairs = new HashMap<String, String>() {
            {
                put("index", " ");
                put("departRU", " ");
                put("lighRAIL", " ");
                put("sdfcGYM", " ");
                put("garageMU", " ");
                put("univCollege", " ");
                put("univBridge", " ");

            }

        };
    /*CTO*/

        public ScheduleINFO() {
        }

        public String getGeneric(String commandHeader) {

            return nameValuePairs.get(commandHeader);

        /* Replace all the get(s)
         is the same that map.get("serialnumber").
         You can use a String vector with the names.
         */
        }

        public String setGeneric(String commandHeader, String value) {

            return nameValuePairs.put(commandHeader, value);

        /* Replace all the set(s)
         is the same that map.set("serialnumber", "value).
         You can use a String vector with the names.
         */
        }

        public int getMapSize() {
            return nameValuePairs.size();
        }
}
