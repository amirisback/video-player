package com.frogobox.kickstart.util.lib.youtube;


import androidx.annotation.NonNull;

import java.util.Objects;

public class YtFile {

    private final Format format;
    private final String url;

    YtFile(Format format, String url) {
        this.format = format;
        this.url = url;
    }

    /**
     * The url to download the file.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Format data for the specific file.
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Format data for the specific file.
     */
    @Deprecated
    public Format getMeta() {
        return format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YtFile ytFile = (YtFile) o;

        if (!Objects.equals(format, ytFile.format)) return false;
        return Objects.equals(url, ytFile.url);
    }

    @Override
    public int hashCode() {
        int result = format != null ? format.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "YtFile{" +
                "format=" + format +
                ", url='" + url + '\'' +
                '}';
    }
}
