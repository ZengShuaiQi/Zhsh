package domain;

import java.util.ArrayList;

/**
 * Created by clever boy on 2017/5/17.
 */

public class PhotoBean {
    public int retcode;
    public PhotoData data;
    public class PhotoData{
        public ArrayList<PhotoNewsData> news;
    }
    public class PhotoNewsData{
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String url;
    }
}
