package io.jenkins.plugins.harbor.steps;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Image implements Serializable {
    public static final String IMAGE_FORMAT_PATTERN = "([^/]+)/([^/]+)/(.+):([^/]+)";
    public static final String IMAGE_DIGEST_FORMAT_PATTERN = "([^/]+)/([^/]+)/(.+)@([^/]+)";
    private final String imageName;
    private final String imageDigest;
    private String registry;
    private String projects;
    private String repository;

    public Image(String imageName, String imageDigest) {
        this.imageName = imageName;
        this.imageDigest = imageDigest;
        processImageName();
    }

    private void processImageName() {
        boolean isDigestRepo = imageName.contains("@");
        Pattern pattern =
                isDigestRepo ? Pattern.compile(IMAGE_DIGEST_FORMAT_PATTERN) : Pattern.compile(IMAGE_FORMAT_PATTERN);
        Matcher matcher = pattern.matcher(imageName);
        if (matcher.find()) {
            registry = matcher.group(1);
            projects = matcher.group(2);
            repository = matcher.group(3);
        } else {
            throw new IllegalArgumentException(
                    "Image parameter process failed, please enter the correct image format.");
        }
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageDigest() {
        return imageDigest;
    }

    public String getRegistry() {
        return registry;
    }

    public String getProjects() {
        return projects;
    }

    public String getRepository() {
        return repository;
    }

    @Override
    public String toString() {
        return "Image{" + "imageName='"
                + imageName + '\'' + ", imageDigest='"
                + imageDigest + '\'' + ", registry='"
                + registry + '\'' + ", projects='"
                + projects + '\'' + ", repository='"
                + repository + '\'' + '}';
    }
}
