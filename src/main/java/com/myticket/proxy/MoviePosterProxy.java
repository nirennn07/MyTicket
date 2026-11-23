package com.myticket.proxy;

import javax.swing.ImageIcon;

public class MoviePosterProxy implements MoviePoster {

    private RealMoviePoster realMoviePoster;

    private String thumbnailPath;
    private String highResolutionPath;

    private ImageIcon thumbnail;
    private ImageIcon highResolutionPoster;

    public MoviePosterProxy(
            String thumbnailPath,
            String highResolutionPath) {

        this.thumbnailPath = thumbnailPath;
        this.highResolutionPath = highResolutionPath;
    }

    @Override
    public ImageIcon getThumbnail() {

        if (thumbnail == null) {
            getRealMoviePoster();
            thumbnail = realMoviePoster.getThumbnail();
        }

        return thumbnail;
    }

    @Override
    public ImageIcon getHighResolutionPoster() {

        if (highResolutionPoster == null) {
            getRealMoviePoster();
            highResolutionPoster =
                    realMoviePoster.getHighResolutionPoster();
        }

        return highResolutionPoster;
    }

    private RealMoviePoster getRealMoviePoster() {

        if (realMoviePoster == null) {
            realMoviePoster = new RealMoviePoster(
                    thumbnailPath,
                    highResolutionPath
            );
        }

        return realMoviePoster;
    }
}