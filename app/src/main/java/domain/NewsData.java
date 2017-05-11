package domain;

import java.util.ArrayList;

/**
 * Created by clever boy on 2017/5/1.
 */

public class NewsData {

    public int retcode;
    public NewsTab data;

    public class NewsTab{
        public ArrayList<TopNews> topnews;
        public ArrayList<News> news;
        public String title;
        public String more;

    }
    public class TopNews{
        public String id;
        public String pubdate;
        public String title;
        public String topimage;
        public String url;
    }
    public class News{
        public String id;
        public String pubdate;
        public String title;
        public String listimage;
        public String url;
    }
}
