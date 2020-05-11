package com.my.musicplayer;

import android.graphics.Bitmap;

public class AudioModel {
    String aPath;
    String aName;
    String aAlbum;
    String aAlbumURI;
    String aArtist;
    Bitmap aAlbumImage;

    public Bitmap getaAlbumImage() {
        return aAlbumImage;
    }

    public String getaAlbumURI() {
        return aAlbumURI;
    }

    public void setaAlbumURI(String aAlbumURI) {
        this.aAlbumURI = aAlbumURI;
    }

    public void setaAlbumImage(Bitmap aAlbumImage) {
        this.aAlbumImage = aAlbumImage;
    }

    public String getaPath() {
        return aPath;
    }

    public void setaPath(String aPath) {
        this.aPath = aPath;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaAlbum() {
        return aAlbum;
    }

    public void setaAlbum(String aAlbum) {
        this.aAlbum = aAlbum;
    }

    public String getaArtist() {
        return aArtist;
    }

    public void setaArtist(String aArtist) {
        this.aArtist = aArtist;
    }
}