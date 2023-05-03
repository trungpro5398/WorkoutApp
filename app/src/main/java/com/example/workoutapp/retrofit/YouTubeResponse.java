package com.example.workoutapp.retrofit;

import java.util.List;

public class YouTubeResponse {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        public Id id;
        public Snippet snippet;

        public static class Id {
            public String videoId;
        }

        public static class Snippet {
            public String title;
            public Thumbnails thumbnails;

            public static class Thumbnails {
                public High high;

                public static class High {
                    public String url;
                }
            }
        }
    }
}
