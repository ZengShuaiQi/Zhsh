package domain;

import java.util.ArrayList;

/**
 * Created by clever boy on 2017/4/24.
 */

public class NewsMenuData {
    public ArrayList<NewsData> data;
    public ArrayList<String> extend;
    public int retcode;

    public class NewsData{
        public String id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return id;
        }
    }
    public class NewsTabData{
        public String id;
        public String title;
        public int type;
        public String url;
        public String toString() {
            return url;
        }
    }

    @Override
    public String toString() {
        return retcode+"";
    }
}
