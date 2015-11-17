package io.dwak.holohackernews.app.models;

public class AboutLicense {
    public final int nameResId;
    public final int licenseResId;

    public AboutLicense(int nameResId, int license) {
        this.nameResId = nameResId;
        this.licenseResId = license;
    }
}
