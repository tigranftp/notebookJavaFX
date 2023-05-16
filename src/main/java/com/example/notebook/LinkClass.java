package com.example.notebook;

public class LinkClass {
    private final String link;
    private final String description;
    private final String category;

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }


    public String getCategory() {
        return category;
    }

    public LinkClass(String link, String description, String category) {
        this.link = link;
        this.description = description;
        this.category = category;
    }
}
