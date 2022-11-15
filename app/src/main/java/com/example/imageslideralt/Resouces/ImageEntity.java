package com.example.imageslideralt.Resouces;

public class ImageEntity {
    private String ImageUrl;

    public ImageEntity() {
    }

    public ImageEntity(String image) {
        this.ImageUrl = image;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String image) {
        this.ImageUrl = image;
    }
}
