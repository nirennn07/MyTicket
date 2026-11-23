package com.myticket.proxy;

import javax.swing.ImageIcon;
import java.io.File;

public class RealMoviePoster implements MoviePoster {

    private String thumbnailPath;
    private String highResolutionPath;

    private ImageIcon thumbnail;
    private ImageIcon highResolutionPoster;

    public RealMoviePoster(String thumbnailPath, String highResolutionPath) {
        this.thumbnailPath = thumbnailPath;
        this.highResolutionPath = highResolutionPath;
    }

    @Override
    public ImageIcon getThumbnail() {

        if (thumbnail == null) {
            thumbnail = loadImage(thumbnailPath);
        }

        return thumbnail;
    }

    @Override
    public ImageIcon getHighResolutionPoster() {

        if (highResolutionPoster == null) {
            highResolutionPoster = loadImage(highResolutionPath);
        }

        return highResolutionPoster;
    }

    private ImageIcon loadImage(String path) {

        File file = new File(path);

        if (!file.exists()) {
            System.out.println("Poster image not found: " + path);
            return new ImageIcon();
        }

        return new ImageIcon(path);
    }
}